package com.example.client;

import org.junit.jupiter.api.Test;
import org.springframework.ai.stabilityai.api.StabilityAiImageOptions;
import org.springframework.ai.image.ImageModel;
import org.springframework.ai.image.ImagePrompt;
import org.springframework.ai.image.ImageResponse;
import org.springframework.ai.openai.OpenAiImageOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
//@ActiveProfiles("OpenAi")
@ActiveProfiles("stabilityai")
public class ImageGenTests {

    @Autowired
    ImageModel model;

    @Test
    public void testCreateImageUrl() {

        //String request = "Create an image of two fully-crewed sailboats in a race tacking hard around a bouy.";
        String request = "Create an image of a snow skier skiing through deep powder in the trees.";

        StabilityAiImageOptions options = StabilityAiImageOptions.builder()
                .withN(1)
                .withHeight(512).withWidth(512)
                .withCfgScale(5.0f)
                .withSteps(20)
                .build();

        ImagePrompt prompt = new ImagePrompt(
                request,
                options
        );

        ImageResponse response = model.call(prompt);
        //System.out.println( "URL is: " + response.getResult().getOutput().getUrl());

        String imageB64 = response.getResult().getOutput().getB64Json();
        Utilities.testValidBase64Image(imageB64);
        Utilities.saveBase64Image(imageB64, "lab-image.png");


    }

}
