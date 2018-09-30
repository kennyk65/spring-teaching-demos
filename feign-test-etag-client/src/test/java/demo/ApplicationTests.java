package demo;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ApplicationTests {

	@Autowired PersonDao dao;
	
	@Test
	public void contextLoads() {
		
		//	First call should be a 200:
		ResponseEntity<Person> entity = dao.getPerson();
		assertEquals(200, entity.getStatusCodeValue());
		
		//	Second call should be a 304:
		entity = dao.getPerson();
		assertEquals(304, entity.getStatusCodeValue());
		
		
		
	}

}
