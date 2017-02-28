package com.aliece.alieee.annotation.intercept;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * same as com.aliece.alieee.controller.service.Stateful
 * @author banQ
 *
 */

@Target(TYPE)
@Retention(RUNTIME)
@Documented
public @interface Stateful {

}
