package com.example.legal;

import com.github.resource4j.resources.references.ResourceFileReference;
import com.github.resource4j.spring.annotations.InjectResource;

public class EULA {

	@InjectResource("/logo.png")
	private byte[] logo;
	
	@InjectResource(value = "*.txt", required = false)
	private ResourceFileReference content;

	public byte[] getLogo() {
		return logo;
	}

	public ResourceFileReference getContent() {
		return content;
	}
	
}
