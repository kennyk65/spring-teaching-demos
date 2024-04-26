package com.webage.web;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.security.interfaces.RSAPublicKey;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
class AuthControllerTest {

    @Autowired
    MockMvc mvc;

    @Test
    //@Disabled
    void tokenWhenUnauthenticatedThen401() throws Exception {
        this.mvc.perform(get("/token"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    //@Disabled
    void tokenWhenAuthenticatedThenValidJwt() throws Exception {
        MvcResult result =
                mvc.perform(
                    post("/token")
                    .with(httpBasic("client", "DoNotTell"))
                )
                .andExpect(status().isOk())
                .andReturn();

        String encodedToken = result.getResponse().getContentAsString();
        Jwt token = jwtDecoder().decode(encodedToken);
        var claims = token.getClaims();

        assertTrue(claims.containsKey("iss"));
        assertEquals("self", claims.get("iss"));

        assertTrue(claims.containsKey("sub"));
        assertEquals("client", claims.get("sub"));    //  The user's name

        assertTrue(claims.containsKey("scope"));
        assertEquals("read", claims.get("scope"));  //  One of the user's authorities
    }

    
    @Value("${rsa.public-key}") RSAPublicKey publicKey;

    private JwtDecoder jwtDecoder() {
        return NimbusJwtDecoder.withPublicKey(publicKey).build();
    }

}
