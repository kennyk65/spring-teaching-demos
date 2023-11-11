package com.example;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.Environment;
import org.springframework.core.env.MutablePropertySources;

import com.example.util.Ec2InstanceMetadataPropertySource;
import com.example.util.WithinEc2Environment;

import software.amazon.awssdk.imds.Ec2MetadataClient;

/**
 * This configuration class will be active only if running on the AWS environment.
 * Separating it from the main configuration allows your app to run on AWS AND off AWS (locally).
 * 
 */
@Configuration
@WithinEc2Environment
//@ConditionalOnAwsCloudEnvironment    //	This config only applies when running on AWS.
//	TODO:  REPLACE AND RETEST WITH @ConditionalOnAwsCloudEnvironment and @ConditionalOnMissingAwsCloudEnvironment
//@EnableContextInstanceData	//	Get instance metadata, when running on AWS.  Automatic for Boot apps.
//@EnableStackConfiguration(stackName = "awseb-e-ce8qvczr8a-stack")  // trying to get around errors on Elastic Beanstalk
public class AwsConfig {

	@Bean
	public Ec2InstanceMetadataPropertySource ec2InstanceMetadataPropertySource(ConfigurableEnvironment env) {
		Ec2InstanceMetadataPropertySource propertySource = new Ec2InstanceMetadataPropertySource("Ec2InstanceMetadata", Ec2MetadataClient.create());
		propertySource.init();
		env.getPropertySources().addFirst(propertySource);
		return propertySource;
	}

	//	Convenience Bean for carrying instanceMetadata to the page.
	@Bean(initMethod="init")
	@DependsOn("ec2InstanceMetadataPropertySource")
	public InstanceMetadata instanceMetadata(Environment env) {
		return new InstanceMetadata(env);
	}
}
