package com.example;

import io.awspring.cloud.context.annotation.ConditionalOnMissingAwsCloudEnvironment;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;


/**
 * This configuration class will be active only if NOT running on the AWS environment.
 * Beans defined here allow the application to run locally (including local JUnit tests).
 * 
 */
@Configuration
@ConditionalOnMissingAwsCloudEnvironment        //	This config only applies when NOT running on AWS.
@PropertySource("classpath:non-aws.properties")	//	Use these properties only when NOT running on AWS.
public class NonAwsConfig {

}
