package com.example.wf;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.amazonaws.services.simpleworkflow.AmazonSimpleWorkflowClient;
import com.amazonaws.services.simpleworkflow.model.ActivityTask;
import com.amazonaws.services.simpleworkflow.model.ChildPolicy;
import com.amazonaws.services.simpleworkflow.model.DecisionTask;
import com.amazonaws.services.simpleworkflow.model.DomainAlreadyExistsException;
import com.amazonaws.services.simpleworkflow.model.HistoryEvent;
import com.amazonaws.services.simpleworkflow.model.RegisterActivityTypeRequest;
import com.amazonaws.services.simpleworkflow.model.RegisterDomainRequest;
import com.amazonaws.services.simpleworkflow.model.RegisterWorkflowTypeRequest;
import com.amazonaws.services.simpleworkflow.model.TaskList;
import com.amazonaws.services.simpleworkflow.model.TypeAlreadyExistsException;
import com.amazonaws.services.simpleworkflow.model.WorkflowType;

@Component
public class Utility {

	static Logger logger = LoggerFactory.getLogger(Utility.class);
	
	public static final String DOMAIN_NAME = "Demo";
	public static final String WORKFLOW_NAME = "FoolOnTheHillFlow";
	public static final String WORKFLOW_VERSION = "1.0";
    public static final String ACTIVITY_1 = "step_one";
    public static final String ACTIVITY_2 = "step_two";
    public static final String ACTIVITY_VERSION = "1.0";
	public static final String TWENTY_SECONDS = "20";
	public static final String ONE_DAY = "1";
	public static final String TASKLIST = "TASKLIST";
	
	@Autowired AmazonSimpleWorkflowClient swf;


	public void setup() {
		createDomain();
		registerActivityType1();
		registerActivityType2();
		createWorkFlow();
	}
	

	//	Utility the Domain, ignore if it already exists.	
	public void createDomain(){
		try {
			swf.registerDomain(
				new RegisterDomainRequest()
					.withName(DOMAIN_NAME)
					.withWorkflowExecutionRetentionPeriodInDays(ONE_DAY)					
			);
		} catch (DomainAlreadyExistsException e) {
			logger.info("Domain '" + DOMAIN_NAME + "' already exists...");
		}
	}
	
	
	//	Create the workflow, unless it already exists.
	public void createWorkFlow() {
	    try {
	        swf.registerWorkflowType(new RegisterWorkflowTypeRequest()
	            .withDomain(DOMAIN_NAME)
	            .withName(WORKFLOW_NAME)
	            .withVersion(WORKFLOW_VERSION)
	            .withDefaultChildPolicy(ChildPolicy.TERMINATE)	//	I Shouldn't have to set this, no children.	
	            .withDefaultTaskList(getTaskList())
	            .withDefaultTaskStartToCloseTimeout("30"));
	    }
	    catch (TypeAlreadyExistsException e) {
			logger.info("Workflow '" + WORKFLOW_NAME + "' already exists..."); 
	    }
	}	
	
	
	//	Register the activity type, unless it already exists.
	public void registerActivityType1() {
	    try {
	        swf.registerActivityType(new RegisterActivityTypeRequest()
	            .withDomain(DOMAIN_NAME)
	            .withName(ACTIVITY_1)
	            .withVersion(ACTIVITY_VERSION)
	            .withDefaultTaskList(getTaskList())
	            .withDefaultTaskScheduleToStartTimeout("5")		//	5 seconds to get assigned
	            .withDefaultTaskStartToCloseTimeout("2")		//	Two seconds to process
	            .withDefaultTaskScheduleToCloseTimeout("7")		//	Not sure
	            .withDefaultTaskHeartbeatTimeout("20"));		//	Frequency of heartbeats
	    }
	    catch (TypeAlreadyExistsException e) {
			logger.info("Activity Type '" + ACTIVITY_1 + "' already exists..."); 
	    }
	}	
	
	
	public void registerActivityType2() {
	    try {
	        swf.registerActivityType(new RegisterActivityTypeRequest()
	            .withDomain(DOMAIN_NAME)
	            .withName(ACTIVITY_2)
	            .withVersion(ACTIVITY_VERSION)
	            .withDefaultTaskList(getTaskList())
	            .withDefaultTaskScheduleToStartTimeout("5")
	            .withDefaultTaskStartToCloseTimeout("2")
	            .withDefaultTaskScheduleToCloseTimeout("7")
	            .withDefaultTaskHeartbeatTimeout("20"));
	    }
	    catch (TypeAlreadyExistsException e) {
			logger.info("Activity Type '" + ACTIVITY_2 + "' already exists..."); 
	    }
	}	
	
	
	public static WorkflowType getWorkflowType() {
        return new WorkflowType()
                .withName(Utility.WORKFLOW_NAME)
                .withVersion(Utility.WORKFLOW_VERSION);		
	}
	
	public static TaskList getTaskList() {
		return new TaskList().withName(TASKLIST);
	}
	
	private void logDecisionTask(DecisionTask dt) {
		logger.info("Decision Task, workflow type: " + dt.getWorkflowType() );
		logger.info("Decision Task, started event id: " + dt.getStartedEventId()   );
		logger.info("Decision Task, task token : " + dt.getTaskToken() );
		logger.info("Decision Task, previous started event id : " + dt.getPreviousStartedEventId() );
		logEventHistory(dt.getEvents());
	}
	

	private void logActivityTask(ActivityTask at) {
		logger.info("Activity Task, id: " + at.getActivityId());
		logger.info("Activity Task, type: " + at.getActivityType());
		logger.info("Activity Task, input: " + at.getInput());
		logger.info("Activity Task, started event id: " + at.getStartedEventId());
		logger.info("Activity Task: token: " + at.getTaskToken());
	}

	public static void logEventHistory(List<HistoryEvent> list) {
		for (HistoryEvent he : list){
			Utility.logger.debug(he.toString());
		}
	}
}
