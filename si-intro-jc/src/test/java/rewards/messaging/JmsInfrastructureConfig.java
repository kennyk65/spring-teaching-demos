package rewards.messaging;

import javax.jms.ConnectionFactory;
import javax.jms.Queue;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.command.ActiveMQQueue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JmsInfrastructureConfig {

	@Bean
	public ConnectionFactory connectionFactory() {
		return new ActiveMQConnectionFactory("vm://embedded?broker.persistent=false");
	}

	@Bean
	public Queue diningQueue() {
		return new ActiveMQQueue("rewards.queue.dining");
	}

	@Bean
	public Queue confirmationQueue() {
		return new ActiveMQQueue("rewards.queue.confirmation");
	}

}
