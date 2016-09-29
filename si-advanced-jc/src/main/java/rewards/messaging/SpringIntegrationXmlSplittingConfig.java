package rewards.messaging;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.dsl.channel.MessageChannels;
import org.springframework.integration.splitter.AbstractMessageSplitter;
import org.springframework.integration.xml.splitter.XPathMessageSplitter;
import org.springframework.messaging.MessageChannel;
import rewards.Dining;


@Configuration
public class SpringIntegrationXmlSplittingConfig {

	@Autowired MessageChannel xmlDinings;
	
	@Bean
	public MessageChannel mixedXmlDinings() {
		return MessageChannels.direct().get();
	}

	@Bean
	public IntegrationFlow xpathSplitterFlow()  {
		return IntegrationFlows
			.from(mixedXmlDinings())
			
			//	TODO 09: Use the split method to insert a new XPathMessageSplitter to this flow.
			//	The XPath expression to locate individual dining elements is "//dining"

			.channel(xmlDinings)
			.get();
	}
	
}
