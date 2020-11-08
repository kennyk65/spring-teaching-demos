package com.example.demo;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ApplicationContext;

public class BTest {

	B b;
	
//	@BeforeEach
//	public void setup() {
//		ApplicationContext spring = SpringApplication.run(Application.class);
//		b = spring.getBean(B.class);
//		
//	}
	@Test
	public void test() {
		b = new B(new A());
		b.setFoo("my foo value");
		b.init();
	}
	
}
