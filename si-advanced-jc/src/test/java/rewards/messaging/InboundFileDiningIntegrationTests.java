package rewards.messaging;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.ImportResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.dsl.file.Files;
import org.springframework.messaging.Message;
import org.springframework.messaging.PollableChannel;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.xml.transform.StringSource;
import org.springframework.xml.xpath.Jaxp13XPathTemplate;
import org.springframework.xml.xpath.XPathOperations;

//@SpringApplicationConfiguration(locations="/rewards/messaging/InboundFileDiningIntegrationTests-context.xml")
@SpringApplicationConfiguration
@RunWith(SpringJUnit4ClassRunner.class)
public class InboundFileDiningIntegrationTests {

	@Autowired PollableChannel xmlConfirmations;

	XPathOperations xpathTemplate = new Jaxp13XPathTemplate();

	// TODO 11: Remove @Ignore below.
	//	Run this test, it should initially fail.
	// TODO 31: Comment out the empty @SpringApplicationConfiguration above, 
	//	and uncomment the alternate @SpringApplicationConfiguration which imports the XML configurations.  
	//	Then same as #11...
	
	@Test
	@Ignore
	public void filesReceived() throws Exception {
		String xpath = "/reward-confirmation/@dining-transaction-id";
		int messageCount = 0;
		for(;;) {
			Message<?> receivedMessage = xmlConfirmations.receive(2500);
			if (receivedMessage == null) {
				break;
			}
			messageCount++;
			assertThat(receivedMessage.getPayload(), is(instanceOf(String.class)));
			String payload = (String) receivedMessage.getPayload();
			String diningTxId = xpathTemplate.evaluateAsString(xpath, new StringSource(payload));
			assertTrue(diningTxId.startsWith("universallyUniqueString"));
		}
		assertEquals(3, messageCount);
	}


	@Configuration
	@Import({
		SpringIntegrationIdempotentReceiverConfig.class,
		SpringIntegrationInfrastructureConfig.class,
		SpringIntegrationMarshallingConfig.class,
		SpringIntegrationXmlSplittingConfig.class,
	})
	@ImportResource({
		"classpath:system-test-config.xml",
	})
	public static class TestConfiguration {

		//		TODO 12:	Implement a new integration flow which finds 
		//		files in a directory and sends them to the mixedXmlDinings channel.
		//		The flow should begin with an inbound File Channel Adapter.
		//		Specify the directory as a relative path to the location of the 
		//		dining-sample.xml and dinings-sample.xml files.
		//		Use a pattern filter to include only these two files.
		//		Set preventDuplicates to keep the adapter from processing the same files repeatedly.
		//		The channel of the output messages should be mixedXmlDinings.
		//		Re-run the previous test, it should pass.
		@Bean
		public IntegrationFlow mixedXmlDiningsFlow() {
			return null;
		}
	}	
	
}
