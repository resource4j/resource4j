package com.github.resource4j.generic;

import com.github.resource4j.OptionalValue;
import com.github.resource4j.OptionalValueContracts;
import com.github.resource4j.ResourceKey;
import com.github.resource4j.generic.values.GenericOptionalValue;

public class GenericOptionalValueTest extends OptionalValueContracts {

	@Override
	protected OptionalValue<String> createValue(ResourceKey key, String content) {
		return new GenericOptionalValue<>("unimportant", key, content);
	}

}
