package com.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Application {

	/**
	 *	Used when launching outside a web container... 
	 */
	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}
	
}
