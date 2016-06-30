package com.github.resource4j;

import java.text.Format;
import java.util.function.Function;

import com.github.resource4j.values.GenericMandatoryString;
import com.github.resource4j.values.GenericMandatoryValue;
import com.github.resource4j.converters.TypeCastException;
import com.github.resource4j.converters.TypeConverter;

/**
 * Not-null representation of resource value, i.e. successfully resolved in a resolution context and, 
 * thus, contains information about resolved source.
 * @author Ivan Gammel
 * @since 1.0
 * @param <V> type of the managed value
 */
public interface MandatoryValue<V> extends ResourceValue<V> {

	/**
	 * Apply the provided mapping function to the wrapped value, and if the result is non-null, return
	 * an {@code OptionalValue} describing the result.  Otherwise return an empty {@code OptionalValue}.
	 *
	 * @param <U> The type of the result of the mapping function
	 * @param mapper a mapping function to apply to the value, if present
	 * @return an {@code OptionalValue} describing the result of applying a mapping
	 * function to the wrapped value, if a value is present, otherwise an empty {@code OptionalValue}
	 * @throws NullPointerException if the mapping function is null
	 * @since 3.0
	 */
	<U> OptionalValue<U> map(Function<? super V, ? extends U> mapper);

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
