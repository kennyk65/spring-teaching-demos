package rewards.messaging.client;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.ImportResource;

import rewards.MailServerConfig;
import rewards.messaging.JmsInfrastructureConfig;
import rewards.messaging.JmsRewardsConfig;
import rewards.messaging.SpringIntegrationRewardsConfig;
import rewards.messaging.SystemTestConfig;

@Configuration

//	To run the solution using XML configuration, comment out the @Import lines below, 
//	UNcomment the @ImporResource lines, and run the DiningProcessorTests

@Import({ 
	ClientConfig.class, 
	JmsInfrastructureConfig.class,
	JmsRewardsConfig.class,
	SpringIntegrationRewardsConfig.class,
	SystemTestConfig.class,
	MailServerConfig.class	
})

@ImportResource(locations={
//		"/rewards/messaging/client/client-config.xml",
//		"classpath*:rewards/messaging/jms-infrastructure-config.xml",
//		"classpath*:rewards/messaging/jms-rewards-config.xml",
//		"classpath*:rewards/messaging/spring-integration-rewards-config.xml",
//		"/rewards/mail-server-config.xml",
//		"/system-test-config.xml" 
})


public class SystemTestSelector {

	//	Note that the only purpose of this class is to allow you to 
	//	easily select between XML Config or Java Config in this lab!

}
