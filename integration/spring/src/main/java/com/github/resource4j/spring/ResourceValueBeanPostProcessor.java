package com.github.resource4j.spring;

import static com.github.resource4j.ResourceKey.key;
import static com.github.resource4j.resources.resolution.ResourceResolutionContext.in;
import static org.springframework.util.ReflectionUtils.doWithFields;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.util.ReflectionUtils.FieldCallback;
import org.springframework.util.ReflectionUtils.FieldFilter;

import com.github.resource4j.MandatoryString;
import com.github.resource4j.OptionalString;
import com.github.resource4j.ResourceKey;
import com.github.resource4j.resources.ResourceProvider;
import com.github.resource4j.resources.Resources;
import com.github.resource4j.resources.references.GenericResourceValueReference;
import com.github.resource4j.resources.references.ResourceValueReference;

public class ResourceValueBeanPostProcessor implements BeanPostProcessor {

	private static final Logger LOG = LoggerFactory.getLogger(ResourceValueBeanPostProcessor.class); 
	
	@Autowired
	private Resources resources;

	@Override
	public Object postProcessBeforeInitialization(Object bean, String beanName)
			throws BeansException {
		return bean;
	}

	@Override
	public Object postProcessAfterInitialization(final Object bean,
			String beanName) throws BeansException {
		doWithFields(bean.getClass(), new FieldCallback() {
			@Override
			public void doWith(Field field) throws IllegalArgumentException,
					IllegalAccessException {
				ResourceKey key = buildKey(bean, field);
				Object value = null;
				if (ResourceValueReference.class.equals(field.getType())) {
					value = new GenericResourceValueReference(resources, key);
				} else if (ResourceProvider.class.equals(field.getType())) {
					field.set(bean, resources.forKey(key));
				} else {
					AutowiredResource autowired = field.getAnnotation(AutowiredResource.class);
					String contextString = autowired.context();
					List<Object> components = new ArrayList<>();
					if (autowired.localized()) {
						components.add(getCurrentLocale());
					}
					if (contextString.length() > 0) {
						components.add(contextString);
					}
					OptionalString string = resources.get(key, in(components.toArray()));
					if (OptionalString.class.equals(field.getType())) {
						value = string;
					} else if (MandatoryString.class.equals(field.getType())) {
						value = string.notNull();
					} else {
						value = string.notNull().as(field.getType());
					}
				}
				field.setAccessible(true);
				field.set(bean, value);
				field.setAccessible(false);
				LOG.trace("Autowired {}#{} as {}", 
						field.getDeclaringClass().getName(), 
						field.getName(), 
						key);
			}
		}, new FieldFilter() {
			@Override
			public boolean matches(Field field) {
				return field.isAnnotationPresent(AutowiredResource.class);
			}
		});
		return bean;
	}

	// TODO: add support for localization of session-scoped beans here
	@SuppressWarnings("static-method")
	private Locale getCurrentLocale() {
		return Locale.getDefault();
	}

	private static ResourceKey buildKey(final Object bean, Field field) {
		AutowiredResource annotation = field.getAnnotation(AutowiredResource.class);
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
