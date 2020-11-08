package com.example;

import javax.annotation.PostConstruct;

import org.springframework.core.annotation.Order;

public class A {

	public A () {
		System.out.println("Constructor from class: " + A.class.getSimpleName() );
	}
	
	
//	@PostConstruct
//	public void hiThere() {
//		System.out.println("Class A PostConstruct");
//	}
//	
}
