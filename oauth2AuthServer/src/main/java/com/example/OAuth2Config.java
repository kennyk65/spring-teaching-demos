package com.example;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;

@Configuration
@EnableAuthorizationServer		//	We are now an OAuth 2 Authorization Server
//@EnableResourceServer			//	We are now an OAuth 2 Resource Server too
public class OAuth2Config 
	//extends AuthorizationServerConfigurerAdapter 
{

//	@Override
//	public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
//		clients.inMemory()
//				.withClient("theOneAndOnlyClient")
//				.secret("DoNotTellAnyone")
//				.authorizedGrantTypes("authorization_code", "refresh_token")
//				.scopes("openid","user");
//	}
}
