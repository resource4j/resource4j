package com.github.resource4j.spring;

public class AutowireBundleExample {

	@AutowiredResource(bundle="mybundle")
	private String country;
	
	@AutowiredResource(bundleClass = ExampleBean.class)
	private String value;

	public String getCountry() {
		return country;
	}

	public String getValue() {
		return value;
	}
	
	
	
}
