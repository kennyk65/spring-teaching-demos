package rewards.messaging;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.when;

import org.apache.log4j.Logger;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.integration.MessageRejectedException;
import org.springframework.integration.core.MessagingTemplate;
import org.springframework.integration.handler.LoggingHandler;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import rewards.Dining;
import rewards.RewardNetwork;

@SpringApplicationConfiguration(classes=SystemTestSelector.class)
@RunWith(SpringJUnit4ClassRunner.class)
public class InvalidDiningsIntegrationTests {

	Dining invalidDining = new Dining(null, null, null, null, null);

	@Autowired
	RewardNetwork rewardNetwork;
	@Autowired
	MessagingTemplate template;

	@Test
	public void invalidDiningShouldCauseExceptionMessage() throws Exception {
		// set up mock for when there's a mistake in the filter config,
		// the invalid dining would cause an exception then
		Logger.getLogger(LoggingHandler.class).warn(
				"Note: A warning is expected if " + getClass().getSimpleName() + " succeeds ...");

		when(rewardNetwork.rewardAccountFor(invalidDining)).thenThrow(new EmptyResultDataAccessException(1));

		
		// TODO 05: Use the messaging template to send the invalidDining to the dinings channel.
		// Run this test, initially it should fail.  Move onto the next step.
		// TODO 25: Same as #05...

		
		//	TODO 10: Use the messaging template 
		//	to verify that the errorTestChannel contains a MessageRejectedException.
		//	Run this test, it should pass.
		//	TODO 30: Same as #10...
	}
}
