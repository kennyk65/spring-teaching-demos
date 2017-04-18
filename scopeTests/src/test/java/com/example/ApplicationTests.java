package com.example;

import static org.junit.Assert.assertNotEquals;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ApplicationTests {

	@Autowired MySingleton single;
	@Autowired MySingleton2 single2;
	
	
	@Test
	public void contextLoads() {
	}
	
	@Test
	public void testScopeProxy() {
		System.out.println("Using proxy:  ");
		double val = 0;
		for(int i=0; i<5; i++) {
			//	The result: Every time we call the singleton, and it
			//	calls the prototype, we get a separate instance of 
			//	the prototype:
			double val2 = single.getValue();
			assertNotEquals(val, val2);
			val = val2;
		}
	}
	
	@Test
	public void testProvider() {
		System.out.println("Using provider:  ");
		double val = 0;
		for(int i=0; i<5; i++) {
			//	The result: Every time we call the singleton, and it
			//	calls the Provider, we get a separate instance of 
			//	the prototype:
			double val2 = single.getValue2();
			assertNotEquals(val, val2);
			val = val2;
		}
	}
	
	
	@Test
	public void testObjectFactory() {
		System.out.println("Using ObjectFactory:  ");
		double val = 0;
		for(int i=0; i<5; i++) {
			//	The result: Every time we call the singleton, and it
			//	references the ObjectFactory, we get a separate instance of 
			//	the prototype:
			double val2 = single.getValue3();
			assertNotEquals(val, val2);
			val = val2;
		}
	}
	
	@Test
	public void testMethodInjection() {
		System.out.println("Using Method Injection:  ");
		double val = 0;
		for(int i=0; i<5; i++) {
			//	The result: Every time we call the singleton, it
			//	obtains a reference to the prototype via the 
			//	@Lookup method:
			double val2 = single2.getValue();
			assertNotEquals(val, val2);
			val = val2;
		}
	}
	

}
