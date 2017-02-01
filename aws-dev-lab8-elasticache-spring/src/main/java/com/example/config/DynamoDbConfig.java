package com.example.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;

import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;


@Configuration
public class DynamoDbConfig {

    @Value("${amazon.dynamodb.endpoint:N/A}")
    private String amazonDynamoDBEndpoint;
    
    @Autowired AWSCredentialsProvider credentialsProvider;
    
    
    /**
     *	DynamoDB Client: 
     */
    @Bean
    public AmazonDynamoDB amazonDynamoDB() {
        AmazonDynamoDB amazonDynamoDB = new AmazonDynamoDBClient(credentialsProvider);
        if (!"N/A".equals(amazonDynamoDBEndpoint)) {
            amazonDynamoDB.setEndpoint(amazonDynamoDBEndpoint);
        }
        return amazonDynamoDB;
    }

    /**
     * DynamoDB Document Client:
     */
    @Bean
    public DynamoDB dynamoDB() {
    	return new DynamoDB(amazonDynamoDB());
    }

    
}
