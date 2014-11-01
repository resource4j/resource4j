package com.github.resource4j.files;

import java.io.InputStream;

import com.github.resource4j.OptionalValue;
import com.github.resource4j.ResourceKey;
import com.github.resource4j.files.parsers.ResourceParser;

/**
 * A reference to resource file resolved by Resource4J API. 
 * The referenced file may not exist or may be not accessible - the API does 
 * not perform the check before first attempt to load file contents.
 * 
 * @author Ivan Gammel
 */
public interface ResourceFile {

    /**
     * @return the key used to request this file
     */
    ResourceKey key();

    /**
     * Returns the actual name of this resource file if it exists or default file name 
     * for given key if file does not exist.
     * @return resolved name of this resource file
     * @since 2.0
     */
    String resolvedName();

    /**
     * Returns content of this file as input stream, throwing {@link InaccessibleResourceException} 
     * if the file is not exist or not accessible.
     * @return content of this file as input stream
     * @throws InaccessibleResourceException if file does not exist or is not accessible.
     */
    InputStream asStream() throws InaccessibleResourceException;

    /**
     * Parse the content of this file using given parser and return it in OptionalValue, 
     * which will be empty if the file does not exist.
     * @param parser a parser to use
     * @param <T> type of content to be returned in OptionalValue.
     * @param <V> type of returned value defined by parser implementation.
     * @return returns content of this file parsed using given parser.
     */
    <T, V extends OptionalValue<T>> V parsedTo(ResourceParser<T, V> parser);

}
