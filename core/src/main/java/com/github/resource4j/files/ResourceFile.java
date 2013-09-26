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
     *
     * @return
     */
    ResourceKey key();

    /**
     *
     * @return
     */
    InputStream asStream();

    /**
     *
     * @param parser
     * @return
     */
    <T, V extends OptionalValue<T>> V parsedTo(ResourceParser<T, V> parser);

}
