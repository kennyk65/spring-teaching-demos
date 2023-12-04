package example;

import java.util.concurrent.TimeUnit;

import reactor.core.publisher.Mono;

public class Java9Reactor {

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
        Mono.fromCallable(() -> doThing1())
            .map(Java9Reactor::doThing2)
            .map(Java9Reactor::doThing3)
            .doOnNext(System.out::println)
            .subscribe(); 
    }

}
