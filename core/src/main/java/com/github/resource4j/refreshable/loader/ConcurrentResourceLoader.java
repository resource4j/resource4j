package com.github.resource4j.refreshable.loader;

import java.time.Clock;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.resource4j.resources.resolution.DefaultResolutionContextMatcher;
import com.github.resource4j.resources.resolution.ResolutionContextMatcher;
import com.github.resource4j.resources.resolution.ResourceResolutionContext;

public class ConcurrentResourceLoader<K,V> {

	private static final Logger LOG = LoggerFactory.getLogger(ConcurrentResourceLoader.class);

	private ResolutionContextMatcher matcher = new DefaultResolutionContextMatcher();
	
	private ExecutorService loadingPlanner;
	
	private ExecutorService collector;
	
	private List<ResourceStorage<K,V>> loaders;
	
	private Clock clock;
	
	private ResourceLoadingListener<K,V> listener;
	
	
	public ConcurrentResourceLoader(ExecutorService loadingPlanner, ExecutorService collector,
			List<ResourceStorage<K, V>> loaders, ResourceLoadingListener<K, V> listener) {
		super();
		this.loadingPlanner = loadingPlanner;
		this.collector = collector;
		this.loaders = loaders;
		this.listener = listener;
	}

	public ResourceResponse<K,V> loadResource(K key, ResourceResolutionContext context) 
					throws ResourceNotFoundException {
		long timestamp = clock.millis();
		LOG.trace("Initiating resource requests for {} [{}] on {}", key, context, timestamp);
		List<ResourceResolutionContext> options = matcher.matches(context);

		Queue<Future<ResourceResponse<K,V>>> futures = new LinkedList<>(); 
		for (ResourceResolutionContext ctx : options) {
			for (ResourceStorage<K,V> loader : loaders) {
				futures.add(loadingPlanner.submit(() -> loader.get(timestamp, key, ctx)));
			}
		}
		
		ResourceNotFoundException failure = new ResourceNotFoundException(key, context);	
		while (!futures.isEmpty()) {
			Future<ResourceResponse<K,V>> future = futures.poll();
			try {
				ResourceResponse<K,V> value = future.get();
				if (value.isPresent()) {
					LOG.trace("Value {} found for {} @ {}", value, context, timestamp);
					collector.execute(() -> collect(key, context, futures));
					return value;
				}
			} catch (ExecutionException e) {
				LOG.trace("Resource loading failure: {} [{}]", key, context, e);
				failure.addSuppressed(e);
			} catch (InterruptedException e) {
				LOG.info("Resource loading interrupted: {} [{}]", key, context);
				failure.addSuppressed(e);
				break;
			}
		}
		throw failure;
	}
	
	private void collect(K key, ResourceResolutionContext ctx, 
			Queue<Future<ResourceResponse<K,V>>> futures) {	
		while (!futures.isEmpty()) {
			try {
				ResourceResponse<K,V> response = futures.poll().get();
				listener.responseReceived(response);
			} catch (ExecutionException e) {
				LOG.error("Failed to collect loading task result", e);
			} catch (InterruptedException e) {
				LOG.info("Resource loading interrupted: {} [{}]", key, ctx);
				break;
			}
		}
	}
	
}
