package com.example;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes=Config3.class)
public class PropSource3Test {

	@Autowired Config3 config;
	@Autowired Environment env;
	@Value("${USER}") public String user;
	@Value("${some.test}") public String test;
	
	@Test
	public void contextLoads() {
		//	Properties on the Config class weren't 
		//	resolved at the time it was instantiated:
		assertNull(Config3.user);
		assertNull(Config3.test);
		System.out.println("user is: " + Config3.user + ", test is: " + Config3.test );
		//	But the same properties were resolved here just fine:
		assertNotNull(user);
		assertNotNull(test);
		System.out.println("user is: " + user + ", test is: " + test );
		//	The properties themselves are present in the environment:
		assertNotNull(env.getProperty("USER"));
		assertNotNull(env.getProperty("some.test"));
	}
	

}
