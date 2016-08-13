package com.example.wf;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import javax.annotation.PreDestroy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.amazonaws.services.simpleworkflow.AmazonSimpleWorkflowClient;
import com.amazonaws.services.simpleworkflow.model.ActivityTaskCompletedEventAttributes;
import com.amazonaws.services.simpleworkflow.model.ActivityTaskScheduledEventAttributes;
import com.amazonaws.services.simpleworkflow.model.ActivityTaskStartedEventAttributes;
import com.amazonaws.services.simpleworkflow.model.ActivityType;
import com.amazonaws.services.simpleworkflow.model.CompleteWorkflowExecutionDecisionAttributes;
import com.amazonaws.services.simpleworkflow.model.Decision;
import com.amazonaws.services.simpleworkflow.model.DecisionTask;
import static com.amazonaws.services.simpleworkflow.model.DecisionType.*;
import com.amazonaws.services.simpleworkflow.model.HistoryEvent;
import com.amazonaws.services.simpleworkflow.model.PollForDecisionTaskRequest;
import com.amazonaws.services.simpleworkflow.model.RespondDecisionTaskCompletedRequest;
import com.amazonaws.services.simpleworkflow.model.ScheduleActivityTaskDecisionAttributes;
import static com.example.wf.Utility.*;

@Component
public class Decider {

	Logger logger = LoggerFactory.getLogger(Decider.class);

	@Autowired AmazonSimpleWorkflowClient swf;
    private CountDownLatch waitForTermination = new CountDownLatch(1);	//	Used to terminate gracefully.
	private boolean shutdown = false;
	
