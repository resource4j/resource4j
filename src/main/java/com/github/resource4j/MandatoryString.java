package com.github.resource4j;

/**
 *
 * @author Ivan Gammel
 * @since 1.0
 */
public interface MandatoryString extends MandatoryValue<String>, ResourceString {

    @Override
    <T> MandatoryValue<T> ofType(Class<T> type);

}
