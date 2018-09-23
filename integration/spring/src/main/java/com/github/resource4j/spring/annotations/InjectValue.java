package com.github.resource4j.spring.annotations;

import com.github.resource4j.spring.ResourceValueBeanPostProcessor;
import com.github.resource4j.spring.context.EmptyResolutionContextProvider;
import com.github.resource4j.spring.context.ResolutionContextProvider;

import java.lang.annotation.*;

/**
 * Injects value of annotated field parameterized Resource4J framework.
 * @author Ivan Gammel
 * @see com.github.resource4j.resources.Resources
 * @see InjectResource
 * @see InjectBundle
 * @see ResourceValueBeanPostProcessor
 */
@Target({ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface InjectValue {
	
	/**
	 * Identifier of resource in the bundle. If not set, field name is used.
	 * @return resource id
	 */
	String value() default "";
	
	/**
	 * Indicates whether this resource value is required (i.e. not null) or not.
	 * @return <code>true</code> if injected value cannot be <code>null</code>, <code>false</code> otherwise.
	 */
	boolean required() default true;
	
	/**
	 * Resource bundle to look up in. If not set, {@link #bundleClass()} or bean class is 
	 * used to determine the bundle.
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
	 * Use given resolution context provider to resolve the resource value during autowiring. 
	 * This attribute can be used to specify dynamic resolution context provider for scoped 
	 * beans, which may build resolution context from web sessions or requests.
	 * @return type of resolution context provider to use for this value
	 */
	Class<? extends ResolutionContextProvider> resolvedBy() 
		default EmptyResolutionContextProvider.class;

	/**
	 * Specifies list of fields that must be populated in target object. Subkeys must match 
	 * field names.
	 * @return list of field names
	 */
	String[] params() default {};
	
}
