package com.github.resource4j;

/**
 *
 * @author Ivan Gammel
 * @since 1.0
 * @param <V>
 */
public interface MandatoryValue<V> extends ResourceValue<V> {

	/**
	 * Returns name of resource bundle within resolution context in which this value found:
	 * e.g. if bundle name specified in key was "example", the resolution context was 
	 * defined by Germany locale and the value was found in <code>example-de_DE.properties</code> file, 
	 * then the name of this file will be the resolved source.
	 * @return name of resource bundle
	 * @since 2.0
	 */
	String resolvedSource();

}
