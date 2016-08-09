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
import com.amazonaws.services.simpleworkflow.model.DecisionType;
import com.amazonaws.services.simpleworkflow.model.HistoryEvent;
import com.amazonaws.services.simpleworkflow.model.PollForDecisionTaskRequest;
import com.amazonaws.services.simpleworkflow.model.RespondDecisionTaskCompletedRequest;
import com.amazonaws.services.simpleworkflow.model.ScheduleActivityTaskDecisionAttributes;

@Component
public class Decider {

	Logger logger = LoggerFactory.getLogger(Decider.class);

	@Autowired AmazonSimpleWorkflowClient swf;
    private CountDownLatch waitForTermination = new CountDownLatch(1);
	private boolean shutdown = false;
	
	/**
	 * Poll the SWF service for decision tasks.  Do this in a dedicated thread.
	 */
	@Async
	public void pollForDecisions () {
	    PollForDecisionTaskRequest req =
            new PollForDecisionTaskRequest()
                .withDomain(Utility.DOMAIN_NAME)
                .withTaskList(Utility.getTaskList());

        while (!shutdown) {
        	logger.info("Polling for a decision task from the tasklist '" +
                    Utility.TASKLIST + "' in the domain '" +
                    Utility.DOMAIN_NAME + "'.");

            DecisionTask task = swf.pollForDecisionTask(req);

            String taskToken = task.getTaskToken();
            if (taskToken != null) {
                try {
                    decide(task);
                }
                catch (Throwable th) {
                    th.printStackTrace();
                }
            }
        }	
        
        //	Allow for graceful termination:
	    waitForTermination.countDown();
	}

	
	/**
	 * Make decisions.
	 */
	private void decide(DecisionTask task) throws Throwable {

	    List<Decision> decisions = new ArrayList<Decision>();
        ScheduleActivityTaskDecisionAttributes attrs = null;
		
		String taskToken = task.getTaskToken();
		List<HistoryEvent> events = task.getEvents();
		
		Utility.logEventHistory(events);
		
	    //	Is step 2 complete?  Then signal the completion of the workflow:
	    if ( isActivityComplete(events, Utility.ACTIVITY_2)) {
	        logger.info("Decision: workflow execution " + task.getWorkflowExecution().getRunId() + " is complete.");
	        decisions.add(
	            new Decision()
	                .withDecisionType(DecisionType.CompleteWorkflowExecution)
	                .withCompleteWorkflowExecutionDecisionAttributes(
	                    new CompleteWorkflowExecutionDecisionAttributes()
	                        .withResult(getActivityResult(events,Utility.ACTIVITY_2))));
	        returnDecision(decisions, taskToken);
	        return;
		} 
			
		//	Otherwise, if only step 1 is complete, schedule step 2:
		if(  isActivityComplete(events, Utility.ACTIVITY_1) ) {
	        logger.info("Decision: schedule step 2 of execution " + task.getWorkflowExecution().getRunId());
            attrs = new ScheduleActivityTaskDecisionAttributes()
                .withActivityId(UUID.randomUUID().toString())
                .withInput(getActivityResult(events,Utility.ACTIVITY_1))
                .withActivityType(new ActivityType()
                    .withName(Utility.ACTIVITY_2)
                   	.withVersion(Utility.ACTIVITY_VERSION));

            decisions.add(
                new Decision()
                    .withDecisionType(DecisionType.ScheduleActivityTask)
                    .withScheduleActivityTaskDecisionAttributes(attrs));
            
	        returnDecision(decisions, taskToken);
	        return;
		}

		
		//	Otherwise, start activity 1:
        logger.info("Decision: schedule step 1 of execution " + task.getWorkflowExecution().getRunId());

        attrs = new ScheduleActivityTaskDecisionAttributes()
            .withActivityId(UUID.randomUUID().toString())
            .withInput(getOriginalWorkflowInput(events))
            .withActivityType(new ActivityType()
                .withName(Utility.ACTIVITY_1)
               	.withVersion(Utility.ACTIVITY_VERSION));

        decisions.add(
            new Decision()
                .withDecisionType(DecisionType.ScheduleActivityTask)
                .withScheduleActivityTaskDecisionAttributes(attrs));
        
        returnDecision(decisions, taskToken);
        return;
	}
	
	
	private void returnDecision(List<Decision> decisions, String taskToken) {
	    //	RETURN the decision:
	    swf.respondDecisionTaskCompleted(
	    	new RespondDecisionTaskCompletedRequest()
	            .withTaskToken(taskToken)
	   	        .withDecisions(decisions));	    
	}
	
	
	/**
	 * Examine event history to determine if the given event is complete.
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
	 * Examine event history to obtain and return the seto of attributes associated with the given completed task.
	 * If the task is not completed, returns null.
	 */
	private ActivityTaskCompletedEventAttributes getActivityTaskCompletedEventAttributes(List<HistoryEvent> events, String type) {
		
		Long scheduledEventId = null;
		Long startedEventId = null;
		
	    for (HistoryEvent event : events) {
	    	
	        switch(event.getEventType()) {
            case "ActivityTaskScheduled":
            	ActivityTaskScheduledEventAttributes scheduledAttrs = event.getActivityTaskScheduledEventAttributes();
            	if ( type.equals(scheduledAttrs.getActivityType().getName())) {
            		scheduledEventId = event.getEventId();
            	}
                break;
            case "ActivityTaskStarted":
            	ActivityTaskStartedEventAttributes startedAttrs = event.getActivityTaskStartedEventAttributes();
            	if ( startedAttrs.getScheduledEventId().equals(scheduledEventId)) {
            		startedEventId = event.getEventId();
            	}
                break;
            case "ActivityTaskCompleted":
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
