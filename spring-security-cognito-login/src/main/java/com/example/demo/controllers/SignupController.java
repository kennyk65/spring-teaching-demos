package com.example.demo.controllers;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.demo.service.CognitoSignupService;

@Controller
public class SignupController {

	@Autowired
	CognitoSignupService service;
	
	@GetMapping("/signup")
	public void getSignup() {
	}
	
	@PostMapping("/signup")
	public String postSignup( Model m, @Valid SignupModel input, BindingResult errors, RedirectAttributes redirectModel ) {

		input.validate(input, errors);  // Some extra validations 
		if (errors.hasErrors()) {
			System.out.println("Errors Encountered");
			
			//	Problem: I can't get the mustache-based template to see the contents of the binding result.
			//	So I am putting them on the model directly.  Only handles one error per field right now:
			for (ObjectError oe : errors.getAllErrors() ) {
				if (oe instanceof FieldError) {
					FieldError fe = (FieldError)oe;
					m.addAttribute(fe.getField() + "." + "errors", fe.getDefaultMessage() );
				}
			}
			
			return "signup";
		}		
		
		service.signUp(input);
		redirectModel.addFlashAttribute(input);
		return "redirect:signup-confirmation";
	}
}
