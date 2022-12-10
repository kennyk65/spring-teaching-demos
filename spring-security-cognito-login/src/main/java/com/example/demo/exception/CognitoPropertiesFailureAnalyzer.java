package com.example.demo.exception;

import org.springframework.boot.diagnostics.AbstractFailureAnalyzer;
import org.springframework.boot.diagnostics.FailureAnalysis;

public class CognitoPropertiesFailureAnalyzer  extends AbstractFailureAnalyzer<CognitoPropertiesException> {

	@Override
	protected FailureAnalysis analyze(Throwable rootFailure, CognitoPropertiesException cause) {

		return new FailureAnalysis(
			cause.getCognitoProperties().toString() + "\n" + cause.getCognitoProperties().getMissingPropertiesMessage(),
			"Set these missing properties using either \n" +
			"  environment variables (i.e. COGNITO_POOLID=??),\n" +
			"  Java system properties (i.e. -Dcognito.poolId=??)\n" +
			"  or command line arguments (--cognito.poolId=??).", 
			cause);
	}

}
