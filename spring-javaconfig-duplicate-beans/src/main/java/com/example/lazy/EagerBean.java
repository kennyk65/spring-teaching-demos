package com.example.lazy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

@Component
public class EagerBean {

	@Autowired @Lazy
	LazyBean lazy;
	
	public EagerBean() {
		System.out.println("Just Instantiated " + this.getClass());
	}

	public String getStaticValue() {
		//	Get the value from the Lazy bean.  Depending on @Lazy the order of the sysouts will be affected.
		System.out.println("About to call Lazy bean...");
		String value = lazy.getStaticValue();
		System.out.println("Returned from lazy bean call.");
		return value;
	}
}
