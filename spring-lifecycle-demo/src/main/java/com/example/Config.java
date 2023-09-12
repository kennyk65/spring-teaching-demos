package com.example;

import ch.qos.logback.core.net.SyslogOutputStream;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Config {

	public Config () {
		System.out.println("this is the Config constructor.");
	}

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

//	 @Bean
//	 public static BeanFactoryPostProcessor bfpp() {
//	 	return new BFPP();
//	 }
//
//	 @Bean
//	 public BeanPostProcessor bpp() {
//	 	return new BPP();
//	 }
}
