package com.github.resource4j.spring;

import java.lang.annotation.*;

@Target({ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface AutowiredResource {
	
	String bundle() default "";
	
	Class<?> bundleClass() default Object.class;

	String value() default "";

	String context() default "";
	
	boolean localized() default false;
	
}
