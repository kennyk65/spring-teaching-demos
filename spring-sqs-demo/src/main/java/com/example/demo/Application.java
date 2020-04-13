package com.example.demo;

import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.aws.messaging.config.SimpleMessageListenerContainerFactory;
import org.springframework.cloud.aws.messaging.core.QueueMessagingTemplate;
import org.springframework.context.annotation.Bean;

import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSAsync;

@SpringBootApplication
//@ConditionalOnMissingAwsCloudEnvironment
public class Application {

    private static final Logger LOG = LoggerFactory.getLogger(Application.class);
	
	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	@Value("${cloud.aws.queue-endpoint}")
	private String queueUrl;
	
	@Bean
    public QueueMessagingTemplate queueMessagingTemplate(AmazonSQSAsync client) {
        return new QueueMessagingTemplate(client);
    }

	@Bean
	public String safetyCheck(AmazonSQS client) {
		//	The purpose of this bean / logic is to detect issues 
		//	with connecting to SQS and fail-fast at application startup 
		//	time with a meaningful message.
		try {
			client.getQueueAttributes(queueUrl,new ArrayList<String>());
		} catch (Exception e) {
			LOG.error("Unable to connect to SQS");
			LOG.error(e.getMessage());
			throw new RuntimeException(e);
		}
		return "";
	}

    @Bean
    public SimpleMessageListenerContainerFactory simpleListenerFactory(AmazonSQSAsync amazonSQS){
        SimpleMessageListenerContainerFactory factory 
        	= new SimpleMessageListenerContainerFactory();
        factory.setAmazonSqs(amazonSQS);
        factory.setMaxNumberOfMessages(10);
        return factory;	
    }
	
}
