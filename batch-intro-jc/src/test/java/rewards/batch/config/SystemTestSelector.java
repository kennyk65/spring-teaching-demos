package rewards.batch.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.ImportResource;

@Configuration

//	TODO 00:  This lab can be completed using either JavaConfiguration or XML configuration.
//	To follow the JavaConfiguration steps, begin with step 01.
//	To follow the XML steps instead, comment out the @Import statement below, 
//	UNcomment the @ImporResource statement, and begin with step 21.

@Import(SystemTestConfig.class)
//@ImportResource("classpath:system-test-config.xml")
public class SystemTestSelector {

	//	Note that the only purpose of this class is to allow you to 
	//	easily select between XML Config or Java Config in this lab!

}
