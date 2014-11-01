package com.github.resource4j.files.parsers;

import java.io.IOException;

import com.github.resource4j.OptionalValue;
import com.github.resource4j.ResourceKey;
import com.github.resource4j.files.InaccessibleResourceException;
import com.github.resource4j.files.ResourceFile;
import com.github.resource4j.files.ResourceFileException;

/**
 * Basic implementation of a {@link ResourceParser} that provides a framework for specialized implementations.
 * @author Ivan Gammel
 *
 * @param <T> type of parsed content
 * @param <V> type of {@link OptionalValue} to return
 */
public abstract class AbstractParser<T, V extends OptionalValue<T>> implements ResourceParser<T, V> {

    @Override
    public V parse(ResourceKey key, ResourceFile file) {
      T value = null;
      Throwable suppressedException = null;
      try {
          value = parse(file);
      } catch (IOException | ResourceFileException e) {
          suppressedException = e;
      }
      return createValue(file, key, value, suppressedException);
    }

    /**
     * Create instance of {@link OptionalValue} with wrapped file content
     * @param file parsed file
     * @param key key to use for value creation
     * @param value parsed file content to wrap
     * @param suppressedException optional exception caught when parsing
     * @return {@link OptionalValue} wrapping the file content
     */
    protected abstract V createValue(ResourceFile file, ResourceKey key, T value, Throwable suppressedException);

    /**
     * Perform actual parsing of given file. 
     * @param file the file to parse
     * @return parsed content
     * @throws IOException if reading from input stream failed
     * @throws ResourceFileFormatException if file format is invalid
     * @throws InaccessibleResourceException if file does not exist or not accessible
     * @see ResourceFile#asStream()
     */
    protected abstract T parse(ResourceFile file) throws IOException, ResourceFileException;

}
