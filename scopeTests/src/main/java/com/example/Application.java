package com.example;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Application {

	@Autowired MySingleton single;
	
	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}
	
	@PostConstruct
	public void init(){
		System.out.println("Using proxy:  ");
		for(int i=0; i<5; i++) {
			//	The result: Every time we call the singleton, and it
			//	calls the prototype, we get a separate instance of 
			//	the prototype:
			System.out.println( "value is: " + single.getValue());
		}
		System.out.println("Using provider:  ");
		for(int i=0; i<5; i++) {
			//	The result: Every time we call the singleton, and it
			//	calls the Provider, we get a separate instance of 
			//	the prototype:
			System.out.println( "value is: " + single.getValue2());
		}
	}
}
