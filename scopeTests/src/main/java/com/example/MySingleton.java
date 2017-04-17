package com.example;

import javax.inject.Provider;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MySingleton {

	//	I am a singleton.  But I am injected with a prototype...
	@Autowired MyPrototype proto;
	
	//	I am a singleton.  But I am injected with a Provider
	//	to a prototype:
	@Autowired Provider<MyPrototype2> proto2;
	
	public double getValue() {
		return proto.getValue();
	}

	
	public double getValue2() {
		//	Two stages:
		return proto2.get().getValue();
	}
}
