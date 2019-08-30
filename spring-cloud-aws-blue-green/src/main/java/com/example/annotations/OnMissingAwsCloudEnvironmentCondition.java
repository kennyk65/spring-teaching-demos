package com.example.annotations;

import org.springframework.cloud.aws.context.annotation.OnAwsCloudEnvironmentCondition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;

/**
 * @author Ken Krueger
 */
public class OnMissingAwsCloudEnvironmentCondition extends OnAwsCloudEnvironmentCondition {

	/**
	 * Return the opposite of whatever OnAwsCloudEnvironmentCondition does:
	 */
	@Override
	public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
		return !super.matches(context, metadata);
	}
	
	//	TODO:  REPLACE AND RETEST WITH @ConditionalOnAwsCloudEnvironment and @ConditionalOnMissingAwsCloudEnvironment

}
