package com.example.wiremockdemo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;

@SpringBootApplication
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

  @Bean
  public WebClient helloClient(WebClient.Builder webClientBuilder) {
    return webClientBuilder
	  //.baseUrl("https://jsonplaceholder.typicode.com")
      .baseUrl("http://169.254.169.254")
      //.baseUrl("http://127.0.0.1")
      .defaultHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
      .build();
  }	
}
