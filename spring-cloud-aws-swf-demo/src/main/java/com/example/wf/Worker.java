package com.example.wf;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import javax.annotation.PreDestroy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.amazonaws.services.simpleworkflow.AmazonSimpleWorkflowClient;
import com.amazonaws.services.simpleworkflow.model.ActivityTask;
import com.amazonaws.services.simpleworkflow.model.ActivityType;
import com.amazonaws.services.simpleworkflow.model.PollForActivityTaskRequest;
import com.amazonaws.services.simpleworkflow.model.RespondActivityTaskCompletedRequest;

public abstract class Worker {

	Logger logger = LoggerFactory.getLogger(Worker.class);

	@Autowired AmazonSimpleWorkflowClient swf;
    private CountDownLatch waitForTermination = new CountDownLatch(1);
	private boolean shutdown = false;
	
	
	protected abstract String doTheWork(String input);
	
	protected abstract ActivityType getActivityType();
	
	/**
	 * Poll the SWF service for work tasks.  Do this in a dedicated thread.
	 */
	@Async
	public void pollForWork () {
	    while (!shutdown) {
	    	logger.info("Polling for an activity task from the tasklist '"
	                + Utility.TASKLIST + "' in the domain '" + Utility.DOMAIN_NAME + "'.");

	        ActivityTask task = swf.pollForActivityTask(
	            new PollForActivityTaskRequest()
	                .withDomain(Utility.DOMAIN_NAME)
	                .withTaskList(Utility.getTaskList()));

	        if ( !getActivityType().equals(task.getActivityType()))  {
	        	//	Skip.  This is not our activity:
	        	continue;
	        }
	        
	        String token = task.getTaskToken();
	        if (token != null) {
	        	
                logger.info("Executing the activity task with input '" + task.getInput() + "'.");
                String result = doTheWork(task.getInput());
                logger.info("The activity task succeeded, result: " + result );
                
                swf.respondActivityTaskCompleted(
                    new RespondActivityTaskCompletedRequest()
                        .withTaskToken(token)
                        .withResult(result));
	            }

	        	//	IF FAILURE:
//	        System.out.println("The activity task failed with the error '"
//	                        + error.getClass().getSimpleName() + "'.");
//	                swf.respondActivityTaskFailed(
//	                    new RespondActivityTaskFailedRequest()
//	                        .withTaskToken(task_token)
//	                        .withReason(error.getClass().getSimpleName())
//	                        .withDetails(error.getMessage()));
//	            }
	    }
	    waitForTermination.countDown();
	}
	
	
	//	If we get the shutdown signal, stop looping:
	@PreDestroy
	private void shutdown() throws Exception {
		shutdown = true;
		logger.info("Waiting for the current poll request to return before shutting down.");
		waitForTermination.await(60, TimeUnit.SECONDS);		
	}
	
}
