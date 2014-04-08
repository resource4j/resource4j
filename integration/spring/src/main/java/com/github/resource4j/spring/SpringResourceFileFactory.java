package com.github.resource4j.spring;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.io.Resource;

import com.github.resource4j.ResourceKey;
import com.github.resource4j.files.MissingResourceFileException;
import com.github.resource4j.files.ResourceFile;
import com.github.resource4j.files.lookup.ResourceFileFactory;

public class SpringResourceFileFactory implements ResourceFileFactory, ApplicationContextAware {

	private ApplicationContext applicationContext;

	@Override
	public void setApplicationContext(ApplicationContext applicationContext)
			throws BeansException {
		this.applicationContext = applicationContext;
	}

	@Override
	public ResourceFile getFile(ResourceKey key, String actualName)
			throws MissingResourceFileException {
		if (applicationContext == null) {
			throw new IllegalStateException("SpringResourceFileFactory not initialized: application context required.");
		}
		Resource resource = applicationContext.getResource("classpath:" + actualName);
		if (!resource.exists() || !resource.isReadable()) {
			throw new MissingResourceFileException(key, actualName);
		}
		return new SpringResourceFile(key, resource);
	}

}
