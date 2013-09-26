package com.github.resource4j.files.parsers;

import java.io.IOException;
import java.io.InputStream;

import com.github.resource4j.OptionalValue;
import com.github.resource4j.ResourceKey;

public abstract class AbstractParser<T, V extends OptionalValue<T>> implements ResourceParser<T, V> {

    @Override
    public V parse(ResourceKey key, InputStream stream) {
      T value = null;
      Throwable suppressedException = null;
      try {
          value = parse(stream);
      } catch (IOException e) {
          suppressedException = e;
      }
      return createValue(key, value, suppressedException);
    }

    protected abstract V createValue(ResourceKey key, T value, Throwable suppressedException);

    protected abstract T parse(InputStream stream) throws IOException;

}
