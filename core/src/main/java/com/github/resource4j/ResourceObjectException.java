package com.github.resource4j;

import com.github.resource4j.objects.exceptions.ResourceObjectAccessException;
import com.github.resource4j.objects.exceptions.ResourceObjectRepositoryException;
import com.github.resource4j.objects.parsers.ResourceObjectFormatException;
import com.github.resource4j.objects.providers.mutable.ResourceObjectIsNotBundleException;

public abstract sealed class ResourceObjectException extends ResourceException
        permits ResourceObjectAccessException, ResourceObjectRepositoryException,
                ResourceObjectFormatException, ResourceObjectIsNotBundleException {

	private static final long serialVersionUID = 1L;

    private String name;

	private String actualName;

    protected ResourceObjectException(String name) {
    	this(name, null, null, null);
    }

	protected ResourceObjectException(String name, String actualName) {
		this(name, actualName, null, null);
	}

	protected ResourceObjectException(Throwable cause, String name) {
		this(name, null, null, cause);
	}

	protected ResourceObjectException(Throwable cause, String name, String actualName) {
		this(name, actualName, null, cause);
	}

	protected ResourceObjectException(String message, String name, String actualName) {
		this(name, actualName, message, null);
	}

	protected ResourceObjectException(String message, Throwable cause, String name) {
		this(name, null, message, cause);
	}

	protected ResourceObjectException(String name, String actualName, String message, Throwable cause) {
		super(format(name, actualName, message), cause);
		this.name = name;
		this.actualName = actualName;
	}

	public String getName() {
		return name;
	}

	public String getActualName() {
		return actualName;
	}

	private static String format(String name, String actualFileName, String message) {
		String pattern = message != null ? message : "%s";
		StringBuilder ref = new StringBuilder();
		ref.append(name);
		if (actualFileName != null) {
			ref.append('(').append(actualFileName).append(')');
		}
		return String.format(pattern, ref.toString());
	}
}
