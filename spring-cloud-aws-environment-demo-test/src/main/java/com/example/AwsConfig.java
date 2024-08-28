package com.example;

import java.util.Optional;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.core.env.Environment;

import io.awspring.cloud.autoconfigure.imds.ImdsPropertySource;

/**
 * This configuration class will be active only if running on the AWS environment.
 * Separating it from the main configuration allows your app to run on AWS AND off AWS (locally).
 */
@Configuration
public class AwsConfig {


	//	Convenience Bean for carrying instanceMetadata to the page.
	//	Ensure instance metadata is initialized before using it.
	@Bean(initMethod="init")
	public InstanceMetadata instanceMetadata(Environment env, Optional<ImdsPropertySource> props) {
		return new InstanceMetadata(env);
	}
}
