package com.github.resource4j.objects.providers;

import java.util.List;

/**
 * Common interface for resource object providers, that alter behavior of other providers, for example, by filtering
 * requests. The only additional method in this interface provides access to adapted implementations, so it will be
 * possible to for user to subscribe on adapted provider events.
 */
public interface ResourceObjectProviderAdapter extends ResourceObjectProvider {

    List<ResourceObjectProvider> unwrap();

}
