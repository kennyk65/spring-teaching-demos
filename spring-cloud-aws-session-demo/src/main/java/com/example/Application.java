package com.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class Application extends SpringBootServletInitializer {

	
	/**
	 * Used when launching within a web container...
	 */
	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
		return builder.sources(Application.class);
	}

	/**
	 *	Used when launching outside a web container... 
	 */
	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}
	
}
