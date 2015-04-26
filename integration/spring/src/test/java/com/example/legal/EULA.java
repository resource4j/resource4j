package com.example.legal;

import com.github.resource4j.resources.references.ResourceFileReference;
import com.github.resource4j.spring.AutowiredResource;

public class EULA {

	@AutowiredResource(source = "/logo.jpg")
	private byte[] logo;
	
	@AutowiredResource(source = "*.txt")
	private ResourceFileReference content;

	public byte[] getLogo() {
		return logo;
	}

	public ResourceFileReference getContent() {
		return content;
	}
	
}
