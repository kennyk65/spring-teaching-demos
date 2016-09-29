package rewards.messaging.client;

import javax.jms.ConnectionFactory;
import javax.jms.Destination;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.IntegrationComponentScan;
import org.springframework.integration.config.EnableIntegration;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.dsl.jms.Jms;

@Configuration
@EnableIntegration
@IntegrationComponentScan
public class ClientConfig {

	@Autowired private Destination diningQueue;
	@Autowired private Destination confirmationQueue;
	@Autowired private ConnectionFactory connectionFactory;

	@Bean
	public RewardConfirmationLogger confirmationLogger() {
		return new RewardConfirmationLogger();
	}

	@Bean
	public IntegrationFlow clientFlow() {
	
		return IntegrationFlows
			.from("clientDinings")
			.handle(
				Jms.outboundGateway(connectionFactory)
				.requestDestination(diningQueue)
				.replyDestination(confirmationQueue)
				)
			.handle( "confirmationLogger", "log" )
			.get();
	}
	
	
	
}
