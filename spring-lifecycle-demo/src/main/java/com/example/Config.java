package com.example;

import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Config {

	@Bean
	public A a() {
		return new A();
	}

	@Bean
	public B b() {
		return new B();
	}

	@Bean
	public C c() {
		return new C();
	}
	
	@Bean
	public BeanFactoryPostProcessor bfpp() {
		return new BFPP();
	}
	
	@Bean
	public BeanPostProcessor bpp() {
		return new BPP();
	}
	
}
