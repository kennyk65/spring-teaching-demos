package org.springframework.cloud.serverless;

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
	 * Instantiate the Spring ApplicationContext once and only once, using
	 * the class specified by spring.config.class or SPRING_CONFIG_CLASS.
	 * This code is thread safe, otherwise multiple Lambda invocations 
	 * could create multiple ApplicationMainEntry Contexts.
	 */
	public static synchronized ApplicationContext getInstance() {
		if(context==null) {
			context = SpringApplication.run(SpringLauncherSingleton.getMainClass());
		}
		return context;
	}

	
	/**
	 * Determine what class to use as the 'main' @Configuration class.
	 * This should be user specified via spring.config.class system property
	 * or SPRING_CONFIG_CLASS environment variable, the former takes precedent.
	 */
	@SuppressWarnings("rawtypes")
	public static Class getMainClass() {
		String classname = System.getProperty("spring.config.class");
		if (classname == null) {
			classname = System.getenv("SPRING_CONFIG_CLASS");
		}
		if (classname == null) {
			throw new ServerlessException("No main Spring @Configuration class was specified.  Use SPRING_CONFIG_CLASS environment variable or spring.config.class system property to specify.");
		}
		
		Class configClass = null;
		try {
			configClass = Class.forName(classname);
		} catch (ClassNotFoundException e) {
			throw new ServerlessException(
				"Could not interpret '" + classname + "' as a valid Java class.", e);
		}
		return configClass;
	}
}
