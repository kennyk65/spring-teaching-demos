package rewards.messaging;

import org.springframework.util.Assert;

import rewards.Dining;
import rewards.RewardConfirmation;
import rewards.RewardNetwork;
import rewards.internal.reward.RewardRepository;

/**
 * Message Endpoint that checks if a Dining was already processed. If so it
 * short circuits the message flow and sends the existing confirmation directly
 * to the confirmationProcessor, bypassing the {@link RewardNetwork}.
 */
public class AlreadyRewardedConfirmer {

	private final RewardRepository rewardRepository;
	private final ConfirmationProcessor confirmationProcessor;

	public AlreadyRewardedConfirmer(RewardRepository rewardRepository, ConfirmationProcessor confirmationProcessor) {
		Assert.notNull(rewardRepository);
		Assert.notNull(confirmationProcessor);

		this.confirmationProcessor = confirmationProcessor;
		this.rewardRepository = rewardRepository;
	}

	/**
	 * Check if the passed in dining was already processed and if so resend the
	 * confirmation and return null. Return the passed in dining otherwise.
	 */
	public Dining sendConfirmationForExistingDining(Dining dining) {
		RewardConfirmation existingConfirmation = rewardRepository.findConfirmationFor(dining);
		if (existingConfirmation != null) {
			confirmationProcessor.process(existingConfirmation);
			return null; // prevent further processing of the dining
		}
		return dining;
	}
}
