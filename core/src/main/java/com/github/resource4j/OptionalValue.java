package com.github.resource4j;

/**
 * Resource value explicitly defined as nullable.
 * @author Ivan Gammel
 * @since 1.0
 * @param <V> type of wrapped value
 */
public interface OptionalValue<V> extends ResourceValue<V> {

	/**
	 * Returns wrapped value or given default value if wrapped value is <code>null</code>. If <code>defaultValue</code> is <code>null</code>,
	 * {@link IllegalArgumentException} is thrown.
	 * @param defaultValue the value to return if wrapped value is <code>null</code>. Cannot be <code>null</code>
	 * @return wrapped value or given default value
	 * @throws IllegalArgumentException if default value is <code>null</code>.
	 */
    V orDefault(V defaultValue) throws IllegalArgumentException;

    /**
     * Returns result of {@link #orDefault(Object)} as {@link MandatoryValue}. If <code>defaultValue</code> is <code>null</code>,
	 * {@link IllegalArgumentException} is thrown.
     * @param defaultValue the value to wrap if own wrapped value is <code>null</code>. Cannot be <code>null</code>.
     * @return this value as mandatory
     * @throws IllegalArgumentException if default value is <code>null</code>.
     */
    MandatoryValue<V> or(V defaultValue) throws IllegalArgumentException;

    /**
     * Returns this value as {@link MandatoryValue} if it is not <code>null</code>, otherwise throws {@link MissingValueException}.
     * @return this value as mandatory (not <code>null</code>)
     * @throws MissingValueException exception thrown if wrapped value is <code>null</code>
     */
    MandatoryValue<V> notNull() throws MissingValueException;

}
