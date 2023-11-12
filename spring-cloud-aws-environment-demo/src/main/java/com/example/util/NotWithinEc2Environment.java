package com.example.util;


import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.context.annotation.Conditional;

/**
 * {@link org.springframework.context.annotation.Conditional} annotation that returns true
 * if the current JVM is NOT started within an EC2 instance, more specifically it returns true
 * if EC2 Instance Metadata is NOT available.  This is true when NOT running within EC2, ElasticBeanstalk, 
 * ECS, EKS, etc., but is also true if the EC2 instance metadata service is simply switched off.
 * Useful to restrict creation of beans which would only be applicable if the application context is 
 * NOT bootstrapped inside an EC2 instance, such as running in a local environment or another cloud.
 *
 * @author Ken Krueger
 */
@Target({ ElementType.TYPE, ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Conditional(NotWithinEc2EnvironmentCondition.class)
public @interface NotWithinEc2Environment {

}
