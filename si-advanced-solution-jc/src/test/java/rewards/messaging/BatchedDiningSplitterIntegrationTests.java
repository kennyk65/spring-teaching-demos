package rewards.messaging;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.io.File;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.io.ClassPathResource;
import org.springframework.integration.config.EnableIntegration;
import org.springframework.integration.core.MessagingTemplate;
import org.springframework.integration.dsl.channel.MessageChannels;
import org.springframework.messaging.MessageChannel;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.xml.transform.StringSource;
import org.springframework.xml.xpath.Jaxp13XPathTemplate;
import org.springframework.xml.xpath.XPathOperations;

//@SpringApplicationConfiguration(locations="/rewards/messaging/BatchedDiningSplitterIntegrationTests-context.xml")
@SpringApplicationConfiguration
@RunWith(SpringJUnit4ClassRunner.class)
public class BatchedDiningSplitterIntegrationTests {

	XPathOperations xpathTemplate = new Jaxp13XPathTemplate();
	
	@Autowired MessagingTemplate template;

	@Test
	public void inboundSingleDiningXml() throws Exception {
		File diningFile = new ClassPathResource("dining-sample.xml", getClass()).getFile();
		template.convertAndSend("mixedXmlDinings", diningFile);

		Object receivedPayload = template.receive("xmlDinings").getPayload();
		assertThat(receivedPayload, is(instanceOf(String.class)));
		assertThat(xpathTemplate.evaluateAsString("/dining/@transaction-id",
				new StringSource((String) receivedPayload)), is("universallyUniqueString"));
	}

	@Test(timeout = 2000)
	public void inboundMultipleDiningXml() throws Exception {
		File diningsFile = new ClassPathResource("dinings-sample.xml", getClass()).getFile();
		
		// TODO 06:	Use the message template to send the diningsFile to the mixedXmlDinings channel.
		// 			Look at the test above for an example.
		// TODO 26: Comment out the @SpringApplicationConfiguration above, and uncomment the
		//	@SpringApplicationConfiguration which imports the XML configuration.  
		//	Then same as #06...
		template.convertAndSend("mixedXmlDinings", diningsFile);

		// TODO 07:	Use the message template to receive and convert from xmlDinings,
		//			assign the result to receivedPayload.
		// TODO 27: Same as #07...
		String receivedPayload = template.receiveAndConvert("xmlDinings", String.class);

		// TODO 08:	Assert that the received payload has a dining root element with the right transaction id. 
		//			Look at dinings-sample.xml to find the transaction id, and use xpathTemplate to query for it.
		//			Run this test, it will initially fail.
		// TODO 28: Same as #08...
		assertThat(xpathTemplate.evaluateAsString("/dining/@transaction-id", 
				new StringSource(receivedPayload)), is("universallyUniqueString1"));
		
		receivedPayload = template.receiveAndConvert("xmlDinings", String.class);
		assertThat(xpathTemplate.evaluateAsString("/dining/@transaction-id", 
				new StringSource(receivedPayload)), is("universallyUniqueString2"));
	}

	//	This configuration is used when running 100% Java Config:
	@Configuration
	@Import({
		SpringIntegrationInfrastructureConfig.class,
		SpringIntegrationXmlSplittingConfig.class,
	})
	@EnableIntegration
	public static class Config {
		@Bean
		public MessageChannel xmlDinings() {
			return MessageChannels.queue(10).get();
		}
	}
}
