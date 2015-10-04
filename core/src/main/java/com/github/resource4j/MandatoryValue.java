package com.github.resource4j;

import java.text.Format;

import com.github.resource4j.generic.GenericMandatoryString;
import com.github.resource4j.generic.GenericMandatoryValue;
import com.github.resource4j.util.TypeCastException;
import com.github.resource4j.util.TypeConverter;

/**
 * Not-null representation of resource value, i.e. successfully resolved in a resolution context and, 
 * thus, contains information about resolved source.
 * @author Ivan Gammel
 * @since 1.0
 * @param <V> type of the managed value
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

	/**
	 * Converts this value to resource value of given type.
     * @param type class object for the expected type of value
     * @param <T> the expected type of value
	 * @return resource value of given type
	 * @throws TypeCastException if conversion failed
	 * @since 3.0
	 */
    default <T> MandatoryValue<T> ofType(Class<T> type) throws TypeCastException {
        T as = TypeConverter.convert(asIs(), type);
        return new GenericMandatoryValue<T>(resolvedSource(), key(), as);    	
    }
    
	/**
	 * Converts this value to resource value of String type.
	 * @return mandatory value of String type
	 * @throws TypeCastException if conversion failed
	 * @since 3.0
	 */
    default MandatoryString asString() throws TypeCastException {
        String as = TypeConverter.convert(asIs(), String.class);
        return new GenericMandatoryString(resolvedSource(), key(), as);    	
    }

	/**
	 * Converts this value to resource value of String type.
     * @param format conversion pattern
	 * @return mandatory value of String type
	 * @throws TypeCastException if conversion failed
	 * @since 3.0
	 */
    default MandatoryString asString(String format) throws TypeCastException {
        String as = TypeConverter.convert(asIs(), String.class, format);
        return new GenericMandatoryString(resolvedSource(), key(), as);    	
    }
    
	/**
	 * Converts this value to resource value of String type.
     * @param format conversion pattern
	 * @return mandatory value of String type
	 * @throws TypeCastException if conversion failed
	 * @since 3.0
	 */
    default MandatoryString asString(Format format) throws TypeCastException {
        String as = TypeConverter.convert(asIs(), String.class, format);
        return new GenericMandatoryString(resolvedSource(), key(), as);    	
    }
    
}
