package com.example;

import org.springframework.boot.SpringApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Import;


@Import({ Config1.class, Config2.class })
public class Application {

    public static void main(String[] args) {
      ApplicationContext context = SpringApplication.run(Application.class);
      System.out.println("Example:" + context.getBean("example"));
    }    

}
