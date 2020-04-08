/*
 * 
 */
package org.easylibs.extoptions;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

@Retention(RUNTIME)
@Target({ FIELD })
public @interface OptionAnnotation {

	boolean catchall() default false;

	boolean clear() default false;

	String[] names() default {};

	String usage() default "";

	boolean required() default false;
	
	boolean lowercase() default true;
}
