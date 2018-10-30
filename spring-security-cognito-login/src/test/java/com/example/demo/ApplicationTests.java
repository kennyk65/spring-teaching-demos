package com.example.demo;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment=WebEnvironment.MOCK)
@AutoConfigureMockMvc

public class ApplicationTests {

	@Autowired MockMvc mock;
	
	/**
	 *  Any attempt to access a secured page should result in a redirect to the login page:
	 */
	@Test
	public void notLoggedInYet() throws Exception {

		mock.perform( 
			get("/secured/anything") )
			.andExpect(redirectedUrl("http://localhost/login"));
	}

	
	/**
	 *  We expect if you try to login with bad credentials, you are simply put back on the login page with a message.
	 *  
	 *  Note that this test will actually interact with AWS Cognito via our CognitoAuthenticationProvider.
	 */
	@Test
	public void invalidLoginAttempt() throws Exception {
		
		mock.perform(
			post("/login")
			.with(csrf())				//	Required for form processing in Spring Security as a way to address CSRF attacks.
			.param("username", "bogus")
			.param("password", "incorrect")
		)
			.andExpect(redirectedUrl("/login?error"));
		;
		
	}
	
	
	/**
	 *  Valid credentials should result in the user reaching the default page in lieu of anywhere else to go.
	 *  
	 *  Note that this test will actually interact with AWS Cognito via our CognitoAuthenticationProvider.
	 */
	@Test
	public void validLoginAttempt() throws Exception {
		
		mock.perform(
			post("/login")
			.with(csrf())					//	Required for form processing in Spring Security as a way to address CSRF attacks.
			.param("username", "robert")	//	This assumes the Cognito user pool has been populated with this user.
			.param("password", "robert")
		)
		.andExpect(status().is3xxRedirection())
		.andExpect(redirectedUrl("/secured/default"))
		;
		
	}
	
	
	/**
	 *  If we attempt to access a secured page with valid credentials, we should be successful.
	 *  
	 *  Note that this does not actually authenticate with Cognito.  It only provides 
	 *  Spring Security with an AuthenticationToken that looks legit.  So it tests our
	 *  security mappings, but it doesn't test interaction with AWS.
	 */
	@Test
	@WithMockUser(username="robert",password="robert")  
	public void authenticatedOk() throws Exception {

		mock.perform( 
			get("/secured/target") )
			.andExpect(status().isOk())
			.andExpect(view().name("secured/target"));
	}

	
}
