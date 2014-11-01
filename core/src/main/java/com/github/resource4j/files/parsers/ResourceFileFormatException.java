package com.github.resource4j.files.parsers;

import com.github.resource4j.files.ResourceFile;
import com.github.resource4j.files.ResourceFileException;

/**
 * Exception thrown by {@link ResourceParser} implementations when {@link ResourceFile} cannot be parsed.
 * @author Ivan Gammel
 * @since 2.0.2
 */
public class ResourceFileFormatException extends ResourceFileException {

	private static final long serialVersionUID = 1L;

	/**
	 * 
	 * @param file the file with incorrect format
	 * @param message exception message pattern
	 */
	public ResourceFileFormatException(ResourceFile file, String message) {
		super(file.key(), file.resolvedName(), message);
	}
	
	/**
	 * 
	 * @param file the file with incorrect format
	 * @param cause the parsing error
	 */
	public ResourceFileFormatException(ResourceFile file, Throwable cause) {
		super(file.key(), file.resolvedName(), cause);
	}
	
}
