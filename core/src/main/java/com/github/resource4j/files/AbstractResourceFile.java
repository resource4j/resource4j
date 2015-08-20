package com.github.resource4j.files;

import com.github.resource4j.OptionalValue;
import com.github.resource4j.ResourceKey;
import com.github.resource4j.files.parsers.ResourceParser;

public abstract class AbstractResourceFile implements ResourceFile {

	private ResourceKey key;
	
	private String resolvedName;
	
	public AbstractResourceFile(ResourceKey key, String resolvedName) {
		super();
        if (key == null) throw new NullPointerException("Resource file key cannot be null");
		this.key = key;
		this.resolvedName = resolvedName;
	}

	@Override
	public ResourceKey key() {
		return key;
	}

	@Override
	public String resolvedName() {
		return resolvedName;
	}

	@Override
	public <T, V extends OptionalValue<T>> V parsedTo(
			ResourceParser<T, V> parser) {
		return parser.parse(key, this);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((key == null) ? 0 : key.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		AbstractResourceFile other = (AbstractResourceFile) obj;
		if (key == null) {
			if (other.key != null)
				return false;
		} else if (!key.equals(other.key))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return key.getBundle() + " [" + resolvedName + "]";
	}
	
}
