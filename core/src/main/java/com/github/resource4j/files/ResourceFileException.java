package com.github.resource4j.files;

import com.github.resource4j.ResourceException;
import com.github.resource4j.ResourceKey;

public abstract class ResourceFileException extends ResourceException {

	private static final long serialVersionUID = 1L;

    private ResourceKey key;
    
    protected ResourceFileException(ResourceKey key) {
    	super(format(key, null, null));
    }

	protected ResourceFileException(ResourceKey key, String actualFileName) {
		super(format(key, actualFileName, null));
	}
	
	protected ResourceFileException(ResourceKey key, Throwable cause) {
		super(format(key, null, null), cause);
		
	}
	
	protected ResourceFileException(ResourceKey key, String actualFileName, Throwable cause) {
		super(format(key, actualFileName, null), cause);
		
	}
	protected ResourceFileException(ResourceKey key, String actualFileName, String message) {
		super(format(key, actualFileName, message));
	}


	protected ResourceFileException(ResourceKey key, Throwable cause, String message) {
		super(format(key, null, message), cause);
	}

    public ResourceKey getResourceKey() {
        return key;
    }

	private static String format(ResourceKey key, String actualFileName, String message) {
		String pattern = message != null ? message : "{0}";
		StringBuilder ref = new StringBuilder();
		ref.append(key);
		if (actualFileName != null) {
			ref.append('(').append(actualFileName).append(')');
		}
		return String.format(pattern, ref.toString());
	}
}
