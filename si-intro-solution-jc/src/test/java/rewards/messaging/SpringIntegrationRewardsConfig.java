package rewards.messaging;

import javax.jms.ConnectionFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.IntegrationComponentScan;
import org.springframework.integration.channel.interceptor.WireTap;
import org.springframework.integration.config.EnableIntegration;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.dsl.channel.MessageChannels;
import org.springframework.integration.dsl.jms.Jms;
import org.springframework.integration.dsl.mail.Mail;
import org.springframework.integration.handler.LoggingHandler;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHandler;

import rewards.Dining;
import rewards.RewardConfirmation;
import rewards.RewardMailMessageCreator;
import rewards.RewardNetwork;
import rewards.internal.account.AccountRepository;

@Configuration
@EnableIntegration
@IntegrationComponentScan
public class SpringIntegrationRewardsConfig {

	@Autowired ConnectionFactory connectionFactory;
	@Autowired RewardNetwork rewardNetwork;
	@Autowired AccountRepository accountRepository;
	
	@Bean
	public IntegrationFlow mainFlow() {
	
		return IntegrationFlows
				
			//	Begin the flow with an inbound JMS gateway.
			//	Obtain JMS messages from "rewards.queue.dining" 
			//	and send these out through the dinings channel.
			//	Confirmations returned from the confirmations channel 
			//	should be sent out to the "rewards.queue.confirmation" queue.
			.from(
				Jms.inboundGateway(connectionFactory)
				.destination("rewards.queue.dining")
				.defaultReplyQueueName("rewards.queue.confirmation")
				.replyChannel(confirmations())
				)
			.channel(dinings())	
			
			//	Messages on the dinings channel should be used to call
			//	the main reward logic.  Resulting confirmations should be placed on 
			//	the outbound confirmations channel:
			.<Dining>handle( 
				(dining,headers) -> rewardNetwork.rewardAccountFor(dining))		
			.channel(confirmations())
			
			//	Messages on the confirmations channel should be used to call
			//	the createMail method on the rewardMailMessageCreator bean.
			//	Formatted output should be placed on the mails channel:
			.<RewardConfirmation>handle(
				(confirmation,headers) -> rewardMailMessageCreator().createMail(confirmation))
			.channel(mails())
			
			//	Messages on the mails channel 
			//	should be sent to the outbound SMTP adapter:
			.handle(Mail.outboundAdapter("localhost").port(2525))	

			.get();	// Make it so...
	}
	
	//	The dinings direct channel 
	//	receives messages from the inbound JMS gateway
	//	and sends them to the main dining processing logic.
	//	It is equiped with a wiretap to log all input messages.
	@Bean
	public MessageChannel dinings() {
		return MessageChannels.direct().interceptor(wiretap()).get();
	}

	
	//	The mails direct channel
	//	receives messages from the reward message creator
	//	and sends to the outbound mail adapter.
	@Bean
	public MessageChannel mails() {
		return MessageChannels.direct().get();
	}
	
	
	//	The wiretap sends messages to the loggingChannel defined below.
	@Bean
	public WireTap wiretap() {
		return new WireTap(loggingChannel());
	}
	
	@Bean
	public MessageChannel loggingChannel() {
		return MessageChannels.direct().get();
	}
	
	//	Logging flow:
	//	messages received on the loggingChannel 
	//	are sent to the logger outbound logging adapter.
	@Bean IntegrationFlow logFlow() {
	     return IntegrationFlows
	    		 .from(loggingChannel())
	    		 .handle(logger())
	    		 .get();
	}	
	
	@Bean
	public MessageHandler logger() {
	     return  new LoggingHandler(LoggingHandler.Level.INFO.name());
	}	

	
	//	The confirmations publish-subscribe channel
	//	receives messages from the main dining processor handler (service activator)
	//	and sends a copy of each message to the mail message creator
	//	and the JMS inbound gateway.
	@Bean
	public MessageChannel confirmations() {
		return MessageChannels.publishSubscribe().get();
	}
	
	
	//	Turns RewardConfirmations into formatted SMTP compatible email content.
	@Bean
	public RewardMailMessageCreator rewardMailMessageCreator() {
		return new RewardMailMessageCreator(accountRepository);
	}
	
}
