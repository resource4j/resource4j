package com.github.resource4j.values;

import com.github.resource4j.OptionalString;
import com.github.resource4j.OptionalStringContracts;
import com.github.resource4j.ResourceKey;
import com.github.resource4j.values.GenericOptionalString;

public class GenericOptionalStringTest extends OptionalStringContracts {

	@Override
	protected OptionalString createValue(ResourceKey key, String content) {
		return new GenericOptionalString("unimportant", key, content);
	}

}
