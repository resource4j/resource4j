package com.github.resource4j.resources.impl;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.function.Predicate;

public class FallbackFuture<T> implements Future<T> {

	private final Future<T> future;
	private final Future<T> parent;
	private final Predicate<T> validator;

	public FallbackFuture(Future<T> future, Future<T> parent, Predicate<T> validator) {
		this.future = future;
		this.parent = parent;
		this.validator = validator;
	}

	@Override
	public boolean cancel(boolean mayInterruptIfRunning) {
		return future.cancel(mayInterruptIfRunning);
	}

	@Override
	public boolean isCancelled() {
		return future.isCancelled();
	}

	@Override
	public boolean isDone() {
		return future.isDone();
	}

	@Override
	public T get() throws InterruptedException, ExecutionException {
		T value = future.get();
		if (validator.test(value) || (parent == null)) {
			return value;
		}
		return parent.get();
	}

	@Override
	public T get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
		T value = future.get(timeout, unit);
		if (validator.test(value) || (parent == null) || !parent.isDone()) {
			return value;
		}
		return parent.get();
	}
}
