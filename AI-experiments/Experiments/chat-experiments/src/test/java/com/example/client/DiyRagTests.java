package com.example.client;

import org.junit.jupiter.api.Test;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("openai")
public class DiyRagTests {

    ChatClient client;
    @Autowired    ChatModel model;

    @Test
    void test() {

        String systemMessage =
        """
        You are a REST web service that returns only JSON responses.
        You examine the given user message and respond with a JSON object describing information required to respond accurately.
        The root element of the JSON object should be "InformationNeeded".
        For weather data, include a "Weather" element listing the cities for which current data is required.
        For travel advisory data, include an "Advisories" element listing the cities for which current data is required.
        For suggested route information, include a "Route" element listing the origin and destination locations.
        Any other missing data required for a complete response should be described in an element called "Other". 
        """;
        String userMessage =
        "Will there be any bad weather today for my drive from Kalamazoo to Detroit? Are there any good places to stop and eat on the way?";

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

}
