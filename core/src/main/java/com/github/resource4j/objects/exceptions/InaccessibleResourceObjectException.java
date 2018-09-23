package com.github.resource4j.objects.exceptions;

public class InaccessibleResourceObjectException extends ResourceObjectAccessException {

	private static final long serialVersionUID = 1L;

	public InaccessibleResourceObjectException(Throwable cause, String name, String actualName) {
		super(cause, name, actualName);
	}

	public InaccessibleResourceObjectException(String message, String name, String actualName) {
		super(message, name, actualName);
	}
}
