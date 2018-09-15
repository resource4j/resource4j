package com.github.resource4j.objects.providers.mutable;

import com.github.resource4j.ResourceObjectException;


public class ResourceObjectIsNotBundleException extends ResourceObjectException {

    private static final long serialVersionUID = 1;

    public ResourceObjectIsNotBundleException(String name) {
        super(name);
    }

}
