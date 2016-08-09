package com.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.web.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableAsync;

import com.amazonaws.services.simpleworkflow.AmazonSimpleWorkflowClient;

@SpringBootApplication
@EnableAsync
public class Application {


	/**
	 *	Used when launching outside a web container... 
	 */
	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}
	

	@Bean
	public AmazonSimpleWorkflowClient simpleWorkflowClient() {
		return new AmazonSimpleWorkflowClient();
	}

	
}
