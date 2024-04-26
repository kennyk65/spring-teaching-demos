package com.webage.security;

import java.security.interfaces.RSAPublicKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.web.SecurityFilterChain;

//  Establish this application as an Resource Server.
//  Each request is expected to provide an Authorization header containing a signed JWT as a Bearer token.
//  The signature will be checked using the PUBLIC key from the Authorization Server.

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Value("${rsa.public-key}") RSAPublicKey publicKey;

    private JwtDecoder jwtDecoder() {
        return NimbusJwtDecoder.withPublicKey(publicKey).build();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
            //  Disable sessions.  We want a stateless application:
            .sessionManagement(
                session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) 

            //  CSRF protection is merely extra overhead with session management disabled:
            .csrf(csrf -> csrf.disable())

            //  All inbound requests must be authenticated:
            .authorizeHttpRequests( auth -> auth
                .requestMatchers("/").permitAll()
                .anyRequest().authenticated()
            )

            //  Cusomize our resource server to receive JWTs, 
            //  decoded using our custom decoder above:
             .oauth2ResourceServer((resourceServer) ->
                 resourceServer.jwt( (customizer) -> customizer.decoder(jwtDecoder()) )
             )
            .build();
    }    

}
