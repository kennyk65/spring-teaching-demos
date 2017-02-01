package com.example;

import static org.junit.Assert.assertEquals;

import org.junit.FixMethodOrder;
import org.junit.Rule;
import org.junit.Test;
import org.junit.contrib.java.lang.system.EnvironmentVariables;
import org.junit.runners.MethodSorters;
import org.mockito.Mockito;
import static org.mockito.Mockito.*;
import org.springframework.cloud.serverless.aws.ApplicationLambdaEntry;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;

//	Do not use the normal Spring Test fixtures for this test.
@FixMethodOrder(MethodSorters.NAME_ASCENDING)	// These test methods will need to run in a specific order.
public class LambdaBootSampleApplicationTests {

	private int testInvocationNumber = 1;
	
	//	Used to set environment variables for test purposes:
	@Rule
	public final EnvironmentVariables environmentVariables = new EnvironmentVariables();

	@Test
	public void contextLoads1UsingSystemProperty() {
		System.setProperty("spring.config.class", "com.example.Config");
		basicTest();
	}

	@Test
	public void contextLoads2UsingEnvironmentVariable() {
		environmentVariables.set("SPRING_CONFIG_CLASS", "com.example.Config");
		testInvocationNumber = 4; // I shouldn't have to do this.
		basicTest();
		environmentVariables.set("SPRING_CONFIG_CLASS", null);
	}

	private void basicTest( ) {	
		//	Mock up the AWS Context and Logger objects.  Needed as input.
		Context context = Mockito.mock(Context.class);
		LambdaLogger logger = Mockito.mock(LambdaLogger.class);
		when(context.getLogger()).thenReturn(logger);
		
		ApplicationLambdaEntry lambdaEntry = new ApplicationLambdaEntry();
		
		assertEquals("Invocation #" + testInvocationNumber++, lambdaEntry.handleRequest(null, context));
		assertEquals("Invocation #" + testInvocationNumber++, lambdaEntry.handleRequest(null, context));
		assertEquals("Invocation #" + testInvocationNumber++, lambdaEntry.handleRequest(null, context));
	}

}
