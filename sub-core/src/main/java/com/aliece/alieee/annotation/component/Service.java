package com.aliece.alieee.annotation.component;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * @Service(="abc")
 * is equals to:
 * <Service name="abc" class="com.sample.Abc" />
 * 
 * Service'name is called by the client.
 * 
 */
@Target(TYPE)
@Retention(RUNTIME)
@Documented
public @interface Service {
	String value();
}
