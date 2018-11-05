package com.example.demo.controllers;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

/**
 * Form object for accepting input from the signup page.
 */
public class SignupModel implements Validator{

	private String username;
	private String password;
	private String confirmPassword;
	private String email;
	private String phone;
	private String confirmBy;
	
	@NotNull
	@NotEmpty
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}

	@NotNull
	@NotEmpty
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	
	@NotNull
	@NotEmpty
	public String getConfirmPassword() {
		return confirmPassword;
	}
	public void setConfirmPassword(String confirmPassword) {
		this.confirmPassword = confirmPassword;
	}

	@Email
	@NotNull
	@NotEmpty
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	
	@Pattern(regexp="^\\(?([0-9]{3})\\)?[-.●]?([0-9]{3})[-.●]?([0-9]{4})$")
	@NotNull
	@NotEmpty
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getPhoneClean() {
		String phoneToReturn = phone;
		phoneToReturn
			.replace("(", "")
			.replace(")", "")
			.replace(" ", "")
			.replace("-", "")
			.replace("_", "")
			.replace(".", "");
		return "+1" + phoneToReturn;
	}
	
	
	@Override
	public boolean supports(Class<?> clazz) {
		return this.getClass().equals(clazz);
	}
	
	
	public String getConfirmBy() {
		return confirmBy;
	}
	public void setConfirmBy(String confirmBy) {
		this.confirmBy = confirmBy;
	}
	/**
	 * Handle a few things that the @annotations can't easily do.
	 */
	@Override
	public void validate(Object target, Errors errors) {

		if (target instanceof SignupModel) {

			if ( password != null ) {
				if ( !password.equals(confirmPassword)) {
					errors.rejectValue("confirmPassword", "confirmPasswordMatch", "Confirmation password must match password.");
				}
			}
			
		}
		
	}
	
	
}
