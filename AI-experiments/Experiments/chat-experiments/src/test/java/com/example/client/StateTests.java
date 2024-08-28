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

import static org.assertj.core.api.Assertions.assertThat;



@SpringBootTest
@ActiveProfiles("openai")
//@ActiveProfiles("aws")
public class StateTests {

    private ChatClient client;
    @Autowired ChatModel model;

    @Test
    void testCallback() {

        InMemoryChatMemory memory = new InMemoryChatMemory(); 

        client = ChatClient
            .builder(model)
            .defaultAdvisors(
                new MessageChatMemoryAdvisor (memory)
            )
            .build();

        var conversationKey = AbstractChatMemoryAdvisor.CHAT_MEMORY_CONVERSATION_ID_KEY;

        //  Conversation 1: Facts about the Great Lakes.
        String response1 = client.prompt()
            .user("List out the names of the Great Lakes.")
            .advisors(a -> a.param(conversationKey, 111))
            .call()
            .content();
        
        System.out.println(response1);
        // use assertJ to assert that the response contains the names of the Great Lakes:
        assertThat(response1).contains("Superior", "Huron", "Michigan", "Erie", "Ontario");

        //  Conversation 2: Facts about Florida Lakes.
        String response2 = client.prompt()
        .user("List out the 10 largest lakes in Florida.")
        .advisors(a -> a.param(AbstractChatMemoryAdvisor.CHAT_MEMORY_CONVERSATION_ID_KEY, 222))
        .call()
        .content();
    
        System.out.println(response2);
        // use assertJ to assert that the response contains the names of some Florida lakes:
        assertThat(response2).contains("Okeechobee", "George", "Apopka");
        

        //  Execute another test to investigate whether the chat client is aware of state.
        response1 = client.prompt()
        .user("Which one is the biggest by surface area?")
        .advisors(a -> a.param(AbstractChatMemoryAdvisor.CHAT_MEMORY_CONVERSATION_ID_KEY, 111))
        .call()
        .content();
        
        System.out.println(response1);
        // use assertJ to assert that the response contains the word "Superior":
        assertThat(response1).contains("Superior");

        //////////////////

        //  Execute another test to prove that the chat client is aware of a different conversation.
        response2 = client.prompt()
        .user("Which one is the deepest?")
        .advisors(a -> a.param(AbstractChatMemoryAdvisor.CHAT_MEMORY_CONVERSATION_ID_KEY, 222))
        .call()
        .content();
        
        System.out.println(response2);
        // use assertJ to assert that the response contains the name "George":
        assertThat(response2).contains("George");

    }
        
}
