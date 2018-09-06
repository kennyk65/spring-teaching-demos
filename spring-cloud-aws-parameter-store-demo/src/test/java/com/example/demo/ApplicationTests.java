package com.example.demo;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.env.CompositePropertySource;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.EnumerablePropertySource;
import org.springframework.core.env.PropertySource;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ApplicationTests {

	@Autowired ConfigurableEnvironment  env;
	
	@Test
	public void contextLoads() {
		
		 CompositePropertySource awsParamSources = null;
		 
		 OUTER: for (PropertySource<?> ps : env.getPropertySources()) {
			if (ps.getName().equals("bootstrapProperties")) {
				CompositePropertySource bootstrap = (CompositePropertySource) ps;
				for (PropertySource<?> nestedSource : bootstrap.getPropertySources()) {
					if (nestedSource.getName().equals("aws-param-store")) {
						awsParamSources = (CompositePropertySource) nestedSource;
						break OUTER;
	                }
	            }
			}
	    }
	    if (awsParamSources == null) {
	        System.out.println("No AWS Parameter Store PropertySource found");
	    } else {
	        System.out.println("Overview of all AWS Param Store property sources:\n");
	        for (PropertySource<?> nestedSource : awsParamSources.getPropertySources()) {
	            EnumerablePropertySource eps = (EnumerablePropertySource) nestedSource;
	            System.out.println(eps.getName() + ":");
	            for (String propName : eps.getPropertyNames()) {
	                System.out.println("\t" + propName + " = " + eps.getProperty(propName) + ", resolved to: " + env.getProperty(propName));
	            }
	
	        }
	    }
	}
}
