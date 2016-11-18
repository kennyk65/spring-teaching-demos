package com.example;

import org.springframework.boot.SpringApplication;
import org.springframework.context.ApplicationContext;

/**
 * Instantiate the Spring ApplicationMainEntry Context once and only once.  Classic singleton.
 */
public class SpringLauncherSingleton {

	private static ApplicationContext context;
	
	private SpringLauncherSingleton() {
	}

	/**
	 * Beware: this code needs to be thread safe.  
	 * Otherwise multiple Lambda invocations 
	 * could create multiple ApplicationMainEntry Contexts.
	 */
	public static synchronized ApplicationContext getInstance() {
		if(context==null) context = SpringApplication.run(Config.class);
		return context;
	}
}
