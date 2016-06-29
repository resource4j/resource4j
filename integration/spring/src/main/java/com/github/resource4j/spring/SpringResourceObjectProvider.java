package com.github.resource4j.spring;

import com.github.resource4j.ResourceObject;
import com.github.resource4j.objects.exceptions.ResourceObjectAccessException;
import com.github.resource4j.objects.providers.AbstractFileResourceObjectProvider;
import com.github.resource4j.objects.providers.ResourceObjectProvider;
import com.github.resource4j.resources.context.ResourceResolutionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.io.Resource;

import com.github.resource4j.objects.exceptions.MissingResourceObjectException;
import org.springframework.core.io.ResourceLoader;

import static org.springframework.core.io.ResourceLoader.CLASSPATH_URL_PREFIX;

public class SpringResourceObjectProvider
		extends AbstractFileResourceObjectProvider
		implements ApplicationContextAware {
	
	private static final Logger LOG = LoggerFactory.getLogger(SpringResourceObjectProvider.class);

	private ApplicationContext applicationContext;

	public SpringResourceObjectProvider() {
		super();
		LOG.debug("Resource path configured: <managed by Spring Framework>");		
	}

    @Override
	public void setApplicationContext(ApplicationContext applicationContext)
			throws BeansException {
		this.applicationContext = applicationContext;
	}

    @Override
	public ResourceObject get(String name, String actualName)
			throws MissingResourceObjectException {
		if (applicationContext == null) {
			throw new IllegalStateException("SpringResourceObjectProvider not initialized: application context required.");
		}
        String path = actualName.startsWith("/") || actualName.startsWith(CLASSPATH_URL_PREFIX)
                ? actualName
                : '/' + actualName;

		Resource resource = applicationContext.getResource(path);
		if (!resource.exists() || !resource.isReadable()) {
			throw new MissingResourceObjectException(name, actualName);
		}
		return new SpringResourceObject(name, resource);
	}

}
