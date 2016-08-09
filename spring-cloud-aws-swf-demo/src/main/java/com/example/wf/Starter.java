package com.example.wf;

import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.amazonaws.services.simpleworkflow.AmazonSimpleWorkflowClient;
import com.amazonaws.services.simpleworkflow.model.ChildPolicy;
import com.amazonaws.services.simpleworkflow.model.Run;
import com.amazonaws.services.simpleworkflow.model.StartWorkflowExecutionRequest;

/**
 * This bean starts the Workflow when the start() method is called.
 */
@Component
public class Starter {
	
	Logger logger = LoggerFactory.getLogger(Starter.class);

	@Autowired AmazonSimpleWorkflowClient swf;
	
   // public static final String WORKFLOW_EXECUTION = "HelloWorldWorkflowExecution";

    public Run start() {
        String workflow_input = "Day after day";

        String workflowExecution = UUID.randomUUID().toString();
        
        logger.info("Starting the workflow execution '" + workflowExecution + "' with input '" + workflow_input + "'.");

        Run run = swf.startWorkflowExecution(new StartWorkflowExecutionRequest()
            .withDomain(Utility.DOMAIN_NAME)
            .withWorkflowType(Utility.getWorkflowType())
            .withWorkflowId(workflowExecution)
            .withTaskList(Utility.getTaskList())
            .withInput(workflow_input)
            .withTaskStartToCloseTimeout("10")	//	I shouldn't have to set this, default is set.
            .withChildPolicy(ChildPolicy.TERMINATE)	//	I Shouldn't have to set this, no children.	
            .withExecutionStartToCloseTimeout("90"));

        logger.info("Workflow execution started with the run id '" + run.getRunId() + "'.");
        return run;
    }

}
