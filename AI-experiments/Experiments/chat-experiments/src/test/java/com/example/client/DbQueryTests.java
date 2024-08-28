package com.example.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("openai")
public class DbQueryTests {

    private ChatClient client;
    @Autowired
    ChatModel model;

    @Test
    void testQuery() {
        String systemPrompt =
        """
        You are an SQL generating web service.
        Responses must be valid, HyperSQL-compatible, executable SQL statements.  
        The SQL statement must be placed between <SQL-START> and <SQL-END> tags.
        Use the following database schema to generate SQL queries: %s
        """;
        String schema = readSchemaFile();

        client = 
            ChatClient.builder(model)
                .defaultSystem(String.format(systemPrompt, schema))
                    .build();

        String userMessage =
        "List the sales of the top 3 products by revenue during the last month.";
        String response =
            client
                .prompt().user(userMessage)
                    .call()
                        .content();
    
        System.out.println(response);                 

        assert response.contains("<SQL-START>") && response.contains("<SQL-END>");

        // Extract the SQL statement from the response:
        String sql = response.substring(
                response.indexOf("<SQL-START>") + "<SQL-START>".length(),
                response.indexOf("<SQL-END>"));

        List<Map<String,Object>> results = 
            template.queryForList(sql);
    
        System.out.println(results);                 

        ///////////////////////

        systemPrompt =
        "You are a web service which specializes in executive summaries.";

        ChatClient client =
            ChatClient.builder(model)
                .defaultSystem(String.format(systemPrompt, schema))
                    .build();

        response =
            client
                .prompt().user(
                    userMessage + ".  Supporting data:  " + results)
                        .call()
                            .content();

        System.out.println(response);
    }

    @Autowired JdbcTemplate template;


    @Autowired
    ResourceLoader resourceLoader;

    static String readSchemaFile() {
        try {
            return new ClassPathResource("schema.sql").getContentAsString(StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}
