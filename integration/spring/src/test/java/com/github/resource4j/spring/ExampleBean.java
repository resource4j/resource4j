package com.github.resource4j.spring;

import com.github.resource4j.MandatoryString;
import com.github.resource4j.resources.references.ResourceValueReference;

public class ExampleBean {
	
	@AutowiredResource
	private String value;
	
	@AutowiredResource("second")
	private ResourceValueReference secondValue;
	
	@AutowiredResource(context="WEB")
	private MandatoryString title;
	
	@AutowiredResource(source = "*.xml")
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