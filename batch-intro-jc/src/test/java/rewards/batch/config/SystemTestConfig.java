package rewards.batch.config;

import javax.jms.ConnectionFactory;
import javax.management.MalformedObjectNameException;
import javax.sql.DataSource;

import org.apache.activemq.broker.jmx.QueueViewMBean;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.ImportResource;
import org.springframework.context.annotation.Lazy;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jmx.access.MBeanProxyFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;

import rewards.batch.QueuePopulator;
import rewards.batch.config.BatchJobConfig;

@Configuration
@Import({
	BatchJobConfig.class,
	JmsInfrastructureConfig.class
	})
@ImportResource({
	"classpath:db-config.xml",
	"classpath:jms-ccp-config.xml",
	})
public class SystemTestConfig {

	@Autowired ConnectionFactory connectionFactory;
	@Autowired DataSource dataSource;
	
	@Bean
	public QueuePopulator queuePopulator() {		
		JmsTemplate template = new JmsTemplate(connectionFactory);
		template.setDefaultDestinationName("confirmation.queue");
		return  new QueuePopulator(template);
	}
	
	//	Make the dining queue view available for checking in the test.
	@Bean
	public MBeanProxyFactoryBean diningQueueView() throws MalformedObjectNameException {
		MBeanProxyFactoryBean factory = new MBeanProxyFactoryBean();
		factory.setProxyInterface(QueueViewMBean.class);
		factory.setObjectName("org.apache.activemq:type=Broker,brokerName=embedded,destinationType=Queue,destinationName=dining.queue");
		return factory;
	}
	
	//	Helps with testing (autowired, injected in the test instance: 
	@Bean
	@Lazy
	public JobLauncherTestUtils testUtil() {
		return new JobLauncherTestUtils();
	}
	
	@Bean
	public PlatformTransactionManager transactionManager() {
		return new DataSourceTransactionManager(dataSource);
	}
	
}
