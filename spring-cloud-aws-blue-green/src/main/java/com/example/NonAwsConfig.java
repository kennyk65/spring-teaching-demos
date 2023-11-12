package com.example;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import com.example.util.NotWithinEc2Environment;


/**
 * This configuration class will be active only if NOT running on the AWS environment.
 * Beans defined here allow the application to run locally (including local JUnit tests).
 * 
 */
@Configuration
@NotWithinEc2Environment
@PropertySource("classpath:non-aws.properties")	//	Use these properties only when NOT running on AWS.
public class NonAwsConfig {

}
