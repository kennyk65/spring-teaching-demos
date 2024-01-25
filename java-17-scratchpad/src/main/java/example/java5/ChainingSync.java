package example.java5;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

public class ChainingSync {

    public static void main(String[] args) {
        String result1 = doThing1();
        String result2 = doThing2(result1);
        String result3 = doThing3(result2);
        System.out.println("Result from a single thread: " + result3);
    }

    private static String doThing1() {
        delay(1);
        return "Hello";
    }

    private static String doThing2(String input) {
        delay(1);
        return input + " World";
    }

    private static String doThing3(String input) {
        delay(1);
        return input + "!!";
    }

    // Utility method to simulate a delay:
    private static void delay(int i) {
        try { TimeUnit.SECONDS.sleep(i); } 
        catch (InterruptedException e) {}
    }

}
