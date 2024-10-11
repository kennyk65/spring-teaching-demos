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
        SimpleUrlAuthenticationFailureHandler handler = 
            new SimpleUrlAuthenticationFailureHandler("/");
        
        http
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/*","/error","/webjars/**","/logout").permitAll()
                .anyRequest().authenticated()
            )
            .logout(l -> l.logoutSuccessUrl("/").permitAll())
            .csrf(c -> c
                .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
                .ignoringRequestMatchers("/logout")
            )
            .oauth2Login(o -> o
                .failureHandler(
                    (request, response, exception) -> {
                        request.getSession().setAttribute("error.message", exception.getMessage());
                        handler.onAuthenticationFailure(request, response, exception);
            }))
            ;

        return http.build();
    }

    @GetMapping("/user")
    public Map<String, Object> user(@AuthenticationPrincipal OAuth2User principal) {
        if (principal != null) {
            return Collections.singletonMap("name", principal.getAttribute("name"));
        }
        return null;
    }


    @GetMapping("/error")
    public String error(HttpServletRequest request) {
        String message = (String) request.getSession().getAttribute("error.message");
        request.getSession().removeAttribute("error.message");
        return message;
    } 

    @Bean
    public OAuth2AuthorizedClientService authorizedClientService(ClientRegistrationRepository clientRegistrationRepository) {
        OAuth2AuthorizedClientService delegate = new InMemoryOAuth2AuthorizedClientService(clientRegistrationRepository);
        return new TokenLoggingService(delegate);
    }    
}
