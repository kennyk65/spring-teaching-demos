package com.example.demo;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.CompositePropertySource;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.EnumerablePropertySource;
import org.springframework.core.env.PropertySource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PropertyViewerController {

	@Value("${aws.paramstore.prefix}") 			String prefix;
	@Value("${aws.paramstore.name}") 			String name;
	@Value("${aws.paramstore.default-context}") String defaultContext;
	
	@Autowired ConfigurableEnvironment  env;
	
	@GetMapping("/")
	public String index(Model m) {
		m.addAttribute("prefix",prefix);
		m.addAttribute("name",name);
		m.addAttribute("defaultContext",defaultContext);
		m.addAttribute("properties",getProps());
		
		return "index";
	}	
	
	private Collection<Props> getProps() {
		
		Map<String,Props> props = new HashMap<>();
		
		 CompositePropertySource awsParamSources = null;
		 
		 //	Get the PropertySource(s) that come from the AWS Parameter Store:
		 OUTER: for (PropertySource<?> ps : env.getPropertySources()) {
			if (ps.getName().equals("bootstrapProperties")) {
				CompositePropertySource bootstrap = (CompositePropertySource) ps;
				for (PropertySource<?> nestedSource : bootstrap.getPropertySources()) {
					if (nestedSource.getName().equals("aws-param-store")) {
						awsParamSources = (CompositePropertySource) nestedSource;
						break OUTER;
	                }
	            }
			}
	    }
		
		//	Extract the properties, their values, and their default values: 
	    if (awsParamSources == null) {
	        System.out.println("No AWS Parameter Store PropertySource found");
	    } else {
	        for (PropertySource<?> nestedSource : awsParamSources.getPropertySources()) {
	            EnumerablePropertySource eps = (EnumerablePropertySource) nestedSource;
	            for (String propName : eps.getPropertyNames()) {
	            	Props prop = props.get(propName);
	            	if ( prop == null ) {
	            		prop = new Props();
	            	}
	            	prop.setName(propName);
	            	prop.setValue(env.getProperty(propName));
	            	if (eps.getName().equals( prefix + "/" + defaultContext + "/")) {
	            		prop.setDefaultValue((String)eps.getProperty(propName));
	            	}
	            	props.put(propName, prop);
	            }
	        }
	    }
	    return props.values();
	}
	
}
