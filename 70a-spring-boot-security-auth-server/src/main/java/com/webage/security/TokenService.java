package com.webage.security;

import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.JwtClaimsSet; 
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.stereotype.Service;

import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;


//  This service produces JWTs and signs them with a private key.
//  Most of the work is done by the NimbusJwtEncoder from Spring Security.
//  JWTs contain claims including the user name (sub) and authorities (scope)

@Service
public class TokenService {

    @Value("${rsa.private-key}") RSAPrivateKey privateKey;
    @Value("${rsa.public-key}") RSAPublicKey publicKey;

    public String generateToken(Authentication authentication) {
        Instant now = Instant.now();

        //  Create a space-delimited String containing all of the 
        //  principal's authorities.  These will become the "scope" inside the JWT:
        String scope = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(" "));
    
        //  Build a set of Claims to go inside the JWT.  Claims include 
        //  subject (the principal's username),  issue date of the token, 
        //  expiration time, and scopes, which map to the authorities.
        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer("self")
                .issuedAt(now)
                .expiresAt(now.plus(1, ChronoUnit.HOURS))
                .subject(authentication.getName())
                .claim("scope", scope)
                .build();

        //  The NimbusJwtEncoder creates and encodes the JWT 
        //  from the claims.  It also signs it using the private key: 
        return encoder().encode(JwtEncoderParameters.from(claims)).getTokenValue();
    }

    private JwtEncoder encoder() {
        JWK jwk = new RSAKey.Builder(publicKey).privateKey(privateKey).build();
        JWKSource<SecurityContext> jwks = new ImmutableJWKSet<>(new JWKSet(jwk));
        return new NimbusJwtEncoder(jwks);
    }

}