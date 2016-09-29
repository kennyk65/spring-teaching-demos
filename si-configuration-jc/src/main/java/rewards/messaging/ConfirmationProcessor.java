package rewards.messaging;

import org.springframework.integration.annotation.Gateway;
import org.springframework.integration.annotation.MessagingGateway;
import rewards.RewardConfirmation;

@MessagingGateway
public interface ConfirmationProcessor {

	@Gateway(requestChannel = "confirmations")
	void process(RewardConfirmation confirmation);

}
