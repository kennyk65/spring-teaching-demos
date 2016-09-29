package rewards.messaging;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.config.EnableIntegration;
import org.springframework.integration.core.MessagingTemplate;
import org.springframework.integration.dsl.core.Pollers;
import org.springframework.integration.scheduling.PollerMetadata;

@Configuration
public class SpringIntegrationInfrastructureConfig {

	@Bean(name = PollerMetadata.DEFAULT_POLLER)
	public PollerMetadata defaultPoller() {
		return Pollers.fixedDelay(500).get();
	}

	@Bean
	public MessagingTemplate messagingTemplate() {
		MessagingTemplate mt = new MessagingTemplate();
		mt.setReceiveTimeout(1000);
		return mt;
	}
	
}
