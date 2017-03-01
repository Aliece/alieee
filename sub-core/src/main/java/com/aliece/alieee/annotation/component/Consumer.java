/*
 * Copyright 2003-2009 the original author or authors.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * 
 */
package com.aliece.alieee.annotation.component;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Consumer of the producer annotated with @send(topic) of the method;
 * 
 * * Topic/queue(1:N or 1:1):
 * 
 * @see @Send
 */
@Target(TYPE)
@Retention(RUNTIME)
@Documented
public @interface Consumer {
	/**
	 * topic name
	 * 
	 * @Send(topicName) ==> @Consumer(topicName);
	 * 
	 * @return topic name
	 */
	String value();

}
