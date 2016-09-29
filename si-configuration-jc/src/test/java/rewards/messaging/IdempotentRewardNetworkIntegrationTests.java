package rewards.messaging;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.integration.core.MessagingTemplate;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import rewards.Dining;
import rewards.RewardConfirmation;
import rewards.RewardNetwork;
import rewards.internal.reward.RewardRepository;


@SpringApplicationConfiguration(classes=SystemTestSelector.class)
@RunWith(SpringJUnit4ClassRunner.class)
public class IdempotentRewardNetworkIntegrationTests {

	Dining dining = Dining.createDining("txId", "100.00", "1234123412341234", "1234567890");

	@Autowired RewardRepository rewardRepository;
	@Autowired RewardNetwork rewardNetwork;
	@Autowired MessagingTemplate template;

	@Test
	public void idempotence() throws Exception {
		RewardConfirmation confirmation = mock(RewardConfirmation.class);
		when(rewardNetwork.rewardAccountFor(dining)).thenReturn(confirmation);
		// we are relying on default null-returning behavior of rewardRepository mock here
		template.convertAndSend("dinings", dining);
		
		RewardConfirmation firstConfirmation = template.receiveAndConvert("confirmations", RewardConfirmation.class);
		
		// this time the repository will find an existing confirmation
		when(rewardRepository.findConfirmationFor(dining)).thenReturn(firstConfirmation);
		template.convertAndSend("dinings", dining);
		
		RewardConfirmation secondConfirmation = template.receiveAndConvert("confirmations", RewardConfirmation.class);
		
		//	TODO 02: Add an assert that ensures that the two confirmations are the same.
		//	Alter the verify to ensure that rewardAccountFor is invoked only once (instead of twice).
		//	Run the test.  Your assertion should pass, but the verify will not yet pass.
		//	TODO 21: Same as #02...

		verify(rewardNetwork, times(2)).rewardAccountFor(dining);
	}

}
