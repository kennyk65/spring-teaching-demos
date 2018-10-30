package com.example.demo;

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
	
	@Test
	public void loginRedirect() throws Exception {

		//	Any attempt to access a secured page should result in a redirect to the login page:
		mock.perform( 
			get("/secured/anything") )
			.andExpect(redirectedUrl("http://localhost/login"));
	}

	@Test
	@WithMockUser(username="robert",password="robert")
	public void authenticatedOk() throws Exception {

		//	If we attempt to access a secured page with valid credentials, we should be successful:
		mock.perform( 
			get("/secured/aSecuredPage") )
			.andExpect(status().isOk())
			.andExpect(view().name("aSecuredPage"));
	}

	
}
