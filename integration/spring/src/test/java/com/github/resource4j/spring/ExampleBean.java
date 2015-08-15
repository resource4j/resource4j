package com.github.resource4j.spring;

import com.github.resource4j.MandatoryString;
import com.github.resource4j.resources.references.ResourceValueReference;
import com.github.resource4j.spring.annotations.InjectResource;
import com.github.resource4j.spring.annotations.InjectValue;
import com.github.resource4j.spring.context.RequestResolutionContextProvider;

public class ExampleBean {
	
	@InjectValue
	private String value;
	
	@InjectValue("second")
	private ResourceValueReference secondValue;
	
	@InjectValue(resolvedBy = RequestResolutionContextProvider.class)
	private MandatoryString title;
	
	@InjectResource("*.xml")
	private String text;
	
	public MandatoryString getTitle() {
		return title;
	}

	public String getValue() {
		return value;
	}

	public ResourceValueReference getSecondValue() {
		return secondValue;
	}

	public String getText() {
		return text;
	}
	
}