package com.aliece.alieee.annotation.component;

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
public @interface Interceptor {
	String value() default "";

	String name() default "";

	String pointcut() default "";
}
