package com.example;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {Config2.class,Config1.class})
public class DoesOrderMatterTests {

	@Value("${someProperty}") String value;
	
	@Test
	public void contextLoads() {
		System.out.println("The value is: " + value);
		Assert.assertTrue(value.equals("fromFile1"));
	}

}
