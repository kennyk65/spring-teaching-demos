package example;

import java.util.concurrent.TimeUnit;

public class Java1Threads {
    // Shared variable to store the result
    private static String result;

    // Utility method to simulate a delay
    private static void delay(int i) {
        try { TimeUnit.SECONDS.sleep(i); } 
        catch (InterruptedException e) {}
    }

    // Create an instance of a Runnable that updates the shared result variable
    private static Runnable myRunnable = new Runnable (){
        public void run() {
            String s = longRunningProcess();  
            synchronized (this) {
                result = s;     //  Blocking!  Safely update the shared variable.
            }
        }

        //  Simulate a long running, blocking process:
        private String longRunningProcess() {
            delay(2);               // blocking!
            return "Hello World!";         
        }
    };

    public static void main(String[] args) {
        // Create a Thread, pass the Runnable, and start it:
        Thread thread = new Thread(myRunnable);
        thread.start();

	    // Do other work...

        // Wait for the thread to complete - that's blocking!
        try {
            thread.join();  //  blocking!
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Use results
        String s = null;
        synchronized (myRunnable) { 
            s = result;     //  Blocking! Safely acquire the shared variable
        } 
        System.out.println("Result from the thread: " + s);
    }
}
