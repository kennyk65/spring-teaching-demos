package rewards.batch.config;

import javax.jms.ConnectionFactory;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.command.ActiveMQQueue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.connection.CachingConnectionFactory;

@Configuration
public class JmsInfrastructureConfig {

	@Bean
	public ConnectionFactory connectionFactory() {
		ConnectionFactory cf = 
			new ActiveMQConnectionFactory("vm://embedded?broker.persistent=false&broker.useShutdownHook=false");
		return new CachingConnectionFactory(cf);
	}
	
	@Bean
	public ActiveMQQueue confirmationQueue() {
		return new ActiveMQQueue("confirmation.queue");
	}

	@Bean
	public ActiveMQQueue diningQueue() {
		return new ActiveMQQueue("dining.queue");
	}
}
