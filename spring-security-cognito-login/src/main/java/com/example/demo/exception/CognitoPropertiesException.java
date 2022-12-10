package com.example.demo.exception;

import com.example.demo.CognitoProperties;

public class CognitoPropertiesException extends RuntimeException{

	private CognitoProperties properties;
	
	public CognitoPropertiesException(CognitoProperties properties) {
		this.properties = properties;
	}

	public CognitoProperties getCognitoProperties() {
		return properties;
	}
}
