package com.example.demo;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class HelloController {

	@GetMapping("/home")
	public String index() {
		return "index";
	}
	
	@GetMapping("/hello")
	public @ResponseBody String hello() {
		return "hello world";
	}
}
