package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

//import com.amazonaws.services.cognitoidp.AWSCognitoIdentityProvider;
//import com.amazonaws.services.cognitoidp.AWSCognitoIdentityProviderClientBuilder;

import software.amazon.awssdk.services.cognitoidentity.CognitoIdentityClient;
import software.amazon.awssdk.services.cognitoidentity.CognitoIdentityClientBuilder;
import software.amazon.awssdk.services.cognitoidentity.model.CognitoIdentityProvider;
import software.amazon.awssdk.services.cognitoidentityprovider.CognitoIdentityProviderClient;

@SpringBootApplication
@EnableConfigurationProperties(CognitoProperties.class)
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}
	
	
	/*
	 * A CognitoIdentityProviderClient is a UserPool client.
	 */	
	@Bean
	public CognitoIdentityProviderClient cognitoUserPool(CognitoProperties cognito) {
		System.out.println( cognito.toString() );
		return CognitoIdentityProviderClient.builder().build();
	}
}
