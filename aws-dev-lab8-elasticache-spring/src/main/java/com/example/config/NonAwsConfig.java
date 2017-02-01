package com.example.config;

import java.util.HashSet;
import java.util.Set;

import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCache;
import org.springframework.cache.support.SimpleCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import com.example.annotations.ConditionalOnMissingAwsCloudEnvironment;

/**
 * This configuration class will be active only if NOT running on the AWS environment.
 * Beans defined here allow the application to run locally (including local JUnit tests).
 * 
 */
@Configuration
@ConditionalOnMissingAwsCloudEnvironment		//	This config only applies when NOT running on AWS.
@PropertySource("classpath:non-aws.properties")	//	Use these properties only when NOT running on AWS.
@EnableCaching
public class NonAwsConfig {
	
	//	WARNING - AWS ElastiCache cannot be connected to externally.
	//	So for local testing we just use a local HashMap.
	@Bean
	public CacheManager cacheManager() {
		SimpleCacheManager cacheManager = new SimpleCacheManager();
		Set<Cache> caches = new HashSet<Cache>();
		caches.add(new ConcurrentMapCache("springexample"));
		cacheManager.setCaches(caches);
		return cacheManager;
	}	

}
