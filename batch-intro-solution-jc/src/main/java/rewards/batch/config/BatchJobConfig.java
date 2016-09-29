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
	
	
	//	Step 1.
	@Bean
	public Step processConfirmationsStep() {
		return stepBuilderFactory
			.get("processConfirmationsStep")				//	Please create a StepBuilder.
			.<Confirmation,Confirmation>chunk(chunkSize)	//	Build a chunk-processing step
			.reader(confirmationReader())					//	...using this reader...
			.writer(confirmationUpdater())					//	...and this writer.
			.readerIsTransactionalQueue()					//	reader is transactional.
			.build();										//	Make it so.
	}
	

	//	Step 2.	
	@Bean
	public Step sendUnprocessedDiningsStep() throws Exception {
		return stepBuilderFactory
			.get("sendUnprocessedDiningsStep")	//	Create a step builder.
			.<Dining,Dining>chunk(chunkSize)	//	Build a chunk-based step
			.reader(unconfirmedDiningsReader())	//	using this reader...
			.writer(requestSender())			//	...and this writer.
			.build();							//	Make it so.
	}
	
	//	Job
	@Bean
	public Job resendUnprocessedDiningsJob(JobBuilderFactory factory) throws Exception {
		return factory 
			.get("resendUnprocessedDiningsJob")
			.flow(processConfirmationsStep())	//	First step: get confirmations, update database.
			.next(sendUnprocessedDiningsStep())	//	Second step: read database, create dinings.
			.end()
			.build();
	}
	
	//	Used by Step 1, this JmsItemReader reads confirmations from the 
	//	confirmations queue using the JmsTemplate defined for the purpose:
	@Bean
	public JmsItemReader<Confirmation> confirmationReader() {
		JmsItemReader<Confirmation> reader = new JmsItemReader<>();
		reader.setJmsTemplate(receivingJmsTemplate);
		return reader;
	}
	
	//	Used by Step 1, this writer updates the T_DINING table
	//	for every confirmation read.
	@Bean
	public JdbcBatchItemWriter<Confirmation> confirmationUpdater() {
		assert ( dataSource != null);
		JdbcBatchItemWriter<Confirmation> writer = new JdbcBatchItemWriter<>();
		writer.setDataSource(dataSource);
		writer.setSql("update T_DINING set CONFIRMED=1 where ID=?");
		writer.setItemPreparedStatementSetter(confirmationPreparedStatementPreparer());
		return writer;
	}

	//	The last step defined a writer to perform efficient batched updates on the database.
	//	Batched updates use prepared statements, and prepared statements need to have their parameters
	//	set for each statement in the batch.  Examine the SQL statement used in the last step, and
	//	compare it with the code in the class defined here.  When you feel you understand how the
	//	Confirmation's ID gets set on the update statement, move on to the next step.  
	@Bean
	public ConfirmationPreparedStatementPreparer confirmationPreparedStatementPreparer() {
		return new ConfirmationPreparedStatementPreparer();
	}
	
	//	Used by step 2.  This reads unconfirmed Dinings from the DB:
	@Bean
	public JdbcPagingItemReader<Dining> unconfirmedDiningsReader() throws Exception {
		JdbcPagingItemReader<Dining> reader = new JdbcPagingItemReader<>();
		reader.setDataSource(dataSource);
		reader.setFetchSize(chunkSize);
		reader.setPageSize(chunkSize);

		SqlPagingQueryProviderFactoryBean factory = new SqlPagingQueryProviderFactoryBean();
		factory.setDataSource(dataSource);
		factory.setSelectClause(	"select *");
		factory.setFromClause(		"from T_DINING");
		factory.setWhereClause(		"where confirmed = 0");
		factory.setSortKey("ID");

		reader.setQueryProvider(factory.getObject());
		reader.setRowMapper(new DiningMapper() );
		return reader;
	}
	
	//	Used by step 2, write Dining objects to JMS queue.
	@Bean
	public JmsItemWriter<Dining> requestSender() {
		JmsItemWriter<Dining> writer = new JmsItemWriter<>();
		writer.setJmsTemplate(sendingJmsTemplate);
		return writer;
	}		
	

	@Bean
	public static PropertySourcesPlaceholderConfigurer pspc() {
		return new PropertySourcesPlaceholderConfigurer();
	}
}
