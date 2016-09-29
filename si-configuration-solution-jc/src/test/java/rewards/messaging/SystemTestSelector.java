package rewards.messaging;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.ImportResource;

import rewards.TestContextConfig;

@Configuration

//To run the solution using XML configuration, comment out the @Import lines below, 
//UNcomment the @ImporResource lines, and run the DiningProcessorTests

@Import({ 
	TestContextConfig.class,
})

@ImportResource(locations={
//		"/rewards/messaging/test-context.xml",
})


public class SystemTestSelector {

	//	Note that the only purpose of this class is to allow you to 
	//	easily select between XML Config or Java Config in this lab!

}
