package com.github.resource4j.files;

import com.github.resource4j.ResourceException;

public class InaccessibleResourceException extends ResourceException {

	private static final long serialVersionUID = 1L;

	public InaccessibleResourceException(URLResourceFile file, Throwable cause) {
		super(file.resolvedName() + " (" + file.key() + ")", cause);
	}

}
