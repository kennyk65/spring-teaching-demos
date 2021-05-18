package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class FooAlternative {

	@Autowired
	FooAlternative self;
	
	public void a() {
		System.out.println("Method a");
		self.b();
	}
	
	public void b() {
		System.out.println("Method b");
	}
}
