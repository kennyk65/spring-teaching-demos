package ex;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	@Autowired 
	CatOwner catOwner;
	@Autowired 
	DogOwner dogOwner;
	
	@PostConstruct
	public void init()  {
		catOwner.getPet();
		dogOwner.getPet();
	}
}
