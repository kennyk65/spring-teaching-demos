package com.example.demo;

import java.util.Collections;
import java.util.Map;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.client.InMemoryOAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletRequest;

@Configuration
@EnableWebSecurity
@RestController
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        //  This authentication failure handler stores the error message in the session, 
        //  so that it can be displayed on the home page, see the usage below.
        SimpleUrlAuthenticationFailureHandler handler = 
            new SimpleUrlAuthenticationFailureHandler("/");
        
        http
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/*","/webjars/**").permitAll()
                .anyRequest().authenticated()
            )
            
            // POST to /logout clears the session and invalidates cookies.
            // On logout, redirect to home page, where JS on the page will notice the absence 
            // of user information, and display the login options.
            .logout(l -> l.logoutSuccessUrl("/").permitAll())

            // Not strictly needed for this demo, but is is good to enable CSRF protection.
            // However, it isn't necessary on /logout, even though our client passes the cookie anyway.
            .csrf(c -> c
                .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
                .ignoringRequestMatchers("/logout")
            )

            // On a login failure, store the error message in the session, and redirect to the home page.
            .oauth2Login(o -> o
                .failureHandler(
                    (request, response, exception) -> {
                        request.getSession().setAttribute("error.message", exception.getMessage());
                        handler.onAuthenticationFailure(request, response, exception);
            }))
            ;

        return http.build();
    }

    /**
     * This endpoint is used by the client to determine if the user is logged in.
     * @param principal - may be null or empty, indicating there is no logged in user.
     * @return A map containing the user's name, if available.
     */
    @GetMapping("/user")
    public Map<String, Object> user(@AuthenticationPrincipal OAuth2User principal) {
        // Different Authorization Servers send back the user's name differently.
        // It is often in the "name" claim, such as with GitHub or Google.
        // Spring's Authorization server sends it in the "sub" (subject) claim.
        if (principal != null) {
            String name = principal.getAttribute("name");
            if (name == null) {
                name = principal.getAttribute("sub");
            }
            return Collections.singletonMap("name", name);
        }
        return null;
    }


    /**
     * Used by front-end code to obtain an error message, if any.
     * @param request - the HTTP request object.
     * @return The error message, if any.
     */
    @GetMapping("/error")
    public String error(HttpServletRequest request) {
        String message = (String) request.getSession().getAttribute("error.message");
        request.getSession().removeAttribute("error.message");
        return message;
    } 


    /**
     * Spring Boot / Security will automatically configure this bean, 
     * but we are adding a logging feature.
    */
    @Bean
    public OAuth2AuthorizedClientService authorizedClientService(ClientRegistrationRepository clientRegistrationRepository) {
        OAuth2AuthorizedClientService delegate = 
            new InMemoryOAuth2AuthorizedClientService(clientRegistrationRepository);
        return new TokenLoggingService(delegate);
    }    
}
