package com.example;

import javax.annotation.PostConstruct;

public class A {

	public A () {
		System.out.println("Constructor from class: " + A.class.getSimpleName() );
	}
	
	@PostConstruct
	public void init() {
		System.out.println("Init method of " + A.class.getSimpleName());
	}
}
