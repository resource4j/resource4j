package com.github.resource4j.spring.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.github.resource4j.files.parsers.ResourceParser;
import com.github.resource4j.spring.context.EmptyResolutionContextProvider;
import com.github.resource4j.spring.context.ResolutionContextProvider;

/**
 * <p>Indicates that annotated field must be initialized from the specified resource file using 
 * default type-based parser or custom one, specified in parsedBy attribute.
 * Resource file can be specified as absolute or relative path to the package of the bean.
 * When '*' character is used instead of file name, bean name is used with provided extension.</p>
 * <p>In following code:</p><pre>
 * class MyBean {
 * 
 *    &#64;InjectResource(value = "/config/*.xml", parsedBy = ConfigParser.class)
 *    private Config config;
 *    
 * }
 * </pre><p>The file <code>/config/MyBean.xml</code> will be used to populate the value of the field 
 * and ConfigParser from Resource4j Extras will be used for parsing content.</p>
 * <p>Example of relative path definition:</p>
 * <pre>
 * package com.acme.example;
 *
 * class AnotherBean {
 *    &#64;InjectResource("*.txt")
 *    private String text;
 * }
 * </pre><p>This code will use <code>/com/acme/example/AnotherBean.txt</code> to populate the field.</p>
 *  
 * @author Ivan Gammel
 * @since 2.1
 */
@Target({ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface InjectResource {
	
	/**
	 * <p>Injected value must be loaded from the file specified by this attribute (if set).</p>
	 * @return name of the resource file
	 */
	String value() default "";
	
	/**
	 * Indicates whether this resource value is required (i.e. not null) or not.
	 * @return <code>true</code> if injected value cannot be <code>null</code>, <code>false</code> otherwise.
	 */
	boolean required() default true;
	
	/**
	 * Annotated resource is composite data type that must be parsed from string using given parser.
	 * @return class of parser
	 */
	@SuppressWarnings("rawtypes")
	Class<? extends ResourceParser> parsedBy() default ResourceParser.class;
	
	/**
	 * Use given resolution context provider to resolve the resource value during autowiring. 
	 * This attribute can be used to specify dynamic resolution context provider for scoped 
	 * beans, which may build resolution context from web sessions or requests.
	 * @return type of resolution context provider to use for this value
	 */
	Class<? extends ResolutionContextProvider> resolvedBy() default EmptyResolutionContextProvider.class;	
}
