package rewards.batch.config;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.listener.SkipListenerSupport;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemStreamReader;
import org.springframework.batch.item.adapter.ItemProcessorAdapter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.FlatFileParseException;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.core.io.Resource;

import rewards.Dining;
import rewards.RewardConfirmation;
import rewards.RewardNetwork;
import rewards.batch.DiningFieldSetMapper;
import rewards.batch.DiningSkipListener;
import rewards.batch.ReportWriter;

@Configuration
@ImportResource({"classpath:app-config.xml"})
public class BatchJobConfig {

	@Autowired private JobBuilderFactory jobBuilderFactory;
	@Autowired private StepBuilderFactory stepBuilderFactory;
	@Autowired private RewardNetwork rewardNetwork;
	@Autowired private ItemStreamReader<Dining> diningRequestsReader;
	
	// TODO 03: Create a bean that will create a diningRequestJob.
	//	Use the jobBuilderFactory.  Give the job the Id and name of "diningRequestJob"
	//	Configure it to use an incrementer of RunIdIncrementer to automatically populate individual runs.
	//	Set the first step to the diningRequestsStep defined below.
	@Bean
	public Job diningRequestsJob() {
		return jobBuilderFactory.get("diningRequestsJob")
			.incrementer(new RunIdIncrementer())
			.flow(diningRequestsStep())
			.end()
			.build();
	}

	
	// TODO 04: Configure the diningRequestsStep step.
	//	Use the stepBuilderFactory.  Set the name of the step to "diningRequestsStep"
	//	Set the chunk size to 10.  Parameterize the call to this method to preserve type safety.
	//	Inject the diningRequestsReader, processor, and reportWriter 
	//	as reader, processor, and writer respectively; each are defined within @Bean methods below.
	//	Set the start limit to 3.  
	@Bean
	public Step diningRequestsStep() {
		return stepBuilderFactory.get("diningRequestsStep")
			.<Dining, RewardConfirmation>chunk(10)
			.reader(diningRequestsReader)
			.processor(processor())
			.writer(reportWriter())
			.startLimit(3)
			.build();
	}

	//	TODO 10: Copy/paste the entire diningRequestsJob (including the Step) into new bean definitions.
	//	Change the @Bean ID and Job name to skippingDiningRequestsJob.
	//	Change the @Bean ID and Step name to skippingDiningRequestsStep.
	//	Configure the step to be faultTolerant - skip FlatFileParseException 1 time. 
	@Bean
	public Job skippingDiningRequestsJob() {
		return jobBuilderFactory.get("skippingDiningRequestsJob")
			.incrementer(new RunIdIncrementer())
			.flow(skippingDiningRequestsStep())
			.end()
			.build();
	}

	@Bean
	public Step skippingDiningRequestsStep() {
		return stepBuilderFactory
			.get("skippingDiningRequestsStep")
			.<Dining, RewardConfirmation>chunk(10)
			.reader(diningRequestsReader)
			.processor(processor())
			.writer(reportWriter())
			.faultTolerant().skip(FlatFileParseException.class).skipLimit(1)
			.listener(diningSkipListener())
			.build();
	}

	// TODO 14: Within the skippingDiningRequestsStep, add a listener for the diningSkipListener.
	
	// TODO 16: Set a start-limit="3" to the diningRequestsJob (above).
	
	@Bean
	public SkipListenerSupport diningSkipListener() {
		return new DiningSkipListener();
	}

	@Bean
	public ItemProcessor<Dining, RewardConfirmation> processor() {
		ItemProcessorAdapter<Dining, RewardConfirmation> processorAdapter = new ItemProcessorAdapter<>();
		processorAdapter.setTargetObject(rewardNetwork);
		processorAdapter.setTargetMethod("rewardAccountFor");
		return processorAdapter;
	}

	//	TODO 05: Within the @Value annotation, set the SpEL expression that obtains 'input.resource.path' 
	//	from the job parameters.  Also give the bean a step scope for late binding of this value.
	@Bean
	@StepScope
	public ItemStreamReader<Dining> diningRequestsReader(
			@Value("#{jobParameters['input.resource.path']}") Resource resource)  {
		FlatFileItemReader<Dining> reader = new FlatFileItemReader<>();
		reader.setResource(resource);
		reader.setLineMapper(new DefaultLineMapper<Dining>() {{
			setLineTokenizer(new DelimitedLineTokenizer() {{
				setNames(new String[]{"creditCardNumber", "merchantNumber", "amount", "date"});
			}});
			//	TODO 07: Set the fieldSetMapper property.  
			//	Inject it with a new instance of the DiningFieldSetMapper from the last step.
			setFieldSetMapper(new DiningFieldSetMapper());
		}});
		return reader;
	}
	

	@Bean
	public ReportWriter reportWriter() {
		return new ReportWriter();
	}

}
