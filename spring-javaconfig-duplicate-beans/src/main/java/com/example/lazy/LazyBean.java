package com.example.lazy;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

@Component
@Lazy
public class LazyBean {

	public LazyBean() {
		System.out.println("Just Instantiated " + this.getClass());
	}
	
	public String getStaticValue() {
		return "Some Test Value" ;
	}
}
