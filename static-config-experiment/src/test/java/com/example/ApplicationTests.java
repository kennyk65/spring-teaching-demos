package com.example;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes=Config.class)
public class ApplicationTests {

	@Value("${USER}") String user;
	@Value("${some.test}") String test;
	
	@Test
	public void contextLoads() {
		System.out.println("user is: " + user + ", test is: " + test );
	}

}
