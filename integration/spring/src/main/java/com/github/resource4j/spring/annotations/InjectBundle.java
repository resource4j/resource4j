package com.github.resource4j.spring.annotations;

import java.lang.annotation.*;

/**
 * Specifies resource provider for annotated class or package:
 * 
 * @author Ivan Gammel
 * @see com.github.resource4j.resources.ResourceProvider
 */
@Target({ ElementType.TYPE, ElementType.PACKAGE })
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface InjectBundle {

	/**
	 * Returns name of the bundle to use when autowiring the annotated class of classes 
	 * from annotated package.
	 * @return valid name of the bundle 
	 */
	String value();

	/**
	 * Returns id part of resource provider key: this is a prefix for resource value keys 
	 * constructed for autowired fields in annotated class or classes in annotated package. 
	 * @return id part of resource key
	 */
	String id() default "";

}
