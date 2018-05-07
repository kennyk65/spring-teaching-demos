package demo.reactive;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.BaseStream;

import reactor.core.publisher.Flux;
 
public class Main {
	public static void main(String [] args) throws IOException {
		fromPath(new File("words.txt").toPath())
		.flatMap(line -> Flux.fromArray(line.split(" ")))
		.map(word -> new KeyValue(word, 1))
		.groupBy(keyValue -> keyValue.key)				
		.flatMap(group -> group.reduce(new KeyValue("BaseValue",0), (a, b) -> {
				return new KeyValue(b.key, a.value+b.value);
			}))
 			.subscribe(System.out::println);
	}
			
	static final public class KeyValue {
		String key;
		int value;
		
		KeyValue(String key, int value) {
			this.key = key;
			this.value = value;
		}
		public String toString() {
			return key + ":" + value;
		}
	}

	private static Flux<String> fromPath(Path path) {
		return Flux.using(
				() -> Files.lines(path),
				Flux::fromStream,
				BaseStream::close
		);
	}
}


