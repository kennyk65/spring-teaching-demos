package example.java21;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Demo {

    // Utility method to simulate a delay
    private static void delay(int i) {
        try { TimeUnit.SECONDS.sleep(i); } 
        catch (InterruptedException e) {}
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


    // Create an instance of a Runnable that implements our logic
    private static Runnable myRunnable = () -> {
        String result1 = doThing1();        // Blocking, or so it seems...
        String result2 = doThing2(result1); // Blocking, or so it seems...
        String result3 = doThing3(result2); // Blocking, or so it seems...
        System.out.println("Result from the thread: " + result3);
    };


    public static void main(String[] args) {
        // Create a Virtual Thread, pass the Runnable, and start it:
        Executors.newVirtualThreadPerTaskExecutor().submit(myRunnable);

        // Do other work...

        // Introduce a delay to prevent the JVM 
        // from exiting before work is complete.
        delay(4);
    }
}