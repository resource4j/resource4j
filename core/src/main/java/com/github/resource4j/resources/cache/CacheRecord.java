package com.github.resource4j.resources.cache;

import java.io.Serializable;
import java.util.function.Predicate;
import java.util.function.Supplier;

public class CacheRecord<V> implements java.io.Serializable {

	private Holder<V> holder = new Holder<>();

	public static <O> CacheRecord<O> initial() {
		return new CacheRecord<>();
	}

	public synchronized CacheRecord<V> fail(Throwable e) {
		this.holder = new Holder<>(e);
		return this;
	}

	public synchronized CacheRecord<V> store(V value, Supplier<Long> created) {
		this.holder = new Holder<>(value, created);
		return this;
	}

	private CacheRecord() {
	}

	public V get() {
		return holder.value;
	}

	public long created() {
		return holder.created;
	}

	public Throwable error() {
		return holder.exception;
	}

	public boolean is(Predicate<StateType> predicate) {
		return predicate.test(holder.state);
	}

	public StateType state() {
		return holder.state;
	}

	private static class Holder<V> implements Serializable {

		private long created = 0;

		private volatile StateType state = StateType.PENDING;

		private V value;

		private Throwable exception;

		public Holder(V value, Supplier<Long> created) {
			this.created = created.get();
			this.value = value;
			this.state = value != null ? StateType.EXISTS : StateType.MISSING;
		}

		public Holder(Throwable exception) {
			this.created = 0;
			this.state = StateType.ERROR;
			this.exception = exception;
		}

		public Holder() {
		}

	}

	public static final Predicate<StateType> LOADED = state -> state != StateType.PENDING;

	public enum StateType implements Predicate<StateType> {
		/** Search has not been completed */
		PENDING,
		/** Search failed */
		ERROR,
		/**
		 * Search completed successfully, but value was not found
		 */
		MISSING,
		/**
		 * Search completed successfully and value was found
		 */
		EXISTS;

		public boolean test(StateType state) {
			return state == this;
		}
	}

}
