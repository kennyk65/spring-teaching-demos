package example.java8;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

public class ChainDemo {

    // Utility method to simulate a delay
    private static void delay(int i) {
        try { TimeUnit.SECONDS.sleep(i); } 
        catch (InterruptedException e) {}
    }

    private static String doThing1() {
        delay(1);
        return "Hello";
    }

    private static String doThing2(String s) {
        delay(1);
        return s + " World";
    }

    private static String doThing3(String s) {
        delay(1);
        return s + "!!";
    }


    public static void main(String[] args) {

        CompletableFuture
            .supplyAsync(() -> doThing1())
            .thenApplyAsync(s -> doThing2(s))
            .thenApplyAsync(s -> doThing3(s))
            .thenAcceptAsync(System.out::println);

        System.out.println("Main thread continues, unhindered.");

        // Introduce a delay to prevent the JVM 
        // from exiting before work is complete.
        delay(4);
    }
}