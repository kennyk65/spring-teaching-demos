package com.example;

import org.springframework.cloud.aws.context.annotation.ConditionalOnAwsCloudEnvironment;
import org.springframework.context.annotation.Configuration;

/**
 * This configuration class will be active only if running on the AWS environment.
 * Separating it from the main configuration allows your app to run on AWS AND off AWS (locally).
 * 
 */
@Configuration
@ConditionalOnAwsCloudEnvironment	//	This config only applies when running on AWS.
public class AwsConfig {
	//	TODO:  REPLACE AND RETEST WITH @ConditionalOnAwsCloudEnvironment and @ConditionalOnMissingAwsCloudEnvironment

}
