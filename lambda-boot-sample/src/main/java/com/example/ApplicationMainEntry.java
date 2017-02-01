package com.example;

import org.springframework.cloud.serverless.SpringLauncherSingleton;

/**
 * Entry point of the application when running from the command line.
 */
public class ApplicationMainEntry {

	public static void main(String[] args) {
    	SpringLauncherSingleton.getInstance();
	}
}
