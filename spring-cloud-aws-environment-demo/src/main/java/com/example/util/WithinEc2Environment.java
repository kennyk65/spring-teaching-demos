package com.example.util;


import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.context.annotation.Conditional;

/**
 * {@link org.springframework.context.annotation.Conditional} annotation that returns true
 * if the current JVM is started within an EC2 instance, more specifically it returns true
 * if EC2 Instance Metadata is available.  This is true when running within EC2, ElasticBeanstalk, 
 * ECS, EKS, etc.  Useful to restrict creation of beans which would only be applicable if the 
 * application context is bootstrapped inside an EC2 instance (e.g. beans that fetch meta data 
 * or beans that can only connect to AWS internal services like Elasticache)
 *
 * @author Ken Krueger
 */
@Target({ ElementType.TYPE, ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Conditional(WithinEc2EnvironmentCondition.class)
public @interface WithinEc2Environment {

}
