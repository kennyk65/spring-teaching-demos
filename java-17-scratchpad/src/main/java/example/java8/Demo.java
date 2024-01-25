package example.java8;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

public class Demo {

    // Utility method to simulate a delay
    private static void delay(int i) {
        try { TimeUnit.SECONDS.sleep(i); } 
        catch (InterruptedException e) {}
    }

    // Simulate a long running, blocking process
    private static CompletableFuture<String> longRunningProcess() {
        return CompletableFuture.supplyAsync(() -> {
            delay(2);   // blocking!
            return "Hello World!";
        });
    }

    public static void main(String[] args) {
        // Create a CompletableFuture
        longRunningProcess().thenAccept(result -> {
            System.out.println("Result from CompletableFuture: " + result);
        });

        // Do other work...

        // Introduce a delay to prevent the JVM 
        // from exiting before work is complete.
        delay(3);
    }
}
