package com.example.client;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import com.example.client.EntityTests.ActorFilms;

import java.util.Map;

@SpringBootTest
@ActiveProfiles("openai")
public class CallbackTests {

    private ChatClient client;
    @Autowired OpenAiChatModel model;

    @Test
    void testCallback() {

        String userMessage =
        """
        Provide a 50-100 word overview of company {SYMBOL}}, 
        including today's trading information and recent news.";
        """;
        String prompt =
                new PromptTemplate(userMessage)
                        .render(Map.of("SYMBOL", "NVDA"));

        client = ChatClient.builder(model).
                defaultFunctions(
                        "getStockPriceFunction",
                        "getStockVolumeFunction",
                        "getStockNewsFunction")
                .build();

        String response = client.prompt()
            .user(prompt)
//                .functions(
//                    "getStockPriceFunction",
//                    "getStockVolumeFunction",
//                    "getStockNewsFunction")
                        .call()
                            .content();
        
        System.out.println(response);
        // use assertJ to assert that the response contains the word "price" and "$":
        assertThat(response).contains("price", "$", "volume");
    }
        
}
