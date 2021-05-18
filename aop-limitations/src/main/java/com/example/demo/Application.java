package com.example.demo;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	@Autowired Foo foo;
	@Autowired FooAlternative alternative;
	
	@PostConstruct
	public void init() {
		foo.a();
		foo.b();
		alternative.a();
		alternative.b();
//		FooSubclass temp = new FooSubclass();
//		temp.a();
//		temp.b();
		
	}
}
