package ex;

import org.springframework.stereotype.Component;

@Component
public class DogOwner implements PetOwner {

	public Object getPet() {
		return new Dog();
	}
}
