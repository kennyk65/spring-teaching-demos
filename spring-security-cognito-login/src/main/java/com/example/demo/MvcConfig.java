package com.example.demo;

import java.security.Principal;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;


/**
 * Spring MVC-specific configuration.  Most of the configuration is simple 
 * handler mappings so we don't need to create controllers for these simple web pages.
 * 
 * The only unusual thing being done here is an adjustment to ensure the 
 * principal's information is included in the Model for rendering purposes. 
 * This will allow our Mustache-templates to display the name of the logged-in user. 
 * This is done via an interceptor defined below.
 */
@Configuration
public class MvcConfig implements WebMvcConfigurer {

	/**
	 * These views are so simple they do not need a controller:
	 */
	public void addViewControllers(ViewControllerRegistry registry) {
		registry.addViewController("/").setViewName("index");
		registry.addViewController("/index");
		registry.addViewController("/signup-confirmation");
		registry.addViewController("/login");
		registry.addViewController("/denied");
		registry.addViewController("/secured/default");
		registry.addViewController("/secured/target");
	}

	/**
	 * Add a custom HandlerInterceptor to ensure the Principal is always added to
	 * every request (if ther is one).
	 */
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(new ParameterInterceptor());
	}

}

/**
 * Spring does not automatically add the currently logged in user (the
 * Principal) to the Model. This intercepter will do so.
 * 
 * @param principal
 *            The currently logged in user (may be null)
 * @return Return the principal - will be added to the model as "principal".
 * 
 */
class ParameterInterceptor implements HandlerInterceptor {
	/**
	 * The post-handle method is invoked for EVERY request, after the request
	 * handler (@Controller method) has been invoked and before the View is
	 * processed to create the response.
	 * <p>
	 * The Spring Web course covers advanced Spring MVC features like this.
	 * <p>
	 * DETAILS: {@inheritDoc}
	 */
	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		Principal principal = request.getUserPrincipal();

		if (principal != null && modelAndView != null) {
			modelAndView.addObject("principal", principal);
		}
	}
}
