package com.example;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class EnvironmentController {

	//	InstanceMetadata is only available when running on an EC2 instance; 
	//	when testing locally, it is unavailable.
	@Autowired(required=false) InstanceMetadata instanceMetadata;
	
	
	@RequestMapping("/")
	public  String showEnvironment(Model m) {
		m.addAttribute("instanceMetadata", instanceMetadata );
		return "environment";
	}
	
}
