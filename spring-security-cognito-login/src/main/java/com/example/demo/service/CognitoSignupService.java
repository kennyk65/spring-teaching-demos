package com.example.demo.service;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.validation.constraints.NotNull;

import org.apache.commons.codec.digest.HmacAlgorithms;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.amazonaws.services.cognitoidp.AWSCognitoIdentityProvider;
import com.amazonaws.services.cognitoidp.AWSCognitoIdentityProviderClientBuilder;
import com.amazonaws.services.cognitoidp.model.AttributeType;
import com.amazonaws.services.cognitoidp.model.SignUpRequest;
import com.example.demo.controllers.SignupModel;

@Service
public class CognitoSignupService {

	@Value("${COGNITO.POOL.ID}")
	private String poolId;
	
	@Value("${COGNITO.CLIENT.ID}")
	private String clientId;
	
	@Value("${COGNITO.CLIENT.SECRET}")
	private String clientSecret;

	@Autowired
	AWSCognitoIdentityProvider cognitoClient;
	
	public void signUp(SignupModel model) {
		
		cognitoClient.signUp(
			new SignUpRequest()
				.withClientId(clientId)
				.withUsername(model.getUsername())
				.withPassword(model.getPassword())
				.withSecretHash(calculateSecretHash(model.getUsername(),clientSecret))
				.withUserAttributes(
						new AttributeType().withName("phone_number").withValue(model.getPhoneClean()),
						new AttributeType().withName("email").withValue(model.getEmail())
					)
				);
		
	}
	
	
	//	This code is using the clientSecret to somehow hash/sign the provided username.
	//	I'm not really sure why this needs to be done, I guess it just proves we posess the client secret.
	private String calculateSecretHash(@NotNull String userName, @NotNull String secret) {

		SecretKeySpec signingKey = 
			new SecretKeySpec(
				secret.getBytes(StandardCharsets.UTF_8),
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
