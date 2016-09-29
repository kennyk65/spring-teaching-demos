package rewards.messaging;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.File;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.ImportResource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.integration.config.EnableIntegration;
import org.springframework.integration.core.MessagingTemplate;
import org.springframework.integration.dsl.channel.MessageChannels;
import org.springframework.messaging.MessageChannel;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

//@SpringApplicationConfiguration(locations={"/rewards/messaging/spring-integration-idempotent-receiver-config.xml",
//	                   "/rewards/messaging/spring-integration-infrastructure-config.xml",
//                       "/rewards/messaging/spring-integration-marshalling-config.xml",
//                       "/rewards/messaging/spring-integration-xml-splitting-config.xml",
//	                   "classpath:system-test-config.xml"})
@SpringApplicationConfiguration
@RunWith(SpringJUnit4ClassRunner.class)
public class RewardMessagingIntegrationTests {

	@Autowired MessagingTemplate template;

	// TODO 05: Remove @Ignore below.
	//			Examine the test logic.  
	//			It ensures a dining XML file sent in results in an XML confirmation out.
	//			Run this test, it should pass.
	// TODO 25: Comment out the empty @SpringApplicationConfiguration above, 
	//	and uncomment the alternate @SpringApplicationConfiguration which imports the XML configurations.  
	//	Then same as #05...
	
	@Test
	@Ignore
	public void sendSingleXmlDiningTwice() throws Exception {
		File xmlFile = new ClassPathResource("dining-sample.xml", getClass()).getFile();
		template.convertAndSend("xmlDinings", xmlFile);
		
		String payload = template.receiveAndConvert("xmlConfirmations", String.class);		
		assertTrue(payload.contains("<reward-confirmation"));
		
		template.convertAndSend("xmlDinings", xmlFile);
		assertEquals(payload, template.receiveAndConvert("xmlConfirmations", String.class));
	}
	
	// TODO 10: Remove @Ignore below
	//			Examine the test logic, this test case involves a single 
	//			XML document with multiple dining sub-elements.
	//			Run this test, it should pass.
	// TODO 30: Same as #10...

	@Test 
	@Ignore
	public void sendMultipleDinings() throws Exception {
		File xmlFile = new ClassPathResource("dinings-sample.xml", getClass()).getFile();
		template.convertAndSend("mixedXmlDinings", xmlFile);
		
		assertNotNull(template.receiveAndConvert("xmlConfirmations", Object.class));
		assertNotNull(template.receiveAndConvert("xmlConfirmations", Object.class));
	}


	@Test
	public void configOk() throws Exception {
		// testing the fixture
	}
	
	//	This configuration is used when running 100% Java Config:
	@Configuration
	@Import({
		SpringIntegrationIdempotentReceiverConfig.class,
		SpringIntegrationInfrastructureConfig.class,
		SpringIntegrationXmlSplittingConfig.class,
		SpringIntegrationMarshallingConfig.class,
	})
	@ImportResource("classpath:system-test-config.xml")
	@EnableIntegration
	public static class Config {
		@Bean
		public MessageChannel xmlDinings() {
			return MessageChannels.queue(10).get();
		}
	}
	
}
