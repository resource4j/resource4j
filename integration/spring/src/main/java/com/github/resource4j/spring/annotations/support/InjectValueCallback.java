package com.github.resource4j.spring.annotations.support;

import static com.github.resource4j.ResourceKey.bundle;
import static com.github.resource4j.resources.resolution.ResourceResolutionContext.withoutContext;

import java.lang.reflect.Field;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.util.ReflectionUtils.FieldCallback;

import com.github.resource4j.MandatoryString;
import com.github.resource4j.OptionalString;
import com.github.resource4j.resources.ResourceProvider;
import com.github.resource4j.resources.Resources;
import com.github.resource4j.resources.references.GenericResourceValueReference;
import com.github.resource4j.resources.references.ResourceValueReference;
import com.github.resource4j.resources.resolution.ResourceResolutionContext;
import com.github.resource4j.spring.annotations.InjectValue;
import com.github.resource4j.spring.context.ResolutionContextProvider;

public class InjectValueCallback implements FieldCallback {

	private static final Logger LOG = LoggerFactory.getLogger(InjectValueCallback.class);
	
	private Resources resources;
	
	private Object bean;

	private BeanFactory factory;

	private ResourceProvider provider;

	public InjectValueCallback(Object bean, BeanFactory factory, Resources resources, ResourceProvider provider) {
		this.factory = factory;
		this.resources = resources;
		this.bean = bean;
		this.provider = provider;
	}

	@Override
	public void doWith(Field field) throws IllegalArgumentException, IllegalAccessException {
		InjectValue annotation = field.getAnnotation(InjectValue.class);
		
		
		ResourceProvider actualProvider = provider;
		if (annotation.bundle().isEmpty()) {
			if (provider == null) {
				Class<?> bundleClass = annotation.bundleClass().equals(Object.class) ? 
							bean.getClass() : annotation.bundleClass(); 
				actualProvider = resources.forKey(bundle(bundleClass));
			}
		} else {
			actualProvider = resources.forKey(bundle(annotation.bundle()));
		}

		String name = annotation.value().isEmpty() ? field.getName() : annotation.value();
		
		Object value = null;
		Class<?> type = field.getType();
		if (ResourceValueReference.class.equals(type)) {
			value = new GenericResourceValueReference(actualProvider, name);
		} else if (ResourceProvider.class.equals(type)) {
			value = actualProvider;
		} else {
			ResourceResolutionContext ctx = getContext(annotation.resolvedBy());
			OptionalString string = actualProvider.get(name, ctx);
			if (OptionalString.class.equals(type)) {
				value = string;
			} else if (MandatoryString.class.equals(type)) {
				value = string.notNull();
			} else {
				value = string.notNull().as(type);
			}
		}
		if (annotation.required() && (value == null)) {
			throw new IllegalArgumentException("Cannot autowire " + field.getDeclaringClass().getName() 
					+ "#" + field.getName() + ": value not found.");
		}
		field.setAccessible(true);
		field.set(bean, value);
		field.setAccessible(false);
		LOG.trace("Autowired {}#{} as {}", 
				field.getDeclaringClass().getName(), 
				field.getName(), 
				value.getClass().getSimpleName());
	}

	private ResourceResolutionContext getContext(Class<? extends ResolutionContextProvider> contextProviderType) {
		ResourceResolutionContext ctx = withoutContext();
		try {
			ResolutionContextProvider provider = 
				(ResolutionContextProvider) factory.getBean(contextProviderType);
			ctx = provider.getContext();
		} catch (NoSuchBeanDefinitionException e) {
			try {
				ctx = contextProviderType.newInstance().getContext();
			} catch (Exception e1) {
				// ignore
			}
		}
		return ctx;
	}

}
