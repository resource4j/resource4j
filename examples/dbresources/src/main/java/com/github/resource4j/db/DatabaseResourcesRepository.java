package com.github.resource4j.db;

import com.github.resource4j.OptionalString;
import com.github.resource4j.ResourceKey;
import com.github.resource4j.ResourceObject;
import com.github.resource4j.objects.exceptions.ResourceObjectAccessException;
import com.github.resource4j.objects.exceptions.ResourceObjectRepositoryException;
import com.github.resource4j.objects.providers.events.ResourceObjectRepositoryListener;
import com.github.resource4j.objects.providers.mutable.ResourceValueRepository;
import com.github.resource4j.resources.context.ResourceResolutionContext;

public class DatabaseResourcesRepository implements ResourceValueRepository {

    @Override
    public void put(ResourceKey key, ResourceResolutionContext ctx, String value) {
    }

    @Override
    public void remove(ResourceKey key, ResourceResolutionContext ctx) {

    }

    @Override
    public OptionalString get(ResourceKey key, ResourceResolutionContext ctx) {
        return null;
    }

    @Override
    public void addListener(ResourceObjectRepositoryListener listener) {

    }

    @Override
    public void removeListener(ResourceObjectRepositoryListener listener) {

    }

    @Override
    public boolean contains(String name, ResourceResolutionContext context) throws ResourceObjectRepositoryException {
        return false;
    }

    @Override
    public void put(String name, ResourceResolutionContext context, byte[] data) throws ResourceObjectAccessException, ResourceObjectRepositoryException {

    }

    @Override
    public void remove(String name, ResourceResolutionContext context) throws ResourceObjectRepositoryException, ResourceObjectAccessException {

    }

    @Override
    public ResourceObject get(String name, ResourceResolutionContext context) throws ResourceObjectAccessException {
        return null;
    }
}
