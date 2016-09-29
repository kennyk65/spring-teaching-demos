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

//	TODO 00:  This lab can be completed using either Java Configuration or XML configuration.
//	To follow the JavaConfiguration steps, begin with step 01.
//	To follow the XML steps instead, comment out the @Import lines below, 
//	UNcomment the @ImporResource lines, and begin with step 21.

@Import({ 
	ClientConfig.class, 
	JmsInfrastructureConfig.class,
	JmsRewardsConfig.class,
	SpringIntegrationRewardsConfig.class,
	SystemTestConfig.class,
})

@ImportResource(locations={
//		"/rewards/messaging/client/client-config.xml",
//		"classpath*:rewards/messaging/*-config.xml",
//		"/system-test-config.xml" 
})

//TODO 11: add the MailServerConfig class above
//TODO 31: add the rewards/mail-server-config.xml file above

public class SystemTestSelector {

	//	Note that the only purpose of this class is to allow you to 
	//	easily select between XML Config or Java Config in this lab!

}
