package com.aliece.alieee.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * 
 */
@Target(TYPE)
@Retention(RUNTIME)
@Documented
public @interface Introduce {
	String[] value();
}
