package com.example.demo;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.validation.constraints.NotNull;

import org.apache.commons.codec.digest.HmacAlgorithms;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

import com.amazonaws.services.cognitoidp.AWSCognitoIdentityProvider;
import com.amazonaws.services.cognitoidp.AWSCognitoIdentityProviderClientBuilder;
import com.amazonaws.services.cognitoidp.model.AdminInitiateAuthRequest;
import com.amazonaws.services.cognitoidp.model.AdminInitiateAuthResult;
import com.amazonaws.services.cognitoidp.model.AuthFlowType;

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
 * 
 * 
 */
@Component("cognitoProvider")
public class CognitoAuthenticationProvider implements AuthenticationProvider {

	@Value("${COGNITO.POOL.ID}")
	private String poolId;
	
	@Value("${COGNITO.CLIENT.ID}")
	private String clientId;
	
	@Value("${COGNITO.CLIENT.SECRET}")
	private String clientSecret;


	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {

		//	This is the username and password 
		//	obtained from Spring's authentication manager 
		//	(ultimately the login page):
		String userId = authentication.getName();
		String password = authentication.getCredentials().toString();

		Map<String, String> params = new HashMap<>();
		params.put("USERNAME", userId);
		params.put("SECRET_HASH", calculateSecretHash(userId));
		params.put("PASSWORD", password);

		//	Make a Cognito Identity Provider client.  This will default to use the ACCESS_KEY and SECRET_ACCESS_KEY 
		//	associated with the local machine, or the SDK will use the instance's role to obtain one from STS.
		AWSCognitoIdentityProvider identityProvider = 
				AWSCognitoIdentityProviderClientBuilder.standard().build();
			
		//	Make the login request to Cognito:
		AdminInitiateAuthResult result = 
			identityProvider.adminInitiateAuth(
				new AdminInitiateAuthRequest()
					.withUserPoolId(poolId)
					.withClientId(clientId)
					.withAuthFlow(AuthFlowType.ADMIN_NO_SRP_AUTH)	// This flow is essentially "login with username and password"
					.withAuthParameters(params)				
			);
		
		//	Let's ignore new password stuff for now:
		if(result.getChallengeName().equals("NEW_PASSWORD_REQUIRED")) {
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

	
	//	This code is using the clientSecret to somehow hash/sign the provided username.
	//	I'm not really sure why this needs to be done, I guess it just proves we posess the client secret.
	private String calculateSecretHash(@NotNull String userName) {

		SecretKeySpec signingKey = 
			new SecretKeySpec(
				clientSecret.getBytes(StandardCharsets.UTF_8),
				HmacAlgorithms.HMAC_SHA_256.toString());

		try {
			Mac mac = Mac.getInstance(HmacAlgorithms.HMAC_SHA_256.toString());
			mac.init(signingKey);
			mac.update(userName.getBytes(StandardCharsets.UTF_8));
			byte[] rawHmac = mac.doFinal(clientId.getBytes(StandardCharsets.UTF_8));
			return Base64.getEncoder().encodeToString(rawHmac);

		} catch (Exception ex) {
			throw new RuntimeException("Error calculating secret hash", ex);
		}
	}

}