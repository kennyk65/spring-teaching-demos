package com.example;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;

@Configuration
@EnableAuthorizationServer		//	We are now an OAuth 2 Authorization Server
//@EnableResourceServer			//	We are now an OAuth 2 Resource Server too
public class OAuth2Config 
	// extends AuthorizationServerConfigurerAdapter 
{

}
