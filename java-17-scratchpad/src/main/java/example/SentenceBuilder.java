package example;

import java.util.function.Consumer;

public class SentenceBuilder {
    public static void main(String[] args) {
        StringBuilder sentence = new StringBuilder();

        // Define two Consumers to append words to the sentence
        Consumer<String> appendSpace = word -> sentence.append(" ");
        Consumer<String> appendWord = word -> sentence.append(word);

        // Compose the Consumers using andThen
        Consumer<String> buildSentence = appendWord.andThen(appendSpace);

        // Use the composed Consumer to build the sentence
        buildSentence.accept("This");
        buildSentence.accept("is");
        buildSentence.accept("a");
        buildSentence.accept("sample");
        buildSentence.accept("sentence");

        // Print the final sentence
        System.out.println(sentence.toString().trim());
    }
}