package rewards.messaging;

import javax.jms.ConnectionFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.messaging.handler.annotation.SendTo;

import rewards.Dining;
import rewards.RewardConfirmation;
import rewards.RewardNetwork;

@Configuration
@EnableJms
public class JmsRewardsConfig {

	@Autowired private RewardNetwork rewardNetwork;
	@Autowired private ConnectionFactory connectionFactory;
	
	@Bean 
	public DefaultJmsListenerContainerFactory jmsListenerContainerFactory () {
		DefaultJmsListenerContainerFactory cf = new DefaultJmsListenerContainerFactory( );
		cf.setConnectionFactory(connectionFactory);
		return cf;
	}
	
	//	Whenever a message arrives on the rewards.queue.dining queue,
	//	call the rewardAccountFor method on the rewardNetwork, 
	//	passing the message payload (which is a Dining).
	//	Send the resulting RewardConfirmation out in a message to the rewards.queue.confirmation queue:
	@JmsListener( destination="rewards.queue.dining")
	@SendTo("rewards.queue.confirmation")
	public RewardConfirmation rewardAccountFor(Dining dining) {
		return rewardNetwork.rewardAccountFor(dining);
	}
	
	
}
