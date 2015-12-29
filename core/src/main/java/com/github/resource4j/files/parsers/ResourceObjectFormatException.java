package com.github.resource4j.files.parsers;

import com.github.resource4j.ResourceObject;
import com.github.resource4j.ResourceObjectException;

/**
 * Exception thrown by {@link ResourceParser} implementations when {@link ResourceObject} cannot be parsed.
 * @author Ivan Gammel
 * @since 2.0.2
 */
public class ResourceObjectFormatException extends ResourceObjectException {

	private static final long serialVersionUID = 1L;

	/**
	 * @param object the object that cannot be parsed due to incorrect format
	 * @param message exception message pattern
	 */
	public ResourceObjectFormatException(ResourceObject object, String message) {
		super(message, object.name(), object.resolvedName());
	}
	
	/**
	 * @param object the object that cannot be parsed due to incorrect format
	 * @param cause the parsing error
	 */
	public ResourceObjectFormatException(ResourceObject object, Throwable cause) {
		super(cause, object.name(), object.resolvedName());
	}
	
}
