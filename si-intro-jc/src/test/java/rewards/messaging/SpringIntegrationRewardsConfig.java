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
//@EnableIntegration
//@IntegrationComponentScan
public class SpringIntegrationRewardsConfig {

	@Autowired ConnectionFactory connectionFactory;
	@Autowired RewardNetwork rewardNetwork;
	@Autowired AccountRepository accountRepository;
	
	@Bean
	public IntegrationFlow mainFlow() {
	
			//	TODO 02: Alter this definition of the SI flow.  Remove the "return null;"
			//	Use the IntegrationFlows.from() method and the 
			//	Jms.inboundGateway() method to define an inbound JMS gateway.
			//	Set the destination and defaultReplyQueueName based on values used in JmsRewardsConfig.
			//	Follow the gateway with a reference to the dinings() channel defined below.
			//	Finish the fluent interface with a call to .get() at the end of this method.
			//	Return to SystemTestSelector and remove the @Import of JmsRewardsConfig.class.
			//	Be careful to do the following steps in numerical order.
		return null;

				
			//	TODO 04: Define a handler (service activator) for the type <Dinings>.  
			//	It should call the rewardNetwork for messages on the 'dinings' channel.  
			//	Do not specify a reply channel, the gateway's temporary reply channel will be used automatically.
			//	Re-run DiningProcessorTests.  It should still pass.
			//	Be careful to do the following steps in numerical order.

			
			//	TODO 09: Extend the flow with another handler (service-activator).  
			//	Messages from the 'confirmations' channel will be used to  
			//	call a method on the rewardMailMessageCreator bean.
			//	Handler output should be placed on an output channel called "mails".

			
			//	TODO 10: Extend the flow with another handler (service-activator).
			//	Messages from the 'mails' channel will be
			//	sent to Mail.outboundAdapter("localhost").  
			//	Set the port to 2525.

	}
	
	//	TODO 03: Complete the definition of the dinings channel.
	//	Replace the "return null" below with a definition for a direct 
	//	channel named "dinings".  Use the MessageChannels factory class.
	@Bean
	public MessageChannel dinings() {
		return null;
	}

	
	@Bean
	public MessageChannel mails() {
		return MessageChannels.direct().get();
	}
	
	
	//	TODO 05: Defined a WireTap bean named wiretap.  
	//	The wiretap should send messages to the loggingChannel() defined below.
	//	Alter the dinings() channel to add this wiretap as an interceptor.

	
	
	@Bean
	public MessageChannel loggingChannel() {
		return MessageChannels.direct().get();
	}
	
	//	TODO 06: Alter this bean definition.
	//	Use IntegrationFlows to define a short flow to handle the logging output from the wiretap.
	//	The flow should accept input "from" the loggingChannel.
	//	It should use a handle() method to reference the logger bean defined below.
	//	Re-run DiningProcessorTests.  It should still pass, 
	//	plus you should see output in the console.
	@Bean IntegrationFlow logFlow() {
	     return null;
	}	
	
	@Bean
	public MessageHandler logger() {
	     return  new LoggingHandler(LoggingHandler.Level.INFO.name());
	}	

	
	//	TODO 07: Modify the definition of this channel.
	//	Define a publish-subscribe channel called 'confirmations' to act as a reply channel.
	//	Modify the inbound gateway to use this channel as the reply-channel.
	//	Alter the main flow to specify this channel immediately after the handle (service activator).
	//	Re-run DiningProcessorTests.  It should still pass.
	@Bean
	public MessageChannel confirmations() {
		return null;
	}
	
	
	//	TODO 08: Define a Spring bean with id = 'rewardMailMessageCreator' 
	//	and class = rewards.RewardMailMessageCreator.  Inject it with an accountRepository.
	
}
