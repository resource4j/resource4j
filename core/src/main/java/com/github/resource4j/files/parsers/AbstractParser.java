package com.github.resource4j.files.parsers;

import java.io.IOException;
import java.io.InputStream;

import com.github.resource4j.OptionalValue;
import com.github.resource4j.ResourceKey;
import com.github.resource4j.files.ResourceFile;

public abstract class AbstractParser<T, V extends OptionalValue<T>> implements ResourceParser<T, V> {

    @Override
    public V parse(ResourceKey key, ResourceFile file) {
      T value = null;
      Throwable suppressedException = null;
      try {
          value = parse(file.asStream());
      } catch (IOException e) {
          suppressedException = e;
      }
      return createValue(file, key, value, suppressedException);
    }

    protected abstract V createValue(ResourceFile file, ResourceKey key, T value, Throwable suppressedException);

    protected abstract T parse(InputStream stream) throws IOException;

}
