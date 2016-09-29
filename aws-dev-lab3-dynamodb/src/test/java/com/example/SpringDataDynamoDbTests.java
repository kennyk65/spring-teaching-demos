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
public class SpringDataDynamoDbTests {


	  @Autowired InfectionRepository repository;

	  @Test
	  public void sampleTestCase() {
	    Infection dave = new Infection("123", "Austin", "sdfdsf");
	    repository.save(dave);

	    Infection joe = new Infection("343", "Boston", "sssddd");
	    repository.save(joe);

	    List<Infection> result = repository.findByCity("Boston");
	    assertThat(result.size(), is(1));
	    assertThat(result, hasItem(joe));
	  }	

}
