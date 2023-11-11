package com.example.util;

import org.springframework.context.annotation.ConditionContext;
import org.springframework.context.annotation.ConfigurationCondition;
import org.springframework.core.type.AnnotatedTypeMetadata;

/**
 * Does the opposite as the WithinEc2EnvironmentCondition.
 * 
 * @author Ken Krueger
 */
public class NotWithinEc2EnvironmentCondition extends WithinEc2EnvironmentCondition {

    @Override
    public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
        return !super.matches(context,metadata);
    }

}