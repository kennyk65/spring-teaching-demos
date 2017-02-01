/**
 * 
 */
package org.springframework.cloud.serverless;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.stereotype.Component;

/**
 * Identifies this bean as not merely a Spring Bean, but one designed 
 * to be called as a cloud function / Lambda.
 * 
 * @author kenkrueger
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Component
public @interface Handler {

}
