package org.springframework.cloud.serverless.aws;

import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.amazonaws.services.lambda.runtime.Context;

@Configuration
@ConditionalOnClass(Context.class)
public class AwsAutoConfiguration {

	@Bean
	public LambdaAdapter lambdaAdapter() {
		return new LambdaAdapter();
	}
}
