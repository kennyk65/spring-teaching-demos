package com.example.lazy;


import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes=Config.class)
public class LazyBecomesEagerTest {

	@Autowired EagerBean eager;
	
	@Test
	public void test() {
		Assert.assertEquals("Some Test Value", eager.getStaticValue());
	}

}
