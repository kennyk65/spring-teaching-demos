package com.example;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes=Config2.class)
public class PropSourceWithoutPspcTest {

	@Autowired Environment env;
	
	@Test
	public void contextLoads() {
		System.out.println("test is: " + env.getProperty("some.test") );
	}

}
