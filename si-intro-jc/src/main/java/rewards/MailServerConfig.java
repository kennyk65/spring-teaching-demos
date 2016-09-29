package rewards;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.subethamail.wiser.Wiser;

@Configuration
public class MailServerConfig {

	//	SMTP test server running on default port 25
	@Bean(initMethod="start")
	public Wiser wiser() {
		Wiser w = new Wiser();
		w.setPort(2525);
		return w;
	}
	
}
