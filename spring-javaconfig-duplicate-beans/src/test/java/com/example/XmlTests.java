package com.example;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Verifies behavior, when using XML config, 
 * if the same bean (ID) is defined in two config files, 
 * the last one imported "wins".
 * 
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(locations={"/config1.xml", "/config2.xml"})
public class XmlTests {

	@Autowired String example;
	
	@Test
	public void contextLoads() {
		Assert.assertEquals("Example 2", example);
	}

}
