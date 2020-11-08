package com.example.demo;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Component;

@Component
public class B {

	private String foo;
	
	public void setFoo(String s) {
		foo = s;
	}
	
	public B (A a) {
		System.out.println("constructor for A ran."  );
	}
	
//	@PostConstruct
	public void init() {
		System.out.println("Value of foo is: " + foo);
	}
	
//	@Autowired //@Lazy
//	public void setA(A a) {
//		System.out.println("" + a.getClass());
//	}
}
