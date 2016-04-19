package com.github.resource4j.spring.config;

import com.github.resource4j.resources.RefreshableResources;
import com.github.resource4j.resources.RefreshableResourcesConfigurator;
import com.github.resource4j.resources.Resources;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DelegatingResource4jConfiguration extends Resource4jConfigurationSupport implements ApplicationContextAware {
	
	private ApplicationContext applicationContext;
	
	@Bean
	public Resources resources(RefreshableResourcesConfigurator configurator) {
		RefreshableResources resources = new RefreshableResources(configurator);
		return resources;
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext)
			throws BeansException {
		this.applicationContext = applicationContext;
	}
	
}
