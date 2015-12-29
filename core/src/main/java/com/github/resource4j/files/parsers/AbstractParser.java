package com.github.resource4j.files.parsers;

import java.io.IOException;

import com.github.resource4j.*;

/**
 * Basic implementation of a {@link ResourceParser} that provides a framework for specialized implementations.
 * @author Ivan Gammel
 *
 * @param <T> type of parsed content
 * @param <V> type of {@link OptionalValue} to return
 */
public abstract class AbstractParser<T, V extends OptionalValue<T>> implements ResourceParser<T, V> {

    @Override
    public V parse(ResourceKey key, ResourceObject object) {
      T value = null;
      Throwable suppressedException = null;
      try {
          value = parse(object);
      } catch (IOException | ResourceObjectException e) {
          suppressedException = e;
      }
      return createValue(object, key, value, suppressedException);
    }

    /**
     * Create instance of {@link OptionalValue} with wrapped object content
     * @param object parsed object
     * @param key key to use for value creation
     * @param value parsed object content to wrap
     * @param suppressedException optional exception caught when parsing
     * @return {@link OptionalValue} wrapping the object content
     */
    protected abstract V createValue(ResourceObject object, ResourceKey key, T value, Throwable suppressedException);

    /**
     * Perform actual parsing of given object
     * @param object the object to parse
     * @return parsed content
     * @throws IOException if reading from input stream failed
     * @throws ResourceObjectFormatException if object data format is invalid
     * @throws InaccessibleResourceException if object does not exist or not accessible
     * @see ResourceObject#asStream()
     */
    protected abstract T parse(ResourceObject object) throws IOException, ResourceObjectException;

}
