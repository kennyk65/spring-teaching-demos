package com.example;

import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;

@Component
//	ProxyMode will tell it to provide a proxy when injecting 
//	into other beans:
@Scope(value="prototype", proxyMode=ScopedProxyMode.TARGET_CLASS)
public class MyPrototype {

	private double value = Math.random();

	public double getValue() {
		return value;
	}
	
	
}
