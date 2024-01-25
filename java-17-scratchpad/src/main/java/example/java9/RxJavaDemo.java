package example.java9;

import java.util.concurrent.TimeUnit;

import io.reactivex.rxjava3.core.Single;

public class RxJavaDemo {

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
    
    public static void main(String[] args) {
        Single.fromCallable(() -> doThing1())
                .map(RxJavaDemo::doThing2)
                .map(RxJavaDemo::doThing3)
                .doAfterSuccess(System.out::println)
                .subscribe(); // Subscribe to start the reactive pipeline
    }

}