package example.java5;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

public class ChainingAsync {

    public static void main(String[] args) {

        try {
            ExecutorService svc = Executors.newSingleThreadExecutor();

            Future<String> future1 = svc.submit(new WorkToDo1());
            // Do other work...
            String result1 = future1.get();  // blocking!
            Future<String> future2 = svc.submit(new WorkToDo2(result1));
            // Do other work...
            String result2 = future2.get();  // blocking!
            Future<String> future3 = svc.submit(new WorkToDo3(result2));
            // Do other work...
            String result3 = future3.get();  // blocking!

            System.out.println("Result from the thread: " + result3);

        } catch (Exception e) {}
    }

    private static class WorkToDo1 implements Callable<String> {
        public String call() throws Exception {
            delay(1);       // blocking!
            return "Hello";
        }
    }

    private static class WorkToDo2 implements Callable<String> {
        private final String prefix;
        public WorkToDo2(String prefix) {
            this.prefix = prefix;
        }
        public String call() throws Exception {
            delay(1);       // blocking!
            return prefix + " World";
        }
    }

    private static class WorkToDo3 implements Callable<String> {
        private final String prefix;
        public WorkToDo3(String prefix) {
            this.prefix = prefix;
        }
        public String call() throws Exception {
            delay(1);       // blocking!
            return prefix + "!!";
        }
    }

    // Utility method to simulate a delay:
    private static void delay(int i) {
        try { TimeUnit.SECONDS.sleep(i); } 
        catch (InterruptedException e) {}
    }
   
}