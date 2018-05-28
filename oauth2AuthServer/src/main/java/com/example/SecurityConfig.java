package com.example;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		
		http.authorizeRequests()
	        .antMatchers("/", "/home").permitAll()	//	Landing page
	        .anyRequest().authenticated()

	    .and().formLogin()
			.loginPage("/login")
			.permitAll()			//	Everyone is allowed to get to the login page

        .and().logout()
            .permitAll();				
			
		;
	}

	
	
	@Autowired 
	public void configureAuthentication ( AuthenticationManagerBuilder builder ) throws Exception {
		
		//	Apparently, in either Boot 2 or Security 5, passwords MUST be encoded
		PasswordEncoder encoder = new BCryptPasswordEncoder();
		
		builder.inMemoryAuthentication().passwordEncoder(encoder)
	//	.withUser("user").password( encoder.encode("password")).roles("USER").and()
		.withUser("hughie").password( encoder.encode("hughie")).roles("USER").and()
		.withUser("dewey").password( encoder.encode("dewey")).roles("USER").and()
		.withUser("louie").password( encoder.encode("louie")).roles("USER")
			;
	}	

}
