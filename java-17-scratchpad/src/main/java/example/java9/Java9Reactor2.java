package example.java9;

import java.util.concurrent.TimeUnit;

import reactor.core.publisher.Mono;

public class Java9Reactor2 {

    // Utility method to simulate a delay
    private static void delay(int i) {
        try { TimeUnit.SECONDS.sleep(i); } 
        catch (InterruptedException e) {}
    }

    public static void main(String[] args) {
        doThing1()
            .flatMap(Java9Reactor2::doThing2)
            .flatMap(Java9Reactor2::doThing3)
            .doOnNext(System.out::println)
            .subscribe(); 
    }

    private static Mono<String> doThing1() {
        delay(1);
        return Mono.just("Hello");
    }

    private static Mono<String> doThing2(String input) {
        delay(1);
        return Mono.just(input + " World");
    }

    private static Mono<String> doThing3(String input) {
        delay(1);
        return Mono.just(input + "!!");
    }
}
