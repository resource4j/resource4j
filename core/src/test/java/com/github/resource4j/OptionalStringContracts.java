package com.github.resource4j;

public abstract class OptionalStringContracts extends OptionalValueContracts {

	@Override
	protected abstract OptionalString createValue(ResourceKey key, String content);

}
