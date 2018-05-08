package demo.reactive;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.BaseStream;

import reactor.core.publisher.Flux;
import reactor.core.publisher.GroupedFlux;
 
public class SensorWarning {
	public static void main(String [] args) throws IOException {
		
		SensorWarning m = new SensorWarning();
		m.theLogic(
			//	Turn the incoming file into a Flux of sensor readings:
			fromPath(new File("sensors.txt").toPath()))
		.subscribe(System.out::println);
	}
	

	/**
	 * Given a Flux of incoming sensor readings, return a Flux of 
	 * sensor IDs having more than 5 high temperature (above 50) readings:
	 */
	public Flux<String> theLogic(Flux<KeyValue> sensorReadings) {
		
		//	Filter sensors higher than a threshold:
		Flux<KeyValue> highTemps = 
			sensorReadings.filter( (reading) -> reading.value > 50 );

		//	Group the sensors by sensor id:
		Flux<GroupedFlux<String,KeyValue>> gf = highTemps.groupBy(keyValue -> keyValue.key);

		//	Count # of high readings:
		Flux<KeyValue> highTempSensors = gf.flatMap(
				group -> group.reduce(
					new KeyValue("initialValue",0), 
					(a, b) ->  new KeyValue(b.key, a.value+1)
				)
			);
		
		//	Screen out only those with 5 high temps or more:
		Flux<KeyValue> warnings = 
			highTempSensors.filter((sensor) -> sensor.value >= 5 ); 
		
		//	Just return the sensor id's:
		Flux<String> sensorIds = warnings.map((kv) -> kv.key);
		
		return sensorIds;  
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

	private static Flux<KeyValue> fromPath(Path path) {
		
		//	Read the input file into a Flux of lines:
		Flux<String> lines = Flux.using(
				() -> Files.lines(path),
				Flux::fromStream,
				BaseStream::close
		);
		
		//	Turn the Flux of lines into a Flux of sensor readings:
		return
			lines.map(line -> { 
					String[] s = line.split(",");
					return new KeyValue(s[0],new Integer(s[1]));
				}
			);		
	}
}


