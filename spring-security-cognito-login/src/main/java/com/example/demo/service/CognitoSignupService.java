package com.example.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.CognitoProperties;
import com.example.demo.SigningUtility;
import com.example.demo.controllers.SignupModel;

import software.amazon.awssdk.services.cognitoidentityprovider.CognitoIdentityProviderClient;
import software.amazon.awssdk.services.cognitoidentityprovider.model.AttributeType;
import software.amazon.awssdk.services.cognitoidentityprovider.model.SignUpRequest;

@Service
public class CognitoSignupService {

	@Autowired
	CognitoProperties cognito;

	@Autowired CognitoIdentityProviderClient cognitoClient;
	
	public void signUp(SignupModel model) {

		String secretHash = 
			SigningUtility.calculateSecretHash(
				cognito.getPoolId(), 
				model.getUsername(),
				cognito.getClientSecret());
		
		
		cognitoClient.signUp(
			SignUpRequest.builder()
			.clientId(cognito.getClientId())
			.username(model.getUsername())
			.password(model.getPassword())
			.secretHash(secretHash)
			.userAttributes(
				AttributeType.builder().name("phone_number").value(model.getPhoneClean()).build(),
				AttributeType.builder().name("email").value(model.getEmail()).build()
			)
			.build()
		);
	}

}
