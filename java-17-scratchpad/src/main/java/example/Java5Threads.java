package example;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

public class Java5Threads {
    
    // Utility method to simulate a delay
    private static void delay(int i) {
        try { TimeUnit.SECONDS.sleep(i); } 
        catch (InterruptedException e) {}
    }

    // Create an instance of a Callable that returns a result variable
    private static Callable<String> myCallable = new Callable<>() {
        public String call() throws Exception {
            return longRunningProcess();
        };

        // Simulate a long running, blockingprocess
        private String longRunningProcess() {
            delay(2);               // blocking!
            return "Hello World!";
        }
    };


    public static void main(String[] args) {
        // Create a Thread, pass the Runnable, and start it:
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Future<String> future = executor.submit(myCallable);

        // Do other work...

        // Wait for the thread to complete - that's blocking!
        String result = "";
        try {
            result = future.get();  // blocking!
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        // Use results
        System.out.println("Result from the thread: " + result);
    }

}
