package com.aliece.alieee.annotation.model;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * 
 * Domain Model should normal live in memory not in database. so cache in memory
 * is very important for domain model life cycle.
 * 
 */
@Target(METHOD)
@Retention(RUNTIME)
@Documented
public @interface Send {
	/**
	 * topic/queue name
	 * 
	 * @Send(topicName) ==> @Consumer(topicName);
	 * 
	 * @return topic/queue name
	 */
	String value();

	boolean asyn() default true;
}
