package example;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;


class Person {
    private final String name;

    public Person(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void introduceYourself() {
        System.out.println("Hello, my name is " + name);
    }
}

class Student extends Person {
    private final int score;

    public Student(String name, int score) {
        super(name);
        this.score = score;
    }

    public int getScore() {
        return score;
    }
}

public class GenericSuperExample {
    public static void main(String[] args) {
        Map<String,Person> people = new HashMap<>();
        people.put("Alice", new Student("Alice", 95));
        people.put("Bob", new Student("Bob", 87));

        // SUPER means this consumer is designed to operate on a Person or SUPERCLASS - not subclass.
        // Consumers are intended to work with super-bound types, not extend-bound like you would think.

        BiConsumer<String, ? super Person> introducePerson = (name, person) -> {
            System.out.print(name + ": ");
            if (person instanceof Person) {
                ((Person) person).introduceYourself();
            }
        };

        people.forEach(introducePerson);
    }
}

