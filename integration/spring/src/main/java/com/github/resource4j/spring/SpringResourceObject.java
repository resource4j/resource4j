package com.github.resource4j.spring;

import com.github.resource4j.objects.AbstractResourceObject;
import com.github.resource4j.objects.exceptions.InaccessibleResourceObjectException;
import org.springframework.core.io.Resource;

import java.io.IOException;
import java.io.InputStream;

public class SpringResourceObject extends AbstractResourceObject {

	private Resource resource;

	public SpringResourceObject(String name, Resource resource) {
		super(name, resource.getFilename());
		this.resource = resource;
	}

	@Override
	public long size() {
		try {
			return resource.contentLength();
		} catch (IOException | SecurityException e) {
			throw new InaccessibleResourceObjectException(e, name, resolvedName);
		}
	}

    @Override
    public long lastModified() {
        try {
            return resource.lastModified();
        } catch (IOException | SecurityException e) {
            throw new InaccessibleResourceObjectException(e, name, resolvedName);
        }
    }

    @Override
	public InputStream asStream() {
		try {
			return resource.getInputStream();
		} catch (IOException | SecurityException e) {
			throw new InaccessibleResourceObjectException(e, name, resolvedName);
		}
	}

}
