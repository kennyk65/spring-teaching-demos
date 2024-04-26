package com.webage.dao;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.Arrays;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.webage.dao.ResourceServerDao;
import com.webage.domain.Customer;
import com.webage.domain.Purchase;

import wiremock.com.google.common.collect.Lists;

/**
 * This test exercises interaction with the ResourceServer.
 * WireMock is used as a substitute for the actual server.
*/
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@TestPropertySource(properties = "resource.server.port=16774")   // Psuedo-random.
public class ResourceServerDaoImplTests {

    //  The Object we are using for our test entry point.
    //  Remember, this object contains our RestClient:
    @Autowired
    ResourceServerDao client;

    //  Wiremock is used to intercept HTTP calls and provide mock responses.
    //  This means we can test our software without needing to call a real server:
    private WireMockServer wireMockServer;

    //  Get the port to use for our WireMock server:   
    @Value("${resource.server.port}")
    private int wiremockPort;

    @BeforeEach
    public void setup() {
        //  Configure WireMock to intercept calls to to http://localhost:<port>:
        wireMockServer = new WireMockServer(wireMockConfig().port(wiremockPort));
        wireMockServer.start();
        configureFor("localhost", wireMockServer.port());

        //  Program WireMock.
        //  WHEN a GET to /purchases occurs with a Bearer token in the Authorization header 
        //  THEN it should return 200 with some sample data: 
        stubFor(get(urlEqualTo("/purchases") )
            .withHeader("Authorization", matching("Bearer .+")) // Match if Authorization header is present
            .willReturn(aResponse()
                .withHeader("Content-Type", "application/json")
                .withBody(makeTestData())
            ));
    }

    @Test
    //@Disabled
    public void findAllPurchases() {
        //  Call our ResourceServerClient.  
        //  Remember, this object contains our RestClient 
        //  which we use to call the ResourceServer.  
        //  But the RestClient is using a URL based on the port 
        //  defined above, so it reaches the WireMock server instead.
        Iterable<Purchase> purchases = client.findAllPurchases("FAKE_JWT");

        //  Our test data was only two customers:
        assertNotNull(purchases);
        var purchaseList = Lists.newArrayList(purchases);
        assertThat(purchaseList.get(0).getProduct().equalsIgnoreCase("something")  );
    }

    @AfterEach
    public void teardown() {
        // Stop the WireMock server after the test
        wireMockServer.stop();
    }

    private String makeTestData() {
		Customer c = new Customer();
		c.setName("test");
		c.setId(1);
		c.setEmail("a@a.com");
		Purchase p = new Purchase();
		p.setId(1);
		p.setProduct("something");
		p.setCustomer(c);

        ObjectMapper mapper = new ObjectMapper();
        try{
            return mapper.writeValueAsString(Arrays.asList(p));
        }
        catch(Exception e){
            throw new RuntimeException(e);
        } 
    }

}
