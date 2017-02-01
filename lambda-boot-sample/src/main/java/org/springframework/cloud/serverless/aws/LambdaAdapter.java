package org.springframework.cloud.serverless.aws;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.List;

import org.springframework.cloud.serverless.CloudFunction;
import org.springframework.cloud.serverless.ServerlessException;
import org.springframework.stereotype.Component;

import com.amazonaws.services.lambda.runtime.Context;

/**
 * Adapter for calling @CloudFunction methods on @Handler beans.
 * 
 * @author kenkrueger
 */
public class LambdaAdapter {

	/**
	 * Invoke the @CloudFunction method on the @Handler bean, 
	 * resolving method parameters as needed.
	 */
    public Object handle(Object handlerBean, Object o, Context context) {
    	
    	//	TODO - CACHE THIS FOR PERFORMANCE.
    	Method cloudFunction = findAnnotatedMethod(handlerBean);
    	
    	Parameter[] parameters = cloudFunction.getParameters();
    	Object[] args = null; 
    	if (parameters != null || parameters.length > 0 ) {
    		args = new Object[parameters.length];
    		int i = 0;
    		for (Parameter p : parameters) {
    			//	TODO - SET THE METHOD PARAMETERS IN A MORE ELEGANT WAY:
    			//	RIGHT NOW THERE ARE ONLY TWO TYPES, BUT WE SHOULD SUPPORT
    			//	UNMARSHALLING, LOGGERS, ETC.
    			if ( context.getClass().isAssignableFrom(p.getType())) {
    				args[i] = context;
    			} else if ( context.getClass().isAssignableFrom(Object.class)) {
    				args[i] = o;
    			} 
    			i++;
    		}
    	}
    	
    	Object returnVal = null;
    	try {
    		returnVal = cloudFunction.invoke(handlerBean, args);
    	} catch (Exception e) {
    		throw new ServerlessException("Error invoking @CloudFunction method", e);
    	}
    	return returnVal;
    }
    
    public Method findAnnotatedMethod(Object handlerBean) {
    	
    	List<Method> methods = 
    		findAnnotatedMethods(
    			handlerBean.getClass(), CloudFunction.class);
    	
    	if (methods == null || methods.size() < 1 ) {
    		throw new ServerlessException("No @CloudFunction method was found on "  + handlerBean.getClass());
    	} else if ( methods.size() > 1) {
    		throw new ServerlessException("Only one @CloudFunction method is permitted on bean " + handlerBean.getClass());
    	}
    	
    	return methods.get(0);
    }

    
    public List<Method> findAnnotatedMethods(Class<?> clazz, Class<? extends Annotation> annotationClass) {
        Method[] methods = clazz.getMethods();
        List<Method> annotatedMethods = new ArrayList<Method>(methods.length);
        for (Method method : methods) {
          if( method.isAnnotationPresent(annotationClass)){
            annotatedMethods.add(method);
          }
        }
        return annotatedMethods;    
      }
    }    

