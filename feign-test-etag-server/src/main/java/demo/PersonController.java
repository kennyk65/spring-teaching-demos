package demo;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PersonController {

	@RequestMapping("/")
	public Person getperson() {
		return new Person ("Joe", "Blow");
	}
}
