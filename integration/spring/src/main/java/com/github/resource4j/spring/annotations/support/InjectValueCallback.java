package com.github.resource4j.spring.annotations.support;

import static com.github.resource4j.ResourceKey.key;

import java.lang.reflect.Field;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.util.ReflectionUtils.FieldCallback;

import com.github.resource4j.MandatoryString;
import com.github.resource4j.OptionalString;
import com.github.resource4j.ResourceKey;
import com.github.resource4j.resources.ResourceProvider;
import com.github.resource4j.resources.Resources;
import com.github.resource4j.resources.references.GenericResourceValueReference;
import com.github.resource4j.resources.references.ResourceValueReference;
import com.github.resource4j.spring.annotations.InjectValue;
import com.github.resource4j.spring.context.ResolutionContextProvider;

public class InjectValueCallback implements FieldCallback {

	private static final Logger LOG = LoggerFactory.getLogger(InjectValueCallback.class);
	
	private Resources resources;
	private Object bean;
	private String beanName;

	private BeanFactory factory;

	public InjectValueCallback(BeanFactory factory, Resources resources, Object bean, String beanName) {
		this.factory = factory;
		this.resources = resources;
		this.bean = bean;
		this.beanName = beanName;
	}

	@Override
	public void doWith(Field field) throws IllegalArgumentException, IllegalAccessException {
		InjectValue annotation = field.getAnnotation(InjectValue.class);
		ResourceKey key = buildKey(bean, field);
		Object value = null;
		Class<?> type = field.getType();
		if (ResourceValueReference.class.equals(type)) {
			value = new GenericResourceValueReference(resources, key);
		} else if (ResourceProvider.class.equals(type)) {
			value = resources.forKey(key);
		} else {
			ResolutionContextProvider provider = 
					(ResolutionContextProvider) factory.getBean(annotation.resolvedBy());
			OptionalString string = resources.get(key, provider.getContext());
			if (OptionalString.class.equals(type)) {
				value = string;
			} else if (MandatoryString.class.equals(type)) {
				value = string.notNull();
			} else {
				value = string.notNull().as(type);
			}
		}
		field.setAccessible(true);
		field.set(bean, value);
		field.setAccessible(false);
		LOG.trace("Autowired {}#{} as {}", 
				field.getDeclaringClass().getName(), 
				field.getName(), 
				key.getId());
	}
	
	private static ResourceKey buildKey(final Object bean, Field field) {
		InjectValue annotation = field.getAnnotation(InjectValue.class);
		String name = annotation.value();
		if (name.length() == 0) {
			name = field.getName();
		}
		String bundleName = annotation.bundle().length() > 0 ? annotation.bundle() : null;
		Class<?> bundleClass = annotation.bundleClass().equals(Object.class) ? null : annotation.bundleClass();
		if ((bundleName != null) && (bundleClass != null)) {
			LOG.warn("{}#{} declares both bundle name and class for autowiring resource value. "
					+ "Bundle name {} will be used.", 
					field.getDeclaringClass().getName(), 
					field.getName(), 
					bundleName);
		}
		ResourceKey key = null;
		if (bundleName != null) {
			key = key(bundleName, name);
		} else if (bundleClass != null) {
			key = key(bundleClass, name);
		} else {
			key = key(bean.getClass(), name);
		}
		return key;
	}
}
