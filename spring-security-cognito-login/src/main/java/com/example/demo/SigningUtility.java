package com.example.demo;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.validation.constraints.NotNull;

import org.apache.commons.codec.digest.HmacAlgorithms;

public class SigningUtility {

	/*
	 * Many calls to the Cognito User Pool APIs require signed parameters, typically when 
	 * a user pool has been constructed with an application client secret.  This code can
	 * be used anytime such a signed value is required.  Input is username and client secret.
	 */	
	public static String calculateSecretHash(String clientId, String userName, String clientSecret) {

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
