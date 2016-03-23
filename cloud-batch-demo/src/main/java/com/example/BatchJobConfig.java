package com.example;


import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableBatchProcessing
public class BatchJobConfig {

	@Value("${lucky-word}") String luckyWord;
	@Autowired StepBuilderFactory stepBuilderFactory;	
	
	@Bean
	public Job extremelySimpleJob(JobBuilderFactory factory) throws Exception {
		return factory 
			.get("extremelySimpleJob")
			.flow(stepOne())	
			.end()
			.build();
	}
	
	@Bean
	public Step stepOne() {
		return stepBuilderFactory
			.get("stepOne")
			.tasklet( (contrib,context) -> {
				System.out.println("LUCKY WORD IS: " + luckyWord);
				return null;
			})
			.build();
	}
		
}

