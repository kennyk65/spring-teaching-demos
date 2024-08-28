package com.example.client;

import org.assertj.core.api.Assertions;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;

public class Utilities {

    //  This method provides basic verification that a Base-64 String APPERAS to be a valid image.
    //  It does not have a way to verify the content of the image.
    public static void testValidBase64Image(String b64) {

        //  Can the incoming Base-64 String be decoded:
        byte[] decoded = Base64.getDecoder().decode(b64);
        Assertions.assertThat(decoded).isNotEmpty(); // Check for decoded data

        //  Test that the decoded data APPEARS to represent a valid PNG image.
        //  PNGs start with 0x89 signature and at least 4 bytes
        ByteArrayInputStream bis = new ByteArrayInputStream(decoded);
        boolean hasCommonImageHeaderBytes =
                bis.read() == 0x89 && bis.available() >= 4;

        Assertions.assertThat(hasCommonImageHeaderBytes)
                .withFailMessage("Returned Base64 data does not appear to be a valid PNG image.")
                .isTrue();
    }

    //  This method saves a Base-64 encoded image to a file.
    public static void saveBase64Image(String b64, String filePath) {
        if (filePath == null || filePath.isEmpty()) {
            filePath = "image.png";
        }
        byte[] decoded = Base64.getDecoder().decode(b64);
        try {
            //  Use the Paths.of to create a Path object from a String:
            Path p = Paths.get(filePath);
            System.out.println("Saving image to: " + p.toAbsolutePath());
            Files.write(p, decoded);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}

