package com.example.wiremockdemo;

import static com.github.tomakehurst.wiremock.client.WireMock.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.reactive.server.WebTestClient;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;


//  This attempt is based on https://rieckpil.de/spring-boot-integration-tests-with-wiremock-and-junit-5/
//  This fails due to java.lang.NoClassDefFoundError: javax/servlet/DispatcherType

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT) 
@ContextConfiguration(initializers = {WireMockInitializer.class}) 
public class Attempt2 {

    @Autowired
    private WireMockServer wireMockServer;

  @Autowired
  private WebTestClient webTestClient;
 
  @Test
  void testGetAllTodosShouldReturnDataFromClient() {
    wireMockServer.stubFor(
      WireMock.get("/todos")
        .willReturn(aResponse()
          .withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
          .withBody("[{\"userId\": 1,\"id\": 1,\"title\": \"Learn Spring Boot 3.0\", \"completed\": false}," +
            "{\"userId\": 1,\"id\": 2,\"title\": \"Learn WireMock\", \"completed\": true}]"))
    );
    
    // ... controller invocation using the WebTestClient
  }    
}
