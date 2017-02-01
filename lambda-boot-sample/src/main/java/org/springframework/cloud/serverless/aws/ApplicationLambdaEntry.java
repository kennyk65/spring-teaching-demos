package org.springframework.cloud.serverless.aws;

import java.util.Map;

import org.springframework.cloud.serverless.Handler;
import org.springframework.cloud.serverless.ServerlessException;
import org.springframework.cloud.serverless.SpringLauncherSingleton;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

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
    	Map<String,Object> map = spring.getBeansWithAnnotation(Handler.class);
    	
    	if (map == null || map.size() < 1 ) {
    		throw new ServerlessException("No @Handler bean was found in the application context.");
    	} else if ( map.size() > 1 ) {
    		throw new ServerlessException("Only one @Handler bean is permitted in the application context, found " + map.size() + ".");
    	}
    	
    	//	Get the one and only one @Handler bean:
    	Object handlerBean = map.values().iterator().next();	
    	
    	//	Get the AWS Lambda Adapter:
    	LambdaAdapter adapter = spring.getBean(LambdaAdapter.class);
    	
    	
    	//	Call the @CloudFunction on the @Handler:
    	Object result = adapter.handle(handlerBean,o,context);
    	
    	logger.log(
    		"End of Lambda Function, " + 
    		context.getRemainingTimeInMillis() + 
    		" millisecond of time remaining.\n" + 
    		Runtime.getRuntime().freeMemory() + 
    		" memory free\n");
    	return result;
    }

    
    
}
