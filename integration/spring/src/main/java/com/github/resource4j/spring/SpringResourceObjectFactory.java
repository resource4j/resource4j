package com.github.resource4j.spring;

import com.github.resource4j.ResourceObject;
import com.github.resource4j.generic.objects.factory.ResourceObjectFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.io.Resource;

import com.github.resource4j.MissingResourceObjectException;

public class SpringResourceObjectFactory implements ResourceObjectFactory, ApplicationContextAware {
	
	private static final Logger LOG = LoggerFactory.getLogger(SpringResourceObjectFactory.class);

	private ApplicationContext applicationContext;

	public SpringResourceObjectFactory() {
		super();
		LOG.debug("Resource path configured: <managed by Spring Framework>");		
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext)
			throws BeansException {
		this.applicationContext = applicationContext;
	}

	@Override
	public ResourceObject getObject(String name, String actualName)
			throws MissingResourceObjectException {
		if (applicationContext == null) {
			throw new IllegalStateException("SpringResourceObjectFactory not initialized: application context required.");
		}
		Resource resource = applicationContext.getResource(actualName);
		if (!resource.exists() || !resource.isReadable()) {
			throw new MissingResourceObjectException(name, actualName);
		}
		return new SpringResourceObject(name, resource);
	}

}
