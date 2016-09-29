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
@ContextConfiguration(classes=Config.class)
public class ApplicationTests {

	@Autowired Config config;
	@Autowired Environment env;
	@Value("${USER}") public String user;
	@Value("${some.test}") public String test;
	
	@Test
	public void contextLoads() {
		//	Properties on the Config class weren't 
		//	resolved at the time it was instantiated:
		assertNull(config.user);
		assertNull(config.test);
		System.out.println("user is: " + config.user + ", test is: " + config.test );
		//	But the same properties were resolved here just fine:
		assertNotNull(user);
		assertNotNull(test);
		System.out.println("user is: " + user + ", test is: " + test );
		//	The properties themselves are present in the environment:
		assertNotNull(env.getProperty("USER"));
		assertNotNull(env.getProperty("some.test"));
	}
	

}
