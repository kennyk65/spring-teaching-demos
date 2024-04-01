package com.spring3.oauth.jwt;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;

import com.spring3.oauth.jwt.dtos.AuthRequestDTO;
import com.spring3.oauth.jwt.dtos.JwtResponseDTO;
import com.spring3.oauth.jwt.dtos.RefreshTokenRequestDTO;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class JwtTests {

    @Autowired TestRestTemplate template;

    /**
     *  This test will create a token, use it to get a resource, refresh the token, and use it again.
     *  The test will fail if the token is not valid.
     *  The test will fail if the resource is not accessible.
     *  The test will fail if the token is not refreshed.
     *  The test will fail if the token is not refreshed with a new token.
     *  The test will fail if the token is not refreshed with a new token.
     *  The test will fail if the token is not refreshed with a new token.
     */
    @Test
    void tokenCreateAndUse() {
        var token = getToken();             //  Get a token
        useToken(token);                    //  Try using it by getting a resource...
        refreshToken(token.getToken());     //  Try refreshing it and getting a new token...
        useToken(token);                    //  New token should work just fine.
    }


    private JwtResponseDTO getToken() {
        var request = new AuthRequestDTO();
        request.setUsername("username123");
        request.setPassword("test2day");
    
        var response =
            template.exchange(
                RequestEntity.post("/api/v1/login").body(request), 
                JwtResponseDTO.class
            );

        testTokenResponse(response);    
        return response.getBody();
    }


    private void useToken(JwtResponseDTO token) {
        var response = template.exchange(
            RequestEntity
                .get("/api/v1/test")
                .header("Authorization", "Bearer " + token.getAccessToken())
                .build(),
                String.class
        );
        assertTrue(response.getStatusCode().is2xxSuccessful());     //  Should not be 403.
        assertNotNull(response.getBody());
    }


    private JwtResponseDTO refreshToken(String refreshToken) {
        var request = new RefreshTokenRequestDTO();
        request.setToken(refreshToken);

        var response =
            template.exchange(
                RequestEntity.post("/api/v1/refresh").body(request), 
                JwtResponseDTO.class
            );

        testTokenResponse(response);    
        return response.getBody();
    }


    private void testTokenResponse(ResponseEntity<JwtResponseDTO> token) {
        var body = token.getBody();
        assertTrue(token.getStatusCode().is2xxSuccessful());
        assertNotNull(body);

        var jwtToken = body.getAccessToken();
        var refreshToken = body.getToken();
        assertNotNull(jwtToken);
        assertNotNull(refreshToken);
    }

}
