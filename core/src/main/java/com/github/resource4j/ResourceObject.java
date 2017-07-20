package com.github.resource4j;

import java.io.InputStream;

import com.github.resource4j.objects.parsers.ResourceParser;

/**
 * Resource object is a reference to a binary data object in a resource storage, that can be subsequently
 * parsed into some object model or converted to String. In a file system storage resource objects are usually
 * represented by files, in database - by BLOB records.
 *
 * The referenced object does always exist and immediately available to the client.
 *
 * @author Ivan Gammel
 * @since 3.0
 *
 *
 */
public interface ResourceObject {

	/**
	 * Name of resource object in resource storage used in requests to Resources, e.g. part of the file name that
     * includes relative path, but does not include resolution context.
	 * @return name of resource object in storage
     * @see #actualName()
	 */
	String name();
	
	/**
	 * Returns name of the object represented as {@link ResourceKey} with <code>null</code> id.
	 * @return ResourceKey with bundle equal to {@link #name()} and <code>null</code> id.
	 * @see ResourceKey#bundle()
	 */
	default ResourceKey key() {
		return ResourceKey.bundle(name());
	}
	
    /**
     * Returns the actual name of this resource object in storage resolved within some context if it exists
     * or {@link ResourceObject#name()} if it doesn't.
     * @return resolved name of this resource object
     */
	String actualName();


	/**
	 * Returns size of this object in bytes.
	 * @return size of this object in bytes
	 */
	long size();

    /**
     * The time in milliseconds of epoch when this object has been modified last time.
     * @return time when this object has been modified.
     */
    long lastModified();

	/**
	 * Returns data of this object as input stream.
	 * @return data of this file as input stream
	 */
	InputStream asStream();

	/**
	 * Parse the data of this object parameterized given parser and return it in OptionalValue,
	 * which will be empty if the object does not exist.
	 * @param parser a parser to use
	 * @param <T> type of data to be returned in OptionalValue.
	 * @return data of this object parsed to OptionalValue parameterized given parser.
	 */
	default <T> OptionalValue<T> parsedTo(ResourceParser<T, ? extends OptionalValue<T>> parser) {
		return parser.parse(key(), this);
	}

}
