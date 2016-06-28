package com.example;

import javax.annotation.PostConstruct;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}
	
	//	This bean method has args.  Will the arg values affect scope?
	@Bean
	public Gizmo gizmo(String value) {
		return new Gizmo(value);
	}
	
	@Bean
	public GizmoHolder bean1() {
		return new GizmoHolder(gizmo("bean1"));
	}
	@Bean
	public GizmoHolder bean2() {
		return new GizmoHolder(gizmo("bean2"));
	}
	
	//	Result: Singleton means the first one created is the one in the
	//	app context.  The values referenced by the other calls are 
	//	essentially quietly ignored.  Of course in this case,
	//	if you really wanted prototype behavior, you should ask for it.
	@PostConstruct
	public void init() {
		System.out.println(bean1());
		System.out.println(bean2());
		System.out.println(gizmo("another"));
		
	}
}
