package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.amazonaws.services.cognitoidp.AWSCognitoIdentityProvider;
import com.amazonaws.services.cognitoidp.AWSCognitoIdentityProviderClientBuilder;

@SpringBootApplication
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}
	
	@Bean
	public AWSCognitoIdentityProvider identityProvider() {
		//	Make a Cognito Identity Provider client.  This will default to use the ACCESS_KEY and SECRET_ACCESS_KEY 
		//	associated with the local machine, or the SDK will use the instance's role to obtain one from STS.
		return AWSCognitoIdentityProviderClientBuilder.standard().build();
	}

}
