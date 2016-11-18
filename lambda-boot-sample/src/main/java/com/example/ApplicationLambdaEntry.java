package com.example;

import org.springframework.context.ApplicationContext;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestHandler;

/**
 * Entry point of the application when running from AWS Lambda.
 */
public class ApplicationLambdaEntry implements RequestHandler <Object, Object> { 

	
    public Object handleRequest(Object o, Context context) {
    	
    	LambdaLogger logger = context.getLogger();
    	logger.log("Start of Lambda Function" );

    	//	Get a reference to the Spring ApplicationMainEntry Context:
    	ApplicationContext spring = SpringLauncherSingleton.getInstance();
    			
    	//	Get the bean / entry point to your application:
    	Sample sample = spring.getBean(Sample.class);
    	
    	//	Call whatever logic you want to call.  
    	//	The incoming JSON and Context 
    	//	will probably be relevant 
    	//	for whatever you want to do:
    	Object result = sample.handle(o,context);
    	
    	logger.log(
    		"End of Lambda Function, " + 
    		context.getRemainingTimeInMillis() + 
    		" millisecond of time remaining.\n" + 
    		Runtime.getRuntime().freeMemory() + 
    		" memory free\n");
    	return result;
    }

    
    
}
