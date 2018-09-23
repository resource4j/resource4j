package com.github.resource4j.objects.parsers;

import com.github.resource4j.OptionalValue;
import com.github.resource4j.ResourceKey;
import com.github.resource4j.ResourceObject;
import com.github.resource4j.ResourceObjectException;
import com.github.resource4j.objects.exceptions.InaccessibleResourceObjectException;

import java.io.IOException;

/**
 * Basic implementation of a {@link ResourceParser} that provides a framework for specialized implementations.
 * @author Ivan Gammel
 *
 * @param <T> type of parsed data
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
     * Create instance of {@link OptionalValue} with wrapped object data
     * @param object parsed object
     * @param key key to use for value creation
     * @param value parsed object data to wrap
     * @param suppressedException optional exception caught when parsing
     * @return {@link OptionalValue} wrapping the object data
     */
    protected abstract V createValue(ResourceObject object, ResourceKey key, T value, Throwable suppressedException);

    /**
     * Perform actual parsing of given object
     * @param object the object to parse
     * @return parsed data
     * @throws IOException if reading from input stream failed
     * @throws ResourceObjectFormatException if object data format is invalid
     * @throws InaccessibleResourceObjectException if object does not exist or not accessible
     * @see ResourceObject#asStream()
     */
    protected abstract T parse(ResourceObject object) throws IOException, ResourceObjectException;

}
