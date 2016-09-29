package com.example.config;

import org.socialsignin.spring.data.dynamodb.core.DynamoDBOperations;
import org.socialsignin.spring.data.dynamodb.core.DynamoDBTemplate;
import org.socialsignin.spring.data.dynamodb.repository.config.EnableDynamoDBRepositories;
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
@EnableDynamoDBRepositories(basePackages = "com.example")
public class DynamoDbConfig {

    @Value("${amazon.dynamodb.endpoint:N/A}")
    private String amazonDynamoDBEndpoint;

    
    //	Not sure why, but I can't get the AWSCredentialsProvider to inject 
    //	unless I make it lazy...
    @Autowired @Lazy AWSCredentialsProvider credentialsProvider;
    
    
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

    
//    @Bean
//	public DynamoDBOperations dynamoDBTemplate()
//	{
//    	return new DynamoDBTemplate(amazonDynamoDB());
//	}    
}
