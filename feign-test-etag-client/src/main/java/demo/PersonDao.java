package demo;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(name="x", url="http://localhost:8888")
public interface PersonDao {

	@RequestMapping(value="/", method=RequestMethod.GET)
	ResponseEntity<Person> getPerson();
	
}
