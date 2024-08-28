package com.example.client;

import org.junit.jupiter.api.Test;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("openai")
public class SystemTextTest {

    ChatClient client;
    @Autowired OpenAiChatModel model;

    @Test
    void testSystem() {

        String systemMessage =
            "You are a REST service that provides responses in 100% JSON format.";
        String userMessage =
            "List the names and capitals of all US States, in descending order by population";

        client =
            ChatClient.builder(model)
                .defaultSystem(systemMessage)
                .build();

        String response =
            client
                .prompt().user(userMessage)
                .call()
                .content();

        System.out.println(response); 
    }

    @Test
    void testSystem2() {
        client = 
            ChatClient.builder(model)
                .defaultSystem("All content returned must be 100% JSON")
                .build();

        String response =
            client
                .prompt().user("Provide basic information on the moon")
                .call()
                .content();

        System.out.println(response); 
    }

    @Test
    void testSystem3() {
        client = 
            ChatClient.builder(model)
                .defaultSystem("Any missing or unavailable information should be represented with a placeholders: STOCK_PRICE, TRADING_VOLUME, RECENT_NEWS")
                .build();

        String response =
            client
                .prompt().user("Provide a brief overview of company IBM, including today's trading information and recent news.")
                .call()
                .content();

        System.out.println(response); 
    }

}