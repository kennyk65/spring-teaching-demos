package com.example.client;

import org.junit.jupiter.api.Test;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.AbstractChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.memory.InMemoryChatMemory;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import reactor.core.publisher.Flux;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("openai")

public class StreamingTests {

    private ChatClient client;
    @Autowired
    ChatModel model;

    @Test
    void testStream() {

        client = ChatClient
                .builder(model)
                .build();
        String input = "List the names and capitals of all United Nations members, in descending order by population.";
        Flux<String> flux = client.prompt()
                .user(input)
                .stream()
                .content();

        System.out.println("Call complete, waiting for response...");
        flux.doOnNext(System.out::println).blockLast();
    }
}
