package rewards;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.IntegrationComponentScan;
import org.springframework.integration.channel.QueueChannel;
import org.springframework.integration.config.EnableIntegration;
import org.springframework.integration.config.ServiceActivatorFactoryBean;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.dsl.channel.MessageChannels;
import org.springframework.integration.dsl.support.GenericHandler;
import org.springframework.integration.handler.LoggingHandler;
import org.springframework.integration.handler.ServiceActivatingHandler;
import org.springframework.messaging.MessageChannel;

import rewards.internal.reward.RewardRepository;
import rewards.messaging.AlreadyRewardedConfirmer;
import rewards.messaging.ConfirmationProcessor;


@Configuration		
@EnableIntegration			// Enabling Spring Integration infrastructure
@IntegrationComponentScan	// Discovering @MessagingGateway interfaces
public class SpringIntegrationIdempotentReceiverConfig {

	@Autowired AlreadyRewardedConfirmer alreadyRewardedConfirmer;
	
	@Autowired RewardNetwork rewardNetwork;
	
	@Bean
	public IntegrationFlow diningsFlow() {

		// This integration flow uses the 'dinings' channel as the input. 
		return IntegrationFlows
			.from(dinings())

		//	Filter out any Dining object having null fields.
		//	Throw an exception on rejection.
			.filter(Dining.class,
				d -> d.getAmount() != null && d.getCreditCardNumber() != null && d.getMerchantNumber() != null && d.getDate() != null,
				filterSpec -> filterSpec.throwExceptionOnRejection(true))

		//	Call the alreadyRewardedConfirmer's sendConfirmationForExistingDining() method.
			.<Dining>handle( 
				(dining,headers) -> alreadyRewardedConfirmer.sendConfirmationForExistingDining(dining) )

		//	Process dining messages through the rewardNetwork, 
		//	sending output to the confirmations channel:	
			.<Dining>handle((d,h) -> rewardNetwork.rewardAccountFor(d))				
			.channel(confirmations())
			.get();
	}

	@Bean
	public AlreadyRewardedConfirmer alreadyRewardedConfirmer(RewardRepository rewardRepository, ConfirmationProcessor confirmationProcessor) {
		return new AlreadyRewardedConfirmer(rewardRepository, confirmationProcessor);
	}

	@Bean
	public MessageChannel dinings() {
		return MessageChannels.queue().get();
	}


	@Bean
	public QueueChannel confirmations() {
		return MessageChannels.queue(10).get();
	}

	
	//	Produce more pleasant error output by ignoring filter failures:
	@Bean
	public MessageChannel errorChannel() {
		return MessageChannels
			.publishSubscribe()
			.ignoreFailures(true)
			.get();
	}

	@Bean
	public IntegrationFlow errorFlow() {
		LoggingHandler handler =  new LoggingHandler(LoggingHandler.Level.WARN.name());
		handler.setExpression("'filter rejected message with ' + payload.failedMessage.payload");
		handler.setLoggerName("logger");

		return IntegrationFlows
			.from("errorChannel")
			.handle(handler)
			.get();
	}

}
