package com.example.demo;

import org.springframework.boot.context.properties.ConfigurationProperties;

import com.example.demo.exception.CognitoPropertiesException;

import jakarta.annotation.PostConstruct;

@ConfigurationProperties("cognito")
public class CognitoProperties {

	private String poolId;
	private String clientId;
	private String clientSecret;

	@PostConstruct
	public void init() {
		if (!valid()) {
			throw new CognitoPropertiesException(this);
		}
	}


	@Override
	public String toString() {
		return "CognitoProperties [poolId=" + poolId + ", clientId=" + clientId + ", clientSecret=" + clientSecret	+ "]";
	}


	public String getMissingPropertiesMessage() {
		String message = "";
		if (poolId == null || poolId.length() < 1 ) {
			message += "ERROR - cognito.poolId not set\n";
		}
		if (clientId == null || clientId.length() < 1 ) {
			message += "ERROR - cognito.clientId not set\n";
		}
		if (clientSecret == null || clientSecret.length() < 1 ) {
			message += "ERROR - cognito.clientSecret not set\n";
		}
		return message;
	}

	public boolean valid() {
		return getMissingPropertiesMessage().length()<1;
	}


	public String getPoolId() {
		return poolId;
	}
	public void setPoolId(String poolId) {
		this.poolId = poolId;
	}
	public String getClientId() {
		return clientId;
	}
	public void setClientId(String clientId) {
		this.clientId = clientId;
	}
	public String getClientSecret() {
		return clientSecret;
	}
	public void setClientSecret(String clientSecret) {
		this.clientSecret = clientSecret;
	}
}
