package com.example.demo;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

@Component
@Aspect
public class LoggingAspect {

	@Before("execution(* Foo.*(..)) || execution(* FooAlternative.*(..))")
	public void log(JoinPoint jp) {
		System.out.println(
				"logging call to " + 
				jp.getSignature().getDeclaringType() + 
				"." + jp.getSignature().getName());
	}
}
