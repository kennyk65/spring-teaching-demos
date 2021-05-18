package com.example.demo;

import org.springframework.stereotype.Component;

@Component
public class Foo {

	public void a() {
		System.out.println("Method a");
		this.b();
	}
	
	public void b() {
		System.out.println("Method b");
	}
}
