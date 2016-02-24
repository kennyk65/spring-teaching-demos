package com.example;

import org.junit.Before;
import org.junit.Test;
import org.springframework.boot.SpringApplication;

public class ApplicationTests {

	@Before
	public void init() {
		SpringApplication.run(Config.class);
	}
	
	@Test
	public void contextLoads() {
	}

}
