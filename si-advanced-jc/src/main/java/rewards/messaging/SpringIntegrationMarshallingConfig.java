package rewards.messaging;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.IntegrationComponentScan;
import org.springframework.integration.config.EnableIntegration;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.dsl.channel.MessageChannels;
import org.springframework.integration.dsl.support.Transformers;
import org.springframework.integration.xml.transformer.ResultToStringTransformer;
import org.springframework.messaging.MessageChannel;
import org.springframework.oxm.Marshaller;
import org.springframework.oxm.Unmarshaller;

@Configuration
@EnableIntegration
public class SpringIntegrationMarshallingConfig {

	@Bean
	public MessageChannel xmlDinings() {
		return MessageChannels.direct().get();
	}
	
	
	@Bean
	public IntegrationFlow unmarshallingTransformerFlow() {
		return IntegrationFlows
			.from(xmlDinings())

			//	TODO 02:	Add a transformer between the xmlDinings and dinings channels.
			//	The transformer should unmarshall the XML input into Dining objects.
			//	The existing diningRequestUnmarshaller can be used for unmarshalling.
			//	Re-run the previous test, it should pass. 
			
			.channel("dinings")
			.get();
	}
	
	@Bean
	public Unmarshaller diningRequestUnmarshaller() {
		return new DiningRequestUnmarshaller();
	}

	@Bean
	public IntegrationFlow marshallingFlow() {
		return IntegrationFlows
			.from("confirmations")

			// TODO 04:	Add a transformer between the confirmations input channel 
			//	and the xmlConfirmations output channel.
			//	The transformer should marshall RewardConfirmation objects into XML documents.
			//	The existing rewardConfirmationMarshaller can be used as the marshaller.
			//	Use the resultToStringTransformer to ensure the transformer's output is a String.
			//	Re-run the previous test, it should pass. 
			
			.channel(xmlConfirmations())
			.get();
	}
	
	@Bean
	public Marshaller rewardConfirmationMarshaller() {
		return new RewardConfirmationMarshaller();
	}
	
	@Bean
	public ResultToStringTransformer resultToStringTransformer() {
		return new ResultToStringTransformer();
	}

	@Bean
	public MessageChannel xmlConfirmations(){
		return MessageChannels.queue(10).get();
	}
	
}
