package com.example;

import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ImportResource;

@SpringBootApplication
@ImportResource("classpath:/aws-config.xml")
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}
	
	@Autowired InfectionTableCreator tableCreator;
	@Autowired InfectionService tableService;
	@Autowired InfectionRepository tableDao;
	
	@PostConstruct
	public void run() throws Exception {
		tableCreator.setup();											//	Setup the DynamoDB table.
		tableService.loadInfectionsFromS3();							//	Load the data from S3.
		List<Infection> infections = tableDao.findByCity("Houston");	//	Do a query, just to show it works.
		System.out.printf("%s infections found in %s.", infections.size(), "Houston");
		tableCreator.cleanup();
	}
}
