package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class A {

//	public A (B b) {}

	@Autowired
	public void setB(B b) {
		System.out.println("setter for a.setB() ran.");
	}
}
