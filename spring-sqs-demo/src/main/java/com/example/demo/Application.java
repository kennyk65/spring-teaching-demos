package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.aws.messaging.config.SimpleMessageListenerContainerFactory;
import org.springframework.cloud.aws.messaging.core.QueueMessagingTemplate;
import org.springframework.context.annotation.Bean;

import com.amazonaws.services.sqs.AmazonSQSAsync;
import com.amazonaws.services.sqs.AmazonSQSAsyncClientBuilder;

@SpringBootApplication
//@ConditionalOnMissingAwsCloudEnvironment
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	@Bean
    public QueueMessagingTemplate queueMessagingTemplate(AmazonSQSAsync client) {
        return new QueueMessagingTemplate(client);
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
