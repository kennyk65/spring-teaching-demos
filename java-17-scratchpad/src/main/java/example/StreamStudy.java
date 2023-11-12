package example;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class StreamStudy {

    public static void main(String[] s) {
        // Slide 1: Create a stream.  Print it:
        var fullname = "Rutherford B Hayes";
        Stream.of(fullname.split(" +")) // Split name into stream of three Strings
                .forEach(System.out::println);//  Print each name component on separate line.

        // Slide 2: Introduce Map function
        //String fullname = "Rutherford B Hayes";
        Stream.of(fullname.split(" +")) // Split name into stream of three Strings
                .map(a -> a.substring(0, 1))    //  Convert into new Stream of first letters from each
                .forEach(System.out::print);    //  Print each initial.

        // Slide 3: Introduce Reduce function (and Optional).
        // String fullname = "Rutherford B Hayes";
        Stream.of(fullname.split(" +")) // Create stream of three Strings
                .map(a -> a.substring(0, 1))    //  Convert into new Stream of first letters from each
                .reduce((a, b) -> a.concat(b))  //  Concatenate letters into single Optional<String>
                .ifPresent(System.out::println);//  Print the optional, if a value is present.

        // Slide 4: Introduce FlatMap:
        // String fullname = "Rutherford B Hayes";
        Stream.of(fullname)
                .flatMap( a -> Stream.of(a.split(" +")) )   //  Convert into stream of three Strings
                .forEach(System.out::println);              //  Print each name component on separate line.

        // Combine with Map and Reduce: Print initials
        Stream.of(fullname)
                .flatMap( a -> Stream.of(a.split(" +")) )   //  Convert into stream of three Strings
                .map(a -> a.substring(0, 1))    //  Convert into new Stream of first letters from each
                .reduce((a, b) -> a.concat(b))  //  Concatenate letters into single Optional<String>
                .ifPresent(System.out::println);//  Print the optional, if a value is present.

        // Slide 5: Getting exotic:
        //  Stream within a stream: Print each president's initials:
        Arrays.asList("Rutherford B Hayes", "James K Polk", "James A Garfield").stream()
                .forEach( a -> Stream.of(a.split(" +"))
                        .map(b -> b.substring(0, 1))    //  Convert into new Stream of first letters from each
                        .reduce((b, c) -> b.concat(c))  //  Concatenate letters into single Optional<String>
                        .ifPresent(System.out::println)
                );

        // Compare with: old school:
        var presidents = Arrays.asList("Rutherford B Hayes", "James K Polk", "James A Garfield");
        for (String president : presidents) {
            String[] components = president.split(" +");
            char[] initials = new char[3];
            int index = 0;
            for(String component : components) {
                initials[index++] = component.charAt(0);
            }
            System.out.println(new String(initials));
        }

        Arrays.asList(1,2,3,4,5,6).stream()
                .takeWhile( i -> i < 4 )
                .forEach(System.out::print);

        System.out.println("");

        Arrays.asList(1,2,3,4,5,6).stream()
                .dropWhile( i -> i < 4 )
                .forEach(System.out::print);

//        Stream<String> stream = Stream.iterate("", a -> a.length() < 100, a -> a + "s" )
//                .dropWhile(a -> a.contains("sssss"));
//        stream.forEach(System.out::println);

        Stream<String> stream2 = Stream.iterate("", a -> a.length() < 100, a -> a + "s" )
                .takeWhile(a -> a.length() < 10);
        stream2.forEach(System.out::println);



//        Stream<String> stream = Stream.iterate("", a -> a + "s").forEach(b -> System.out.println(b));
//                .forEach(System.out::println);
//       //         .takeWhile(a -> a.length() < 10)
// //               .forEach(System.out::println);
    }
}
