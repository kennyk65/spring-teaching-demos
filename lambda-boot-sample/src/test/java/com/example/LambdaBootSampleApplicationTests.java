package com.example;

import org.junit.Test;

import org.mockito.Mockito;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;

//	Do not use the normal Spring Test fixtures for this test.
public class LambdaBootSampleApplicationTests {

	@Test
	public void contextLoads() {

		//	Mock up the AWS Context and Logger objects.  Needed as input.
		Context context = Mockito.mock(Context.class);
		LambdaLogger logger = Mockito.mock(LambdaLogger.class);
		when(context.getLogger()).thenReturn(logger);
		
		ApplicationLambdaEntry lambdaEntry = new ApplicationLambdaEntry();
		
		assertEquals("Invocation #1", lambdaEntry.handleRequest(null, context));
		assertEquals("Invocation #2", lambdaEntry.handleRequest(null, context));
		assertEquals("Invocation #3", lambdaEntry.handleRequest(null, context));
	}

}
