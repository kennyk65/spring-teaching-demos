package com.example.demo;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;

@Component
@ConfigurationProperties("cognito")
public class CognitoProperties {

	@Value("${cognito.pool.id}")	//	TODO: FOR SOME REASON, CONFIGURATIONPROPERTIES IS NOT WORKING RELIABLY.
	private String poolId;
	@Value("${cognito.client.id}")	//	TODO: FOR SOME REASON, CONFIGURATIONPROPERTIES IS NOT WORKING RELIABLY.
	private String clientId;
	@Value("${cognito.client.secret}")	//	TODO: FOR SOME REASON, CONFIGURATIONPROPERTIES IS NOT WORKING RELIABLY.
	private String clientSecret;
	
//	@PostConstruct
//	public void init() {
//		System.out.println(String.format("Cognito properties are: user pool id: %s, client id: %s, client secret %s", poolId, clientId, clientSecret));
//	}
//	
//	public boolean propertiesSet() {
//		boolean ok = true;
//		if (poolId == null ) {
//			System.out.println("ERROR - cognito.pool.id not set");
//			ok = false;
//		}
//		if (clientId == null ) {
//			System.out.println("ERROR - cognito.client.id not set");
//			ok = false;
//		}
//		if (clientSecret == null ) {
//			System.out.println("ERROR - cognito.client.secret not set");
//			ok = false;
//		}
//		return ok;
//	}
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
