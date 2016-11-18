package com.example;

import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.stereotype.Component;

import com.amazonaws.services.lambda.runtime.Context;

@Component
public class Sample {

	private AtomicInteger counter = new AtomicInteger(0);
	
	public Object handle(Object o, Context context) {
		return "Invocation #" + counter.incrementAndGet();
	}
}
