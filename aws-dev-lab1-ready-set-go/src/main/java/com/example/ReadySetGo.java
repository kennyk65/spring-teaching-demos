package com.example;

import java.util.List;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Component;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.Bucket;

@SpringBootApplication
public class ReadySetGo {

	Logger logger = LoggerFactory.getLogger(ReadySetGo.class);

	//	The AmazonS3 client is already setup for us by Spring Cloud AWS,
	//	All we have to do is inject and use:
	@Autowired AmazonS3 s3;

	
	public static void main(String[] args) {
		SpringApplication.run(ReadySetGo.class, args);
	}
	
	
	//	Run as soon as Bean is instantiated:
	@PostConstruct
	public void atStartup() {
		
		logger.info("============================================");
        logger.info("Welcome to the AWS Java SDK! Ready, Set, Go!");
        logger.info("============================================");

        // The Amazon S3 client allows you to manage buckets and objects programmatically.
        List<Bucket> buckets = s3.listBuckets();
        logger.info("You have " + buckets.size() + " Amazon S3 bucket(s)");

        //	Exception allowed to propagate up the stack.
	}
}
