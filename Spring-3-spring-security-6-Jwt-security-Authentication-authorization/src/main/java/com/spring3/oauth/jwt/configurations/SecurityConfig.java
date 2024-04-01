package com.spring3.oauth.jwt.configurations;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * @author mhmdz
 * Created By Zeeshan on 20-05-2023
 * @project oauth-jwt
 */
@Configuration
//@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    @Autowired
    JwtAuthFilter jwtAuthFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
        .csrf( config -> config.disable() )   // TODO: WHY?
        .authorizeHttpRequests((authz) -> authz
            .requestMatchers("/api/v1/save", "/api/v1/login", "/api/v1/refreshToken").permitAll()
            .requestMatchers("/api/v1/**").authenticated()
        )
        // .sessionManagement( config -> { config
        //     .sessionCreationPolicy(SessionCreationPolicy.STATELESS); }
        // )
     //   .authenticationProvider(authenticationProvider())
        .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
        .build();

        // return http.csrf().disable()
        //         .authorizeHttpRequests()
        //         .requestMatchers("/api/v1/save", "/api/v1/login", "/api/v1/refreshToken").permitAll()
        //         .and()
        //         .authorizeHttpRequests().requestMatchers("/api/v1/**")
        //         .authenticated()
        //         .and()
        //         .sessionManagement()
        //         .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        //         .and()
        //         .authenticationProvider(authenticationProvider())
        //         .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class).build();

    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    // @Bean
    // public UserDetailsService userDetailsService(){
    //     return new UserDetailsServiceImpl();
    // }

    // @Bean
    // public AuthenticationProvider authenticationProvider(){
    //     DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
    //     authenticationProvider.setUserDetailsService(userDetailsService());
    //     authenticationProvider.setPasswordEncoder(passwordEncoder());
    //     return authenticationProvider;

    // }

    //  Required because Spring Boot / Security does not create 
    //  one of these unless you specifically ask, apparently.
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }


    @Bean
    public UserDetailsService userDetailsService(AuthenticationManagerBuilder auth) {
        return new InMemoryUserDetailsManager(
            User.withUsername("username123")
                .password(passwordEncoder().encode("test2day"))
                .roles("ADMIN")
                .build()
            );
    }

    // @Bean 
    // public UserDetailsManager userDetailsManager(DataSource dataSource) {
    //     String sql = "INSERT INTO users (id, password, username, enabled) VALUES (NEXT VALUE FOR users_seq, ?, ?, ?)";

    //     var manager = new JdbcUserDetailsManager(dataSource);
    //     manager.setCreateUserSql(sql);
    //     manager.createUser(
    //         User.withUsername("username123")
    //             .password(passwordEncoder().encode("test2day"))
    //             .roles("ADMIN")
    //             .build());
    //     return manager;
    // }

}
