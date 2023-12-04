package example;

@FunctionalInterface
public interface Consumer<T> {
 void accept(T t);

 default Consumer<T> andThen(Consumer<? super T> after) {
  return i -> { accept(i);
                after.accept(i);
              };
 }

}
