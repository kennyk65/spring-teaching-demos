package com.example.demo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import software.amazon.awssdk.services.cognitoidentityprovider.CognitoIdentityProviderClient;
import software.amazon.awssdk.services.cognitoidentityprovider.model.AdminInitiateAuthRequest;
import software.amazon.awssdk.services.cognitoidentityprovider.model.AdminInitiateAuthResponse;
import software.amazon.awssdk.services.cognitoidentityprovider.model.AuthFlowType;
import software.amazon.awssdk.services.cognitoidentityprovider.model.UserNotFoundException;


/**
 * A Spring Security AuthentictionProvider based on an AWS Cognito User Pool.
 * 
 * Use of this AuthenticationProvider requires an AWS Account, Access Key, Secret Key.
 * The AK / SK is obtained by the local ~/.aws/credentials file when running outside of AWS, 
 * or obtained automatically by IAM Role when running on AWS.  Selected region is obtained
 * from the ~/.aws/config file when running outside of AWS, or obtained automatically via 
 * instance metadata when running on AWS.
 * 
 * A Cognito user pool must be established.  This will hold all users / credentials.  The pool
 * is identified to this code via PoolID.  Additionally, the pool must have a "client app" defined
 * for it which represents this application.  The ClientID and Client Secret identify this "app". 
 * These values should not be confused with the general IAM Access Key / Secret Key.
 */
@Component 
public class CognitoAuthenticationProvider implements AuthenticationProvider {

	@Autowired CognitoProperties cognito;
	
	@Autowired CognitoIdentityProviderClient cognitoClient;

	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {

		//	This is the username and password 
		//	obtained from Spring's authentication manager 
		//	(ultimately the login page):
		String userId = authentication.getName();
		String password = authentication.getCredentials().toString();

		//	This secret hash is required when making certain Cognito API calls,
		//	typically when the UserPool's client specifies a secret:
		String secretHash = 
			SigningUtility.calculateSecretHash(
				cognito.getClientId(),
				userId,
				cognito.getClientSecret());
		

		Map<String, String> params = new HashMap<>();
		params.put("USERNAME", userId);
		params.put("SECRET_HASH", secretHash);
		params.put("PASSWORD", password);

		
		//	Make the login request to Cognito.  AWS documentation says this 
		//	requires "admin credentials", but this is incorrect; one only needs cognito permissions:
		AdminInitiateAuthResponse response = null;
		try {
			response = 
				cognitoClient.adminInitiateAuth(
					AdminInitiateAuthRequest.builder()
						.userPoolId(cognito.getPoolId())
						.clientId(cognito.getClientId())
						.authFlow(AuthFlowType.ADMIN_NO_SRP_AUTH) 	// This flow is essentially "login with username and password"
						.authParameters(params)
						.build()
				);
		} catch(UserNotFoundException notfound) {
			//	If we get a user not found error from AWS Cognito, translate this into a Spring Security user not found error.
			//	this will allow the framework to handle the situation gently (take us back to the login page) 
			//	rather than an unknown general error:
			throw new UsernameNotFoundException(notfound.getMessage(),notfound);
		}
		
		
		//	Let's ignore new password stuff for now:
		if(response.challengeName().equals("NEW_PASSWORD_REQUIRED")) {
			// ignore
		}

		//	This token will be stored by Spring Security 
		//	to represent this authenticated entity going forward:
		return new UsernamePasswordAuthenticationToken(
    		userId, password, new ArrayList<>());
	}

	@Override
	public boolean supports(Class<?> authentication) {
		return authentication.equals(UsernamePasswordAuthenticationToken.class);
	}

}