package com.example.client;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("openai")
public class EntityTests {
    
    record ActorFilms(String actor, List<String> movies) {}    
    record StateData(String stateName, String capitalCity, int areaInSquareMiles, int population, String famousFor) {}    
        
    private ChatClient client;
    @Autowired OpenAiChatModel model;

    @Test
    void testEntity() {
        String input = "Provide information on the largest US state.";
        client = ChatClient.builder(model).build();
        StateData stateData = 
            client
                .prompt()
                .user(input)
                .call()
                .entity(StateData.class);
        System.out.println(stateData);   
    }
    

    @Test
    void testEntity2() {
        client = ChatClient.builder(model).build();
        List<StateData> stateData = 
            client.prompt()
                .user("Provide information on the five largest US states.")
                .call()
                .entity(new ParameterizedTypeReference<List<StateData>>() {});
        System.out.println(stateData);   
    }
        
}
    