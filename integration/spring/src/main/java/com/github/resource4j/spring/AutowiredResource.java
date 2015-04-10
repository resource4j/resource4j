package com.github.resource4j.spring;

import java.lang.annotation.*;

/**
 * The annotated property will be pre-populated during bean initialization by Spring using Resource4J API.
 * 
 * @author Ivan Gammel
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

	/**
	 * <p>Injected value must be loaded from the file specified by this attribute (if set).
	 * Can be specified as absolute  or relative to the package of the bean.
	 * When '*' character is used instead of file name, bean name is used with provided extension.</p>
	 * <p>In following code:</p><pre>
	 * class MyBean {
	 *    &#64;AutowiredResource(source = "/config/*.xml", parsedBy = XStreamParser.class)
	 *    private Configuration configuration;
	 * }
	 * </pre><p>The file <code>/config/MyBean.xml</code> will be used to populate the value of the field.</p>
	 * <p>Example of relative path definition:</p>
	 * <pre>
	 * package com.acme.example;
	 *
	 * class AnotherBean {
	 *    &#64;AutowiredResource(source = "*.txt")
	 *    private String text;
	 * }
	 * </pre><p>This code will use <code>/com/acme/example/AnotherBean.txt</code> to populate the field.</p>
	 * @return name of the resource file
	 * @since 2.1
	 */
	String source() default "";

	String context() default "";
	
	boolean localized() default false;
	
	/**
	 * Annotated resource is composite data type that must be parsed from string using given parser.
	 * @return class of parser
	 * @since 2.1
	 */
	Class<?> parsedBy() default Object.class;
	
}
