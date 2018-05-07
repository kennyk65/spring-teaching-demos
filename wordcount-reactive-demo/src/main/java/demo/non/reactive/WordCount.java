package demo.non.reactive;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Iterator;
import java.util.Map;
import java.util.Scanner;
import java.util.TreeMap;

public class WordCount {
	public static void main(String [] args) throws FileNotFoundException {		
		Scanner scanner = new Scanner(new File("words.txt"));
		Map<String, Integer> map = new TreeMap<String, Integer>();
		while(scanner.hasNextLine()) {
			String string = scanner.nextLine();
			for (String key: string.split("\\s+")) {
				Integer count = map.get(key);
				if (count == null) {
					map.put(key,  1);
				} else {
					map.put(key,  count + 1);
				}
			}
		}
		scanner.close();
		Iterator<String> iterator = map.keySet().iterator();
		while(iterator.hasNext()) {
			String key = iterator.next();
			System.out.println(key + ":" + map.get(key));
		}
	}
}
