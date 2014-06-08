package com.github.resource4j.files;

import java.io.InputStream;

import com.github.resource4j.OptionalValue;
import com.github.resource4j.ResourceKey;
import com.github.resource4j.files.parsers.ResourceParser;

/**
 *
 * @author Ivan Gammel
 */
public interface ResourceFile {

    /**
     * @return the key used to request this file
     */
    ResourceKey key();

    /**
     * @return resolved name of this resource file
     * @since 2.0
     */
    String resolvedName();

    /**
     * @return content of this file as input stream
     */
    InputStream asStream();

    /**
     * @param parser a parser to use
     * @return returns content of this file parsed using given parser.
     */
    <T, V extends OptionalValue<T>> V parsedTo(ResourceParser<T, V> parser);

}
