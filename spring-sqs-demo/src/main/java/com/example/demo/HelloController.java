package com.example.demo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.aws.messaging.core.QueueMessagingTemplate;
import org.springframework.cloud.aws.messaging.listener.annotation.SqsListener;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

	@Value("${cloud.aws.queue-endpoint}")
	private String queueUrl;
	
    @Autowired
    private QueueMessagingTemplate template;

    private static final Logger LOG = LoggerFactory.getLogger(HelloController.class);
    
    
    @GetMapping("/")
    public String sendMessage() {
        LOG.info(" ***  Sending Message ");
        template.convertAndSend(
        		queueUrl,
        		MessageBuilder.withPayload("hello from Spring Boot").build());
        return "Hello";
    }

    @SqsListener("https://sqs.us-west-2.amazonaws.com/011673140073/demo-queue")
    public void getMessage(String message) {
      LOG.info(" ***  Receiving Message from SQS Queue - "+message);
    }	
	
	
}
