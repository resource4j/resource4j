package com.github.resource4j.objects;

import com.github.resource4j.*;

import java.util.Objects;

public abstract class AbstractResourceObject implements ResourceObject {

	protected final String name;

	protected final String resolvedName;

    public AbstractResourceObject(String name, String resolvedName) {
        this.name = name;
        this.resolvedName = resolvedName;
    }

    @Override
    public String name() {
        return name;
    }

    @Override
    public String resolvedName() {
        return resolvedName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AbstractResourceObject that = (AbstractResourceObject) o;
        return Objects.equals(resolvedName, that.resolvedName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(resolvedName);
    }

    @Override
    public String toString() {
        return name + " -> " + resolvedName;
    }

}
