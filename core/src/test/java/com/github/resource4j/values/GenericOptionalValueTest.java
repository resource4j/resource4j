package com.github.resource4j.values;

import com.github.resource4j.OptionalValue;
import com.github.resource4j.OptionalValueContracts;
import com.github.resource4j.ResourceKey;

public class GenericOptionalValueTest extends OptionalValueContracts {

	@Override
	protected OptionalValue<String> createValue(ResourceKey key, String content) {
		return new GenericOptionalValue<>("unimportant", key, content);
	}

}
