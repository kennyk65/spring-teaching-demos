package rewards.messaging.client;

import org.springframework.integration.annotation.Gateway;
import org.springframework.integration.annotation.MessagingGateway;

import rewards.Dining;

/**
 * A batch processor for dining events.
 * 
 * Typical implementations would send notifications to the reward network in order to generate reward confirmations.
 */
@MessagingGateway
public interface DiningProcessor {

	/**
	 * Processes a batch of dining events.
	 * 
	 * @param dining a list of dining events
	 */
	@Gateway(requestChannel="clientDinings")
	void process(Dining dining);

}
