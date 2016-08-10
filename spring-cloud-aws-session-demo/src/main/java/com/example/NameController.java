package com.example;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;


@Controller
@RequestMapping(value="/" )
@SessionAttributes("simpleForm")
public class NameController {
	
	@ModelAttribute
	public SimpleForm preBuildModel() {
		return new SimpleForm();
	}
	
	@RequestMapping(method=GET )
	public String getName() {
		return "name";
	}

	@RequestMapping(method=POST )
	public String postName(SimpleForm simpleForm) {
		return "redirect:/";
	}
}
