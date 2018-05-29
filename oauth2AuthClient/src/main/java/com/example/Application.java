package com.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
//@EnableOAuth2Sso	//	No longer needed if you have a spring.security.oauth2.client.registration property
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}
}
