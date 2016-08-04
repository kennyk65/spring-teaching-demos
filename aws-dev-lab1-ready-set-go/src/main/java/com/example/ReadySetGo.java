package com.example;

import java.util.List;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.Bucket;

@Component
public class ReadySetGo {

	Logger logger = LoggerFactory.getLogger(ReadySetGo.class);
	
	//	The AmazonS3 client is already setup for us by Spring Cloud AWS,
	//	All we have to do is inject and use:
	@Autowired AmazonS3 s3;

	//	Run as soon as Bean is instantiated:
	@PostConstruct
	public void atStartup() {
		
		logger.info("============================================");
        logger.info("Welcome to the AWS Java SDK! Ready, Set, Go!");
        logger.info("============================================");

        // The Amazon S3 client allows you to manage buckets and objects programmatically.
        try {
            List<Bucket> buckets = s3.listBuckets();
            logger.info("You have " + buckets.size() + " Amazon S3 bucket(s)");
        } catch (AmazonServiceException ase) {
            // AmazonServiceException represents an error response from an AWS service.
            // AWS service received the request but either found it invalid or encountered an error trying to execute it.
            logger.info("Error Message:    " + ase.getMessage());
            logger.info("HTTP Status Code: " + ase.getStatusCode());
            logger.info("AWS Error Code:   " + ase.getErrorCode());
            logger.info("Error Type:       " + ase.getErrorType());
            logger.info("Request ID:       " + ase.getRequestId());
        } catch (AmazonClientException ace) {
            // AmazonClientException represents an error that occurred inside the client on the local host,
            // either while trying to send the request to AWS or interpret the response.
            // For example, if no network connection is available, the client won't be able to connect to AWS to execute a request and will throw an AmazonClientException.
            logger.info("Error Message: " + ace.getMessage());
        }
		
	}
}
