package com.github.resource4j.files.parsers;

import com.github.resource4j.OptionalValue;
import com.github.resource4j.ResourceKey;
import com.github.resource4j.ResourceObject;

/**
 * <p>Resource parser implementations load the content of provided resource object as binary stream, 
 * convert it to the value of given type and wrap in {@link OptionalValue}. 
 * If parsing fails due to incorrect format, missing or inaccessible resource object, the 
 * wrapped value will be <code>null</code>.
 * Parser implementations provided by Resource4J library preserve information on why parsing failed. 
 * When conversion to MandatoryValue is performed, thrown exception contains this information.</p>
 * <p>Type of returned {@link OptionalValue} can be specialized by parser implementation to 
 * extend Resource4J DSL capabilities.</p>
 * 
 * @author Ivan Gammel
 *
 * @param <T> type of parsed content
 * @param <V> type of returned {@link OptionalValue}
 */
public interface ResourceParser<T, V extends OptionalValue<T>> {

	/**
	 * Parse given resource file and wrap the resulting content into {@link OptionalValue}.
	 * @param key the key to use as {@link OptionalValue} key. Must not be <code>null</code>
	 * @param object the object to parse. Must not be <code>null</code>
	 * @return {@link OptionalValue} with object model
	 */
    V parse(ResourceKey key, ResourceObject object);

}
