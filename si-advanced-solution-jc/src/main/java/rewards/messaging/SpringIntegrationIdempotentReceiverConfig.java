package rewards.messaging;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.dsl.channel.MessageChannels;
import org.springframework.messaging.MessageChannel;

import rewards.Dining;
import rewards.RewardNetwork;
import rewards.internal.reward.RewardRepository;

@Configuration
public class SpringIntegrationIdempotentReceiverConfig {

	@Autowired RewardRepository rewardRepository;
	@Autowired RewardNetwork rewardNetwork;
	
	
	@Bean
	public MessageChannel dinings() {
		return MessageChannels.direct().loadBalancer(null).get();  // null to disable load balancing?
	}
	
	@Bean
	public IntegrationFlow flow1() {
		return IntegrationFlows
			.from(dinings())
			.<Dining>handle( (d,h) -> rewardRepository.findConfirmationFor(d),
					spec -> spec.order(0).requiresReply(true) )
			.channel(confirmations())
			.get();
	}

	
	@Bean
	public IntegrationFlow flow2() {
		return IntegrationFlows
			.from(dinings())
			.<Dining>handle( (d,h) -> rewardNetwork.rewardAccountFor(d),
					spec -> spec.order(1) )
			.channel(confirmations())
			.get();
	}

	
	@Bean
	public MessageChannel confirmations() {
		return MessageChannels.queue(10).get();
	}
	

}
