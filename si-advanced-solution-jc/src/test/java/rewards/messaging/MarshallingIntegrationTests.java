package rewards.messaging;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.File;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.io.ClassPathResource;
import org.springframework.integration.core.MessagingTemplate;
import org.springframework.integration.dsl.channel.MessageChannels;
import org.springframework.messaging.MessageChannel;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import rewards.Dining;
import rewards.RewardConfirmation;

@SpringApplicationConfiguration(locations="/rewards/messaging/MarshallingIntegrationTests-context.xml")
//@SpringApplicationConfiguration
@RunWith(SpringJUnit4ClassRunner.class)
public class MarshallingIntegrationTests {

	@Autowired MessagingTemplate template;
	
	@Test
	public void inboundDiningXml() throws Exception {
		File xmlFile = new ClassPathResource("dining-sample.xml", getClass()).getFile();
		
		//	Send XML file input to the xmlDinings channel...
		template.convertAndSend("xmlDinings", xmlFile);
		
		//	...and expect to get an unmarshalled Dinings object on the dinings channel:
		Dining receivedPayload = template.receiveAndConvert("dinings", Dining.class);
		
		// TODO 01: Assert that the received Dining contains values originally found in the dining-sample.xml.
		//          Remove annotation @Ignore on test
		//          Run this test, it should initially fail.
		// TODO 21: Replace the @SpringApplicationConfiguration annotation on this class with
		//	the one that imports the XML-based configuration.  Then perform the same activities as #01...
		assertThat(receivedPayload.getTransactionId(), is("universallyUniqueString"));
	}

	@Test
	public void outboundConfirmation() throws Exception {
		RewardConfirmation confirmation = mock(RewardConfirmation.class);
		when(confirmation.getDiningTransactionId()).thenReturn("UUID");
		
		template.convertAndSend("confirmations", confirmation);
		
		String receivedPayload = template.receiveAndConvert("xmlConfirmations", String.class);
		
		// TODO 03: Add assertions on the returned String to ensure it contains an XML confirmation.
		//          (For example, check that the String contains a "dining-transaction-id" sub-string.)
		//          Remove annotation @Ignore on test
		//          Run this test, it should initially fail.
		// TODO 23: Same as #03...
		assertTrue(receivedPayload.contains("dining-transaction-id=\"UUID\""));
	}

	
	@Configuration
	@Import({
		SpringIntegrationInfrastructureConfig.class,
		SpringIntegrationMarshallingConfig.class
	})
	public static class TestConfiguration {

		@Bean
		public MessageChannel dinings() {
			return MessageChannels.queue(10).get();
		}

		@Bean
		public MessageChannel confirmations() {
			return MessageChannels.direct().get();
		}
	}
	
}
