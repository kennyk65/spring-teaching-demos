package com.example;

import org.springframework.cloud.aws.messaging.listener.annotation.SqsListener;
import org.springframework.stereotype.Service;

// The SQSConsumer class retrieves messages from an SQS queue.
@Service
public class SQSConsumer {

	@SqsListener("MySQSQueue_A")
	public void queueListener(Order o) {
        System.out.printf("Order received from SQS queue:%s%n", o);
	}	

}
