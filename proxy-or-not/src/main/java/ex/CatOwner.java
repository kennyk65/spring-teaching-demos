package ex;

import org.springframework.stereotype.Component;

@Component
public class CatOwner implements PetOwner {

	public Object getPet() {
		return new Cat();
	}
}
