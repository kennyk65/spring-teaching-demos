package com.spring3.oauth.jwt.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.spring3.oauth.jwt.dtos.AuthRequestDTO;
import com.spring3.oauth.jwt.dtos.JwtResponseDTO;
import com.spring3.oauth.jwt.dtos.RefreshTokenRequestDTO;
import com.spring3.oauth.jwt.dtos.UserRequest;
import com.spring3.oauth.jwt.dtos.UserResponse;
import com.spring3.oauth.jwt.models.RefreshToken;
import com.spring3.oauth.jwt.services.JwtService;
import com.spring3.oauth.jwt.services.RefreshTokenService;
import com.spring3.oauth.jwt.services.UserService;


@RestController
@RequestMapping("/api/v1")
public class ApiController {

    @Autowired UserService userService;
    @Autowired JwtService jwtService;
    @Autowired RefreshTokenService refreshTokenService;
    @Autowired AuthenticationManager authenticationManager;


    @PostMapping("/save")
    public UserResponse saveUser(@RequestBody UserRequest userRequest) {
        return userService.saveUser(userRequest);
    }

    @GetMapping("/users")
    public List<UserResponse> getAllUsers() {
        return userService.getAllUser();
    }


    @PostMapping("/profile")
    public UserResponse getUserProfile() {
        return userService.getUser();
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @GetMapping("/test")
    public String test() {
        return "Welcome.  You appear to have the ADMIN role.";
    }

    @GetMapping("/ping")
    public String ping() {
        return "Ping Endpoint";
    }

    @PostMapping("/login")
    public JwtResponseDTO AuthenticateAndGetToken(@RequestBody AuthRequestDTO request) {
        Authentication authentication = 
            authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                    request.getUsername(), 
                    request.getPassword()));
        if(authentication.isAuthenticated()){
            System.out.println(String.format("Generating JWT token for %s",request.getUsername()));
            return JwtResponseDTO.builder()
                .accessToken(jwtService.GenerateToken(request.getUsername()))
                .token(refreshTokenService.createRefreshToken(request.getUsername()).getToken())
                .build();

        } else {
            throw new UsernameNotFoundException("invalid user request..!!");
        }

    }


    @PostMapping("/refresh")
    public JwtResponseDTO refreshToken(@RequestBody RefreshTokenRequestDTO request){
        return 
            refreshTokenService.findByToken(request.getToken())         // Take the incoming "refresh" token...
            .map(refreshTokenService::verifyExpiration)                 // Check the DB.  Has it expired?
            .map(RefreshToken::getUserInfo)                             // If not, use it to retrieve user info ...
            .map(userInfo -> {
                System.out.println(String.format("Refreshing JWT token for %s",userInfo.getUsername()));
                return JwtResponseDTO.builder()                                         // Send back a new response...            
                .accessToken(jwtService.GenerateToken(userInfo.getUsername()))  // With a newly created JWT...                                  
                .token(refreshTokenService.createRefreshToken(userInfo.getUsername()).getToken())
                .build();
            })
            .orElseThrow(() ->new RuntimeException("Refresh Token is not in DB..!!"));  // Or simply expired.
    }

}
