package com.github.resource4j.spring;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 * The annotated property will be pre-populated during bean initialization by Spring parameterized Resource4J API.
 * 
 * @author Ivan Gammel
 * @deprecated use {@link com.github.resource4j.spring.annotations.InjectValue} instead. Will be removed in version 4.0.
 */
@Target({ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface AutowiredResource {

	/**
	 * Resource bundle to look up. If not set, {@link #bundleClass()} or bean class is used to determine the bundle.
	 * @return bundle name
	 */
	String bundle() default "";
	
	/**
	 * Class that defines resource bundle to look up. 
	 * If not set, bean class is used to determine the bundle.
	 * @return bundle class
	 */
	Class<?> bundleClass() default Object.class;
	
	/**
	 * Identifier of resource in the bundle. If not set, field name is used.
	 * @return resource id
	 */
	String value() default "";

	String context() default "";
	
	boolean localized() default false;

	
}
