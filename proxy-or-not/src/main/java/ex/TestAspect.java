package ex;

import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Component
@Aspect
public class TestAspect {

	@AfterReturning(pointcut = "execution(* getPet(..))", returning = "dog")
	public void afterReturning(Dog dog) {
		System.out.println("A dog was found");
	}
}
