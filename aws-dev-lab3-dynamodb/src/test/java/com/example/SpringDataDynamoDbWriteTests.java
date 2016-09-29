package com.example;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
public class SpringDataDynamoDbWriteTests {


	  @Autowired InfectionService svc;
	  @Autowired InfectionRepository repo;
	  
	  @Test
	  public void sampleTestCase() throws Exception {
		  svc.loadInfectionsFromS3();

	    List<Infection> result = repo.findByCity("Reno");
	    assertThat(result.size(), is(178));
	  }	

}
