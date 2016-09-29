package rewards.batch.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.ImportResource;

@Configuration
@Import(SystemTestConfig.class)
//@ImportResource("classpath:system-test-config.xml")
public class SystemTestSelector {

	//	Note that the only purpose of this class is to allow you to 
	//	easily select between XML Config or Java Config in this lab!

}
