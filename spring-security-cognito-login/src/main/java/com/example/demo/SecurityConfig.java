package com.example.demo;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	//	Obtain a reference to our Cognito-based AuthenticationProvider:
	@Resource(name="cognitoProvider") AuthenticationProvider cognitoProvider;
	
	
	/**
	 * Configure URL access, user login and logout.
	 */
	@Override
	protected void configure(HttpSecurity http) throws Exception {

		http
		.formLogin()
			.loginPage("/login").permitAll()			//	Login page accessible to all.
			.and()
		.exceptionHandling()
			.accessDeniedPage("/denied")				//	General authorization error page.
			.and()
		.authorizeRequests()
			.mvcMatchers("/secured/**").authenticated()	//	Must be logged in to get to secured pages.
			.and()
		.logout()
			.permitAll()
			.logoutSuccessUrl("/");						//	Anyone can log out.
	}

	
	/**
	 * Spring automatically calls this method (because it is autowired) to setup
	 * global security definitions. Note that SHA-256 encryption is enabled.
	 * <p>
	 * To understand why this is an Autowired method, refer to the Security slides
	 * in the Student Handout PDF. Look for "Advanced Security - Global
	 * Configuration Choices".
	 * 
	 * @param auth
	 *            The authentication manager builder.
	 * @throws Exception
	 */
	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
		auth //
			.authenticationProvider(cognitoProvider);
	}
		
}
