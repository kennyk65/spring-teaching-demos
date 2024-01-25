package com.example.wiremockdemo;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.get;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.env.Environment;

import com.github.tomakehurst.wiremock.WireMockServer;


@SpringBootTest
// @EnableWireMock({
//         @ConfigureWireMock(name = "user-service", property = "user-client.url")
// })
public class TodoControllerTest {


        // @InjectWireMock("user-service")
        private WireMockServer wiremock;

        @Autowired
        private Environment env;

        @Test
        void aTest() {
            // returns a URL to WireMockServer instance
            env.getProperty("user-client.url");
            wiremock.stubFor(get("/todolist").willReturn(aResponse()
                    .withHeader("Content-Type", "application/json")
                    .withBody("[{ \"id\": 1, \"userId\": 1, \"title\": \"my todo\" } ]")
            ));
        }

}