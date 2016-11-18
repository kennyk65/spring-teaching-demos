package rewards.messaging;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.ImportResource;

import rewards.TestContextConfig;

@Configuration

//	TODO 00:  This lab can be completed using either Java Configuration or XML configuration.
//	To follow the JavaConfiguration steps, begin with step 01.
//	To follow the XML steps instead, comment out the @Import lines below, 
//	UNcomment the @ImportResource lines, and begin with step 21.

@Import({ 
	TestContextConfig.class,
})

//@ImportResource(locations={
//		"/rewards/messaging/test-context.xml",
//})


public class SystemTestSelector {

	//	Note that the only purpose of this class is to allow you to 
	//	easily select between XML Config or Java Config in this lab!

}
