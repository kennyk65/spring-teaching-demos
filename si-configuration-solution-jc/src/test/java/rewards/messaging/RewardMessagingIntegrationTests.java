package rewards.messaging;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

import org.apache.log4j.Logger;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.integration.MessageRejectedException;
import org.springframework.integration.channel.PublishSubscribeChannel;
import org.springframework.integration.core.MessagingTemplate;
import org.springframework.integration.handler.LoggingHandler;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHandler;
import org.springframework.messaging.MessagingException;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import rewards.Dining;
import rewards.RewardConfirmation;
import rewards.SpringIntegrationIdempotentReceiverConfig;
import rewards.SpringIntegrationInfraConfig;
import rewards.SystemTestConfig;

//@SpringApplicationConfiguration(locations={ 
//	"/rewards/messaging/spring-integration-idempotent-receiver-config.xml",
//	"/rewards/messaging/spring-integration-infrastructure-config.xml",
//	"classpath:system-test-config.xml" })

@SpringApplicationConfiguration(classes={
	SpringIntegrationIdempotentReceiverConfig.class,
	SpringIntegrationInfraConfig.class,
	SystemTestConfig.class,
})

@RunWith(SpringJUnit4ClassRunner.class)
public class RewardMessagingIntegrationTests {

	@Autowired
	MessagingTemplate template;
	@Autowired
	PublishSubscribeChannel errorChannel;

	@Test
	public void sendDiningTwice() throws Exception {
		final String txId = "tx-id";
		Dining dining = Dining.createDining(txId, "100.00", "1234123412341234",
				"1234567890");
		template.convertAndSend("dinings", dining);

		RewardConfirmation payload = template.receiveAndConvert("confirmations", RewardConfirmation.class);
		assertEquals(txId, payload.getDiningTransactionId());

		template.convertAndSend("dinings", dining);
		assertEquals(payload, template.receiveAndConvert("confirmations", RewardConfirmation.class));
	}

	@Test
	public void sendInvalidDining() throws Exception {
		ErrorHandler handler = new ErrorHandler();
		errorChannel.subscribe(handler);

		Logger.getLogger(LoggingHandler.class).warn(
				"Note: A warning is expected if " + getClass().getSimpleName()
						+ " succeeds ...");

		Dining invalidDining = new Dining(null, null, null, null, null);
		template.convertAndSend("dinings", invalidDining);

		Thread.sleep(1000);
		assertNotNull("No error message received", handler.msg);
		assertThat(handler.msg.getPayload(), is(instanceOf(MessageRejectedException.class)));
	}

	/**
	 * Handles any error message by storing it away. Allows us to test that a
	 * {@link MessageRejectedException} has been generated as expected.
	 */
	static class ErrorHandler implements MessageHandler {
		volatile Message<?> msg;

		public void handleMessage(Message<?> message) throws MessagingException {
			msg = message;
		}
	}

}
