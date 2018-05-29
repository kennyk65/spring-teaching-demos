package com.example;

import java.security.Principal;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RequestMapping;

@SpringBootApplication
public class Application {
	   private static final Log logger = LogFactory.getLog(Application.class);
	   
	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}
	
	   @RequestMapping("/user")
	   public Principal user(Principal user) {
	      logger.info("AS /user has been called");
	      logger.debug("user info: "+user.toString());
	      return user;
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
