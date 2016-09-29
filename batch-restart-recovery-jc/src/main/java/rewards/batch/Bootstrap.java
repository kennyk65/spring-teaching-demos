package rewards.batch;

import java.io.IOException;
import java.util.UUID;

import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;

@Configuration
@ImportResource({"classpath*:batch-*-config.xml", "db-config.xml", "integration-config.xml"})
public class Bootstrap {
	
	public static void main(String[] args) throws IOException {
		ConfigurableApplicationContext context = SpringApplication.run(Bootstrap.class);
		
		// workaround for Windows XP that causes hang when generating UUID while reading from STDIN:
		UUID.randomUUID();
		System.in.read();
		context.close();
	}
}
