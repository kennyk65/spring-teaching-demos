package com.example;

import org.springframework.cloud.aws.context.annotation.ConditionalOnAwsCloudEnvironment;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

/**
 * This configuration class will be active only if running on the AWS environment.
 * Separating it from the main configuration allows your app to run on AWS AND off AWS (locally).
 * 
 */
@Configuration
@ConditionalOnAwsCloudEnvironment	//	This config only applies when running on AWS.
//	TODO:  REPLACE AND RETEST WITH @ConditionalOnAwsCloudEnvironment and @ConditionalOnMissingAwsCloudEnvironment
//@EnableContextInstanceData	//	Get instance metadata, when running on AWS.  Automatic for Boot apps.
//@EnableStackConfiguration(stackName = "awseb-e-ce8qvczr8a-stack")  // trying to get around errors on Elastic Beanstalk
public class AwsConfig {

	//	Convenience Bean for carrying instanceMetadata to the page.
	@Bean(initMethod="init")
	public InstanceMetadata instanceMetadata(Environment env) {
		return new InstanceMetadata(env);
	}
}
