package com.github.resource4j;

public class InaccessibleResourceException extends ResourceObjectException {

	private static final long serialVersionUID = 1L;

	public InaccessibleResourceException(Throwable cause, String name, String actualName) {
		super(cause, name, actualName);
	}

	public InaccessibleResourceException(String message, String name, String actualName) {
		super(message, name, actualName);
	}
}
