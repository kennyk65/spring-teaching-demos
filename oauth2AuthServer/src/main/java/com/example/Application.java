package com.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}
	
	
    /**
     *	Make this app an OAuth 2 Authorization Server. 
     */
//	@Configuration
//	@EnableAuthorizationServer
//	protected static class OAuth2Config extends AuthorizationServerConfigurerAdapter {
//
//		@Override
//		public void configure(AuthorizationServerSecurityConfigurer security)
//				throws Exception {
//			// TODO Auto-generated method stub
//			super.configure(security);
//		}
//
//		@Override
//		public void configure(ClientDetailsServiceConfigurer clients)
//				throws Exception {
//			// TODO Auto-generated method stub
//			super.configure(clients);
//		}
//
//		@Override
//		public void configure(AuthorizationServerEndpointsConfigurer endpoints)
//				throws Exception {
//			// TODO Auto-generated method stub
//			super.configure(endpoints);
//		}
//		
//		
//	}
	
}