	/**
	 * Poll the SWF service for decision tasks.  Do this in a dedicated thread.
	 */
	@Async
	public void pollForDecisions () {
	    PollForDecisionTaskRequest req =
            new PollForDecisionTaskRequest()
                .withDomain(DOMAIN_NAME)
                .withTaskList(getTaskList());

        while (!shutdown) {
        	logger.info("Polling for a decision task from the tasklist '" +
                    TASKLIST + "' in the domain '" + DOMAIN_NAME + "'.");

            DecisionTask task = swf.pollForDecisionTask(req);

            if (task.getTaskToken() != null) {
                try {
                    decide(task);
                } catch (Throwable th) {
                    th.printStackTrace();
                }
            }
        }	
        
        //	Allow for graceful termination:
	    waitForTermination.countDown();
	}

	
	/**
	 * Make decisions.  First do step 1, then step 2, then complete.  Easy!
	 */
	private void decide(DecisionTask task) throws Throwable {

		List<HistoryEvent> events = task.getEvents();
		logEventHistory(events);	// Adjust logging level as needed to debug.
		
	    //	Is step 2 complete?  Then signal the completion of the workflow.  
		//	The result of the workflow is whatever was returned from step 2:
	    if ( isActivityComplete(events, ACTIVITY_2)) {
	        logger.info("Decision: workflow execution " + task.getWorkflowExecution().getRunId() + " is complete.");
	        signalWorkflowComplete(task, getActivityResult(task.getEvents(),ACTIVITY_2));
	        return;
		} 
			
		//	Otherwise, if only step 1 is complete, schedule step 2.
	    //	The input for step 2 is the output from step 1:
		if(  isActivityComplete(events, ACTIVITY_1) ) {
	        logger.info("Decision: schedule step 2 of execution " + task.getWorkflowExecution().getRunId());
			scheduleStep(task, ACTIVITY_2, ACTIVITY_VERSION, getActivityResult(task.getEvents(),ACTIVITY_1));
	        return;
		}
		
		//	Otherwise, start activity 1.
		//	The input into step one is the original input into the workflow:
        logger.info("Decision: schedule step 1 of execution " + task.getWorkflowExecution().getRunId());
		scheduleStep(task, ACTIVITY_1, ACTIVITY_VERSION, getOriginalWorkflowInput(task.getEvents()));
	}
	
	
	/**
	 * Send a decision to SWF that this workflow execution is now complete.
	 */
	private void signalWorkflowComplete(DecisionTask task,String output) {
	    List<Decision> decisions = new ArrayList<Decision>();
        decisions.add(
            new Decision()
                .withDecisionType(CompleteWorkflowExecution)
                .withCompleteWorkflowExecutionDecisionAttributes(
                    new CompleteWorkflowExecutionDecisionAttributes()
                        .withResult(output)));
        returnDecision(decisions, task.getTaskToken());
	}
	
	
	/**
	 *	Send a decision to SWF that it is time to execute the given step / activity.
	 *	Steps / activities are all pretty much the same except for their names, so it
	 *	makes sense to have a single function take care of the bulk of the details: 
	 */
	private void scheduleStep(DecisionTask task, String activityName, String activityVersion, String input){
	    List<Decision> decisions = new ArrayList<Decision>();
        ScheduleActivityTaskDecisionAttributes attrs = null;
        attrs = new ScheduleActivityTaskDecisionAttributes()
            .withActivityId(UUID.randomUUID().toString())
            .withInput(input)
            .withActivityType(new ActivityType()
                .withName(activityName)
               	.withVersion(activityVersion));

        decisions.add(
            new Decision()
                .withDecisionType(ScheduleActivityTask)
                .withScheduleActivityTaskDecisionAttributes(attrs));
        
        returnDecision(decisions, task.getTaskToken());
	}		

	
	/**
	 *	Send the given decision to SWF. 
	 */
	private void returnDecision(List<Decision> decisions, String taskToken) {
	    //	RETURN the decision:
	    swf.respondDecisionTaskCompleted(
	    	new RespondDecisionTaskCompletedRequest()
	            .withTaskToken(taskToken)
	   	        .withDecisions(decisions));	    
	}
	
	
	/**
	 * Examine event history to determine if the given event is complete.
	 * This is done by seeing if there are any attributes from task completed events.
	 * If so, the task is done.  If not, the task is not yet complete.
	 */
	private boolean isActivityComplete(List<HistoryEvent> events, String type) {
		ActivityTaskCompletedEventAttributes attrs = 
				getActivityTaskCompletedEventAttributes(events, type);	
		return ( attrs != null );
	}

	
	/**
	 * Examine event history to obtain and return the String output (if any) associated
	 * with the given activity type.  If the activity is not complete, or if it does not
	 * return any output, results will be null;
	 */
	private String getActivityResult(List<HistoryEvent> events, String type) {
		
		ActivityTaskCompletedEventAttributes attrs = 
				getActivityTaskCompletedEventAttributes(events, type);	
		
		if ( attrs != null ) {
   			return attrs.getResult();
		}
        return null;
	}

	
	/**
	 * Examine event history to obtain and return the set of attributes associated with the given completed task.
	 * If the task is not completed, returns null.
	 */
	private ActivityTaskCompletedEventAttributes getActivityTaskCompletedEventAttributes(List<HistoryEvent> events, String type) {
		
		Long scheduledEventId = null;
		Long startedEventId = null;
		
	    for (HistoryEvent event : events) {
	    	
	        switch(event.getEventType()) {
            case "ActivityTaskScheduled":
            	//	Is this a record of the task being scheduled?  Does it match 
            	//	the event type we are seeking? If so, record the event id:
            	ActivityTaskScheduledEventAttributes scheduledAttrs = event.getActivityTaskScheduledEventAttributes();
            	if ( type.equals(scheduledAttrs.getActivityType().getName())) {
            		scheduledEventId = event.getEventId();
            	}
                break;
            case "ActivityTaskStarted":
            	//	Is this a record of the task being started?  Does it match the 
            	//	scheduled event Id we recorded earlier?  If so, record the event id:
            	ActivityTaskStartedEventAttributes startedAttrs = event.getActivityTaskStartedEventAttributes();
            	if ( startedAttrs.getScheduledEventId().equals(scheduledEventId)) {
            		startedEventId = event.getEventId();
            	}
                break;
            case "ActivityTaskCompleted":
            	//	Is this a record of a task being completed?  If so, see if the scheduled and started ids
            	//	match what we recorded earlier.  If so, this completion event is the one we are looking for:
            	ActivityTaskCompletedEventAttributes completedAttrs = event.getActivityTaskCompletedEventAttributes();
            	if(completedAttrs.getScheduledEventId().equals(scheduledEventId) &&
            		completedAttrs.getStartedEventId().equals(startedEventId) ) {
            			//	This is the set of attributes we are looking for, return:
            			return completedAttrs;
            	}
            	break;
	        }
	    }
        return null;
	}


	/**
	 * Obtain and return the original String input into the workflow:
	 */
	private String getOriginalWorkflowInput(List<HistoryEvent> events) {
		for (HistoryEvent event : events) {
		    if (event.getEventType().equals("WorkflowExecutionStarted")) {
		    	return event.getWorkflowExecutionStartedEventAttributes().getInput();
		    }
		}
		return null;
	}
	
	
	//	If we get the shutdown signal, stop looping:
	@PreDestroy
	private void shutdown() throws Exception {
		shutdown = true;
		logger.info("Waiting for the current poll request to return before shutting down.");
		waitForTermination.await(60, TimeUnit.SECONDS);		
	}
	
}
