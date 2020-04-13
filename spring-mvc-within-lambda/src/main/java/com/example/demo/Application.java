package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

@SpringBootApplication
public class Application extends SpringBootServletInitializer {

	//	Entry point when running within a web container
	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(Application.class);
	}

	//	Entry point when running from command line.
	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	//java.lang.NoClassDefFoundError: org/springframework/boot/context/embedded/EmbeddedServletContainer
}
