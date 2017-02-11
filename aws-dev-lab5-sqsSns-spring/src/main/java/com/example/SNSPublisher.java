package com.example;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.aws.messaging.core.NotificationMessagingTemplate;
import org.springframework.stereotype.Service;

import com.amazonaws.services.sns.AmazonSNS;
import com.fasterxml.jackson.core.JsonProcessingException;

// The SNSPublisher class publishes messages to SNS topics.
@Service
public class SNSPublisher {

    @Value("${topic.arn.email}") private String TOPIC_ARN_EMAIL;
    @Value("${topic.arn.order}") private String TOPIC_ARN_ORDER;
    
    @Value("${email.subject}") private String EMAIL_SUBJECT;
    @Value("${email.message}") private String EMAIL_MESSAGE;
    @Value("${order.details}") private String ORDER_DETAILS; 
    @Value("${num.messages}") public int NUM_MESSAGES;

	@Autowired AmazonSNS sns;
	NotificationMessagingTemplate template;
	
	@PostConstruct
	public void init() throws Exception {
		template = new NotificationMessagingTemplate(sns);
        publishEmailMessage();
        publishOrderMessages();
    }
   
    private void publishEmailMessage() {
    	// STUDENT TODO: Publish a message to the SNS topic for email messages. Use the EMAIL_MESSAGE and EMAIL_SUBJECT constants as email content.
        template.sendNotification( TOPIC_ARN_EMAIL, EMAIL_MESSAGE, EMAIL_SUBJECT);   // @Del  
    }

    
    private void publishOrderMessages() throws JsonProcessingException {
        Order order = null;
        for (int i = 1; i < (NUM_MESSAGES + 1); i++) {
            order = new Order(i,"2015/10/" + i, ORDER_DETAILS);
            System.out.println("Publishing order to SNS topic: " + order);

        	//	STUDENT TODO: Rather than fumble with JSON ourselves, 
            //	let the framework do it with built-in message converters:
            template.convertAndSend(TOPIC_ARN_ORDER, order);
        }
    }

}
