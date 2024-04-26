package com.webage.dao;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.configureFor;
import static com.github.tomakehurst.wiremock.client.WireMock.matching;
import static com.github.tomakehurst.wiremock.client.WireMock.post;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import com.github.tomakehurst.wiremock.WireMockServer;

/**
 * This test exercises interaction with the AuthorizationServer.
 * WireMock is used as a substitute for the actual server.
*/
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@TestPropertySource(properties = "authorization.server.port=26874")   // Psuedo-random.
public class AuthorizationServerDaoImplTests {

    //  The Object we are using for our test entry point.
    //  Remember, this object contains our RestClient:
    @Autowired AuthorizationServerDao client;

    //  Get the port to use for our WireMock server:   
    @Value("${authorization.server.port}") int wiremockPort;

    //  Wiremock is used to intercept HTTP calls and provide mock responses.
    //  This means we can test our software without needing to call a real server:
    private WireMockServer wireMockServer;

    @BeforeEach
    public void setup() {
        //  Configure WireMock to intercept calls to to http://localhost:<port>:
        wireMockServer = new WireMockServer(wireMockConfig().port(wiremockPort));
        wireMockServer.start();
        configureFor("localhost", wireMockServer.port());

        //  Program WireMock.
        //  WHEN a POST to /token occurs with an HTTP Basic Authorization header 
        //  THEN it should return 200 with a sample JWT 
        stubFor(post(urlEqualTo("/token"))
            .withHeader("Authorization", matching("Basic .+")) // Match if Authorization header is present
            .willReturn(aResponse()
                .withStatus(200)
                .withBody("SAMPLE-JWT")
            ));
    }

    @Test
    //@Disabled
    public void getJwt() {
        //  Call our AuthorizationServerClient.  
        //  Remember, this object contains our RestClient 
        //  which we use to call the AuthorizationServer.  
        //  But the RestClient is using a URL based on the port 
        //  defined above, so it reaches the WireMock server instead.
        String jwt = client.getJwt();

        //  Our test data was only two customers:
        assertNotNull(jwt);
        assertThat(jwt.contains("SAMPLE-JWT"));
    }
        
    @AfterEach
    public void teardown() {
        // Stop the WireMock server after the test
        wireMockServer.stop();
    }

}
