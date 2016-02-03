/*
 * Copyright 2002-2012 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.demo.ex;

import static junit.framework.Assert.assertNotNull;

import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.integration.core.MessagingTemplate;
import org.springframework.messaging.MessageChannel;

import com.demo.ex.service.SimpleGateway;

/**
 * Question:
 * Which error channel gets the exception when there are several?
 * i.e. The gateway specifies an error channel,
 * then something downstream specifies a different error channel,
 * then something downstream specifies a different error channel,
 * and then there is an error.
 * Does the outermost specification apply, or the 'innermost'?
 * 
 * Answer:
 * The innermost / latest / closest error channel.
 * 
 * @author kenk
 * @since 1.0
 * 
 */
public class AsyncErrorHandlingLayerTest {


	@Test
	public void testConvertStringToUpperCase() {
		final ApplicationContext context
			= new ClassPathXmlApplicationContext("/si-async-layers.xml",
												AsyncErrorHandlingTest.class);

		final SimpleGateway service = context.getBean(SimpleGateway.class);

		service.divideByZero(100); // The exception will happen in another thread.

		MessagingTemplate mt = new MessagingTemplate(context.getBean("stage3Error",MessageChannel.class));
		mt.setReceiveTimeout(1000);
		Exception e = (Exception)mt.receiveAndConvert(Exception.class);
		assertNotNull(e);
		System.out.println(e);
	}

}
