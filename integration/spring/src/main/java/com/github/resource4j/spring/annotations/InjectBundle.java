package com.github.resource4j.spring.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.github.resource4j.spring.context.EmptyResolutionContextProvider;

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
	
	/**
	 * Use given resolution context provider to resolve all the resource values in annotated bean
	 * during autowiring process. This attribute can be used to specify dynamic resolution context 
	 * provider for scoped beans, which may build resolution context from web sessions or requests.
	 * @return type of resolution context provider to use for annotated bean
	 */
	Class<?> resolvedBy() default EmptyResolutionContextProvider.class;	
}
