package com.example.demo;

import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.filter.OrderedFilter;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.amazonaws.xray.javax.servlet.AWSXRayServletFilter;

@Configuration
public class ServletInitializer extends SpringBootServletInitializer {

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(Application.class);
	}

	/**
	 * Servlet Filter required for AWS XRay
	 */
	@Bean
	public FilterRegistrationBean<AWSXRayServletFilter> xrayFilter() {
	    final FilterRegistrationBean<AWSXRayServletFilter> registration = new FilterRegistrationBean<>(new AWSXRayServletFilter("sample"));
	    //registration.setOrder(OrderedFilter.REQUEST_WRAPPER_FILTER_MAX_ORDER);
	    registration.addUrlPatterns("/*");
	    return registration;
	}
}
