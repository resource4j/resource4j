package com.github.resource4j;

import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

/**
 * Resource value explicitly defined as nullable.
 * @author Ivan Gammel
 * @since 1.0
 * @param <V> type of wrapped value
 */
public interface OptionalValue<V> extends ResourceValue<V> {

	/**
	 * Converts this optional value to java.lang.Optional
	 * @return nullable java.lang.Optional
	 * @since 3.0
	 */
	default Optional<V> std() {
		return Optional.ofNullable(asIs());
	}
	
    /**
     * Similarly to java.lang.Optional, invokes the specified consumer with 
     * the value if it's present, otherwise does nothing.
     *
     * @param consumer block to be executed if a value is present
     * @throws NullPointerException if value is present and {@code consumer} is null
	 * @since 3.0
     */
	default void ifPresent(Consumer<? super V> consumer) {
		V value = asIs();
		if (value != null) {
			consumer.accept(value);
		}
	}
	
    /**
     * Similarly to java.lang.Optional, if a value is present, and the value 
     * matches the given predicate, returns an {@code OptionalValue} describing 
     * the value, otherwise returns an empty {@code OptionalValue}.
     *
     * @param predicate a predicate to apply to the value, if present
     * @return an {@code OptionalValue} describing the value of this {@code OptionalValue}
     * if a value is present and the value matches the given predicate,
     * otherwise an empty {@code OptionalValue}
     * @throws NullPointerException if the predicate is null
	 * @since 3.0
     */
	OptionalValue<V> filter(Predicate<V> predicate);
	
    /**
     * Like in java.lang.Optional, if a value is present, apply the provided 
     * mapping function to it, and if the result is non-null, return 
     * an {@code OptionalValue} describing the result.  Otherwise return 
     * an empty {@code OptionalValue}.
     *
     * @param <U> The type of the result of the mapping function
     * @param mapper a mapping function to apply to the value, if present
     * @return an {@code OptionalValue} describing the result of applying a mapping
     * function to the value of this {@code OptionalValue}, if a value is present,
     * otherwise an empty {@code OptionalValue}
     * @throws NullPointerException if the mapping function is null
	 * @since 3.0
     */
    <U> OptionalValue<U> map(Function<? super V, ? extends U> mapper);

	
	/**
	 * Returns wrapped value or given default value if wrapped value is <code>null</code>. If <code>defaultValue</code> is <code>null</code>,
	 * {@link IllegalArgumentException} is thrown.
	 * @param defaultValue the value to return if wrapped value is <code>null</code>. Cannot be <code>null</code>
	 * @return wrapped value or given default value
	 * @throws NullPointerException if default value is <code>null</code>.
	 * @since 3.0
	 */
    default V orDefault(V defaultValue) {
    	if (defaultValue == null) {
    		throw new NullPointerException("defaultValue");
    	}
    	V value = asIs();
    	return value != null ? value : defaultValue;
    }

    /**
     * Like in java.util.Optional, return this value if present, 
     * otherwise invoke {@code supplier} and return the result of that invocation.
     * @param supplier a {@code Supplier} whose result is returned if no value
     * is present
     * @return this value or value provided by given supplier
     * @throws NullPointerException if supplier is <code>null</code> or returns <code>null</code>
     * @since 3.0
     */
    default V orElse(Supplier<? extends V> supplier) {
    	if (supplier == null) {
    		throw new NullPointerException("supplier");
    	}
    	V value = asIs();
    	return value != null ? value : supplier.get();
    	
    }
    
    /**
     * Like in java.util.Optional, return the contained value, if present, otherwise throw an exception
     * provided by given supplier.
     *
     * @param <X> Type of the exception to be thrown
     * @param exceptionSupplier The supplier which will return the exception to be thrown
     * @return this value
     * @throws X if there is no value present
     * @throws NullPointerException if no value is present and {@code exceptionSupplier} is null
     * @since 3.0
     */
    default <X extends Throwable> V orElseThrow(Supplier<? extends X> exceptionSupplier) throws X {
    	if (exceptionSupplier == null) {
    		throw new NullPointerException("defaultValue");
    	}
    	V value = asIs();
    	if (value == null) {
    		throw exceptionSupplier.get();
    	}
    	return value;

    }
    
    /**
     * Returns result of {@link #orDefault(Object)} as {@link MandatoryValue}. If <code>defaultValue</code> is <code>null</code>,
	 * {@link IllegalArgumentException} is thrown.
     * @param defaultValue the value to wrap if own wrapped value is <code>null</code>. Cannot be <code>null</code>.
     * @return this value as mandatory
     * @throws NullPointerException if default value is <code>null</code>.
     */
    MandatoryValue<V> or(V defaultValue);

    /**
     * Like in java.util.Optional, return this value as mandatory if present, 
     * otherwise invoke {@code supplier} and return the result of that invocation 
     * wrapped in mandatory value.
     * 
     * @param supplier a {@code Supplier} whose result is returned if no value
     * is present
     * @return this value or, if it's null, the value returned by given supplier
     * @throws NullPointerException if supplied value is <code>null</code>
     * @since 3.0
     */
    MandatoryValue<V> orElseGet(Supplier<? extends V> supplier);
    
    /**
     * Returns this value as {@link MandatoryValue} if it is not <code>null</code>, otherwise throws {@link MissingValueException}.
     * @return this value as mandatory (not <code>null</code>)
     * @throws MissingValueException exception thrown if wrapped value is <code>null</code>
     */
    MandatoryValue<V> notNull() throws MissingValueException;

}
