package com.spring3.oauth.jwt.services;

import com.spring3.oauth.jwt.models.RefreshToken;
import com.spring3.oauth.jwt.repositories.RefreshTokenRepository;
import com.spring3.oauth.jwt.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

/**
 * @author mhmdz
 * Created By Zeeshan on 20-05-2023
 * @project oauth-jwt
 */

@Service
public class RefreshTokenService {

    @Autowired RefreshTokenRepository refreshTokenRepository;
    @Autowired UserRepository userRepository;

    @SuppressWarnings("null")
    public RefreshToken createRefreshToken(String username){
        RefreshToken refreshToken = RefreshToken.builder()
                .userInfo(userRepository.findByUsername(username))
                .token(UUID.randomUUID().toString())
                .expiryDate(Instant.now().plusMillis(JwtService.TOKEN_DURATION_TIME))
                .build();
        refreshToken = refreshTokenRepository.save(refreshToken);
        //System.out.println("Refresh Token Created.");
        return refreshToken;
    }


    public Optional<RefreshToken> findByToken(String token){
        return refreshTokenRepository.findByToken(token);
    }

    
    public RefreshToken verifyExpiration(RefreshToken token){
        var timeUntilExpiration = token.getExpiryDate().compareTo(Instant.now());      
        if(timeUntilExpiration<0){
            refreshTokenRepository.delete(token);
            throw new RuntimeException(token.getToken() + " Refresh token is expired. Please make a new login..!");
        }
        System.out.println(String.format("Token is valid for %s more milliseconds.",timeUntilExpiration));
        return token;

    }

}
