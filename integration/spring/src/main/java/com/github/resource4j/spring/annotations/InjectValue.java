package com.github.resource4j.spring.annotations;

import com.github.resource4j.spring.ResourceValueBeanPostProcessor;
import com.github.resource4j.spring.context.EmptyResolutionContextProvider;
import com.github.resource4j.spring.context.ResolutionContextProvider;

/**
 * 
 * @author IvanGammel
 * @see InjectResource
 * @see InjectBundle
 * @see ResourceValueBeanPostProcessor
 */
public @interface InjectValue {
	
	/**
	 * Identifier of resource in the bundle. If not set, field name is used.
	 * @return resource id
	 */
	String value() default "";
	
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
	
}
