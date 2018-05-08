package demo.reactive;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.BaseStream;

import org.reactivestreams.Publisher;

import reactor.core.publisher.Flux;
import reactor.core.publisher.GroupedFlux;
 
public class Main {
	public static void main(String [] args) throws IOException {
		
		Main m = new Main();
		m.sortedWords(
			//	Turn the incoming file into a Flux of lines:
			fromPath(new File("words.txt").toPath()))
		.subscribe(System.out::println);
	}
	
	public Flux<KeyValue> sortedWords(Flux<String> lines) {
		
		//	Turn the Flux of lines into a Flux of individual words (ignore punctuation):
		Flux<String> words = lines.flatMap(line -> Flux.fromArray(line.split(" ")));
		
		//	Turn the Flux of words into a Flux of KeyValue(word,1):
		Flux<KeyValue> kvs = words.map(word -> new KeyValue(word, 1));
		
		//	Turn the single Flux of KeyValue(word,1) into many Fluxes based on the word:
		Flux<GroupedFlux<String,KeyValue>> gf = kvs.groupBy(keyValue -> keyValue.key);
		
		//	Take the Flux for each word and turn it into a Flux of KeyValue<word,count>
		Flux<KeyValue> wordAndCount = gf.flatMap(
			group -> group.reduce(
				new KeyValue("initialValue",0), 
				(a, b) ->  new KeyValue(b.key, a.value+b.value)
			)
		);

		
		// ...and sort and return.
		return wordAndCount.sort();  
	}
			
	static final public class KeyValue implements Comparable<KeyValue>{
		String key;
		int value;
		
		KeyValue(String key, int value) {
			this.key = key;
			this.value = value;
		}
		public String toString() {
			return key + ":" + value;
		}
		@Override
		public int compareTo(KeyValue o) {
			if(o != null){
				if(this.value<o.value)return -1;
				if(this.value>o.value)return 1;
				return 0;
			}
			return 1;
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


