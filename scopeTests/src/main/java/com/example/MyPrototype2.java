package com.example;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("prototype")
public class MyPrototype2 {

	private double value = Math.random();

	public double getValue() {
		return value;
	}
	
	
}
