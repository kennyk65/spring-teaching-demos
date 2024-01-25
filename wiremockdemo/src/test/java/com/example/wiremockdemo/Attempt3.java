package com.example.wiremockdemo;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.client.WireMockBuilder;
import com.github.tomakehurst.wiremock.core.WireMockApp;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import com.maciejwalkowiak.wiremock.spring.ConfigureWireMock;
import com.maciejwalkowiak.wiremock.spring.EnableWireMock;
import com.maciejwalkowiak.wiremock.spring.InjectWireMock;

import reactor.core.publisher.Mono;

// This attempt is based on https://github.com/maciejwalkowiak/wiremock-spring-boot/
// It uses com.maciejwalkowiak.spring / wiremock-spring-boot / 2.0.0
// When using Spring Boot 3.x The error is java.lang.NoClassDefFoundError: javax/servlet/DispatcherType
// When using Spring Boot 2.x it can start without blowing up.

@SpringBootTest
@EnableWireMock({
        @ConfigureWireMock(name = "user-service", property = "user-client.url")
})
public class Attempt3 {
    @InjectWireMock("user-service")
    private WireMockServer wiremock;

    @Value("${user-client.url}")
    private String wiremockUrl; // injects the base URL of the WireMockServer instance

    @Autowired
    WebClient webClient;

    @Test
    void aTest() {

        //  WireMockConfiguration.wireMockConfig().bindAddress("http://169.254.169.254");

        wiremock.stubFor(
            get("/hello")
            .willReturn(
                aResponse()
                .withBody("hello world")));       
        wiremock.start();

        String result = 
            WebClient.create("http://169.254.169.254")
            .get().uri("/hello")
            .retrieve().bodyToMono(String.class).block();

        assertEquals(result, "hello world");
    }
    
}
