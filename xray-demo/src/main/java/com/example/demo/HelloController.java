package com.example.demo;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.amazonaws.xray.spring.aop.XRayEnabled;

@RestController
@XRayEnabled
public class HelloController {

	@GetMapping("/")
	public String hiThere() {
		return "hello world!";
	}
	
}
