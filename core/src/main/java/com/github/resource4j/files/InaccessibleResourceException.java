package com.github.resource4j.files;


public class InaccessibleResourceException extends ResourceFileException {

	private static final long serialVersionUID = 1L;

	public InaccessibleResourceException(URLResourceFile file, Throwable cause) {
		super(file.key(), file.resolvedName(), cause);
	}

}
