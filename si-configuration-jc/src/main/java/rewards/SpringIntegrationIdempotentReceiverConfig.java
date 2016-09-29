package rewards;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.IntegrationComponentScan;
import org.springframework.integration.channel.QueueChannel;
import org.springframework.integration.config.EnableIntegration;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.dsl.channel.MessageChannels;
import org.springframework.integration.handler.LoggingHandler;
import org.springframework.messaging.MessageChannel;
import rewards.internal.reward.RewardRepository;
import rewards.messaging.AlreadyRewardedConfirmer;
import rewards.messaging.ConfirmationProcessor;


//	TODO 01: Add 2 annotations to this class.
//	One should enable Spring Integration, 
//	the other should scan for Spring Integration specific components:

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

		//	TODO 06: Add a filter using Java for the filtering logic.
		//	The filter should reject any Dining object having null fields.
		//	Re-run the 'invalid dinings' test, it should pass. 
			.filter(Dining.class,
				d -> d.getAmount() != null && d.getCreditCardNumber() != null && d.getMerchantNumber() != null && d.getDate() != null,
				filterSpec -> filterSpec.throwExceptionOnRejection(true))

				
		//	TODO 07: Adjust the previous filter with a filterSpec, 
		//	setting it so it throws and exception on rejection.
		//	Re-run the last test, it should fail. 

				
		//	TODO 03: Add a handler (service activator) for the type <Dining>. 
		//	It should call the alreadyRewardedConfirmer's sendConfirmationForExistingDining() method.
		//	Re-run the 'idempotence' test, it should now pass. 
			.<Dining>handle((d,h) -> alreadyRewardedConfirmer.sendConfirmationForExistingDining(d))

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

	//	TODO 08: Change this direct channel to a queue channel.
	//	Re-run the 'invalid dinings' test. It should pass now. 	
	@Bean
	public MessageChannel dinings() {
		return MessageChannels.queue().get();
	}


	@Bean
	public QueueChannel confirmations() {
		return MessageChannels.queue(10).get();
	}

	
	//	TODO 12: Once all tests are running OK, uncomment this bean for nicer output.
	//	Re-run the last test and observe the different output.
	@Bean
	public MessageChannel errorChannel() {
		return MessageChannels.publishSubscribe()
							  .ignoreFailures(true)
							  .get();
	}

	@Bean
	public IntegrationFlow errorFlow() {
		LoggingHandler loggingHandler =  new LoggingHandler(LoggingHandler.Level.WARN.name());
		loggingHandler.setExpression("'filter rejected message with ' + payload.failedMessage.payload");
		loggingHandler.setLoggerName("logger");

		return IntegrationFlows.from("errorChannel")
							   .handle(loggingHandler)
							   .get();
	}

}
