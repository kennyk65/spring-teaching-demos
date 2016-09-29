package rewards.batch.config;


import javax.sql.DataSource;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.JdbcPagingItemReader;
import org.springframework.batch.item.database.support.SqlPagingQueryProviderFactoryBean;
import org.springframework.batch.item.jms.JmsItemReader;
import org.springframework.batch.item.jms.JmsItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.jms.core.JmsTemplate;

import rewards.batch.Confirmation;
import rewards.batch.ConfirmationPreparedStatementPreparer;
import rewards.batch.Dining;
import rewards.batch.DiningMapper;

@Configuration
@PropertySource("classpath:batch.properties")
@EnableBatchProcessing
public class BatchJobConfig {

	@Autowired DataSource dataSource;
	@Autowired JmsTemplate receivingJmsTemplate;
	@Autowired JmsTemplate sendingJmsTemplate;
	@Autowired StepBuilderFactory stepBuilderFactory;
	@Value("${chunk.size}") int chunkSize;
	
	
	
	//	TODO 05a: Configure the first step of the batch job.
	//	Use the stepBuilderFactory.  Set the name of the step to "processConfirmationsStep"
	//	Set the chunk size using the chunkSize variable defined above.  Parameterize 
	//	the call to this method to preserve type safety (<Confirmation,Confirmation>).
	//	Set the step's reader to the “confirmationReader” you defined earlier.  
	//	Set the writer to the “confirmationUpdater”.  
	//	Set readerIsTransactionalQueue; this will be explained in the next module.
	@Bean
	public Step processConfirmationsStep() {
		return null;
	}
	

	//	TODO 11: Define the second step of the batch job.  
	//	Use the stepBuilderFactory.  Name the step "sendUnprocessedDiningsStep".
	//	Set the chunk size using the chunkSize variable defined above.  Parameterize 
	//	the call to this method to preserve type safety (<Dining,Dining>).
	//	Set the step's reader to the “unconfirmedDiningsReader” you modified earlier.  
	//	Set the writer to the “requestSender”.  
	//	Modify the job definition (above) to included the second step.	
	@Bean
	public Step sendUnprocessedDiningsStep() throws Exception {
		return null;
	}
	
	//	TODO 05b: Define the Job.  Use a JobBuilderFactory to help you build it.
	//	Name the job "resendUnprocessedDiningsJob".
	//	Set the first step (flow) to use the processConfirmationsStep().
	//	For now, the job will have one step, we will add a second step later.
	@Bean
	public Job resendUnprocessedDiningsJob(JobBuilderFactory factory) throws Exception {
		return null;
	}
	
	//	TODO 01: Define a bean named "confirmationReader" of type JmsItemReader<Confirmation>.
	//	Inject it with the receivingJmsTemplate, which is already setup to read from the "confirmation.queue".

	
	//	TODO 02: Define a bean named "confirmationUpdater" of type JdbcBatchItemWriter<Confirmation>.  
	//	Set its dataSource property.
	//	Set its SQL statement to "update T_DINING set CONFIRMED=1 where ID=?".
	//	To control how the parameter is set in the batched update, set the itemPreparedStatementSetter to 
	//	reference the "confirmationPreparedStatementPreparer" bean defined below.  
	//	See the next step to understand what this bean is doing. 


	//	TODO 03: The last step defined a writer to perform efficient batched updates on the database.
	//	Batched updates use prepared statements, and prepared statements need to have their parameters
	//	set for each statement in the batch.  Examine the SQL statement used in the last step, and
	//	compare it with the code in the class defined here.  When you feel you understand how the
	//	Confirmation's ID gets set on the update statement, move on to the next step.  
	@Bean
	public ConfirmationPreparedStatementPreparer confirmationPreparedStatementPreparer() {
		return new ConfirmationPreparedStatementPreparer();
	}
	
	@Bean
	@Lazy
	public JdbcPagingItemReader<Dining> unconfirmedDiningsReader() throws Exception {
		JdbcPagingItemReader<Dining> reader = new JdbcPagingItemReader<>();
		reader.setDataSource(dataSource);
		reader.setFetchSize(chunkSize);
		reader.setPageSize(chunkSize);

		//	TODO 07:  Inject a properly configured SqlPagingQueryProviderFactoryBean 
		//	for the queryProvider property as an inner bean.
		//	Set the properties controlling the SQL statement based on the db-schema.sql file.
		//	For more help see the detailed instructions.  
		
		//	TODO 08: inject a DiningMapper for the rowMapper property as an inner bean 
		
		return reader;
	}
	
	//	TODO 10: create a requestSender bean definition of type JmsItemWriter<Dining>.
	//	Inject it with the sendingJmsTemplate, which is already setup to read from the "dining.queue".
	

	@Bean
	public static PropertySourcesPlaceholderConfigurer pspc() {
		return new PropertySourcesPlaceholderConfigurer();
	}
}
