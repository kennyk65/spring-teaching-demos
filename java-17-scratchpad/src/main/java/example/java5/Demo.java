package example.java5;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

public class Demo {
    
    // Utility method to simulate a delay:
    private static void delay(int i) {
        try { TimeUnit.SECONDS.sleep(i); } 
        catch (InterruptedException e) {}
    }

    // Create a Callable that returns a result variable
    private static Callable<String> workToDo = new Callable<>() {
        public String call() throws Exception {
            return longRunningProcess();
        };

        // Simulate a long running, blocking process
        private String longRunningProcess() {
            delay(2);               // blocking!
            return "Hello World!";
        }
    };


    public static void main(String[] args) {
        // Create a singleThread ExecutorService, and submit the work:
        Future<String> future = 
            Executors
                .newSingleThreadExecutor()
                    .submit(workToDo);

        // Do other work...

        // Wait for the thread to complete:
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