package com.github.resource4j.spring.annotations.support;

import static com.github.resource4j.ResourceKey.key;
import static com.github.resource4j.objects.parsers.ResourceParsers.binary;
import static com.github.resource4j.resources.context.ResourceResolutionContext.in;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import com.github.resource4j.ResourceObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.ReflectionUtils.FieldCallback;

import com.github.resource4j.MandatoryString;
import com.github.resource4j.OptionalString;
import com.github.resource4j.ResourceKey;
import com.github.resource4j.resources.ResourceProvider;
import com.github.resource4j.resources.Resources;
import com.github.resource4j.objects.GenericResourceObjectReference;
import com.github.resource4j.values.GenericResourceValueReference;
import com.github.resource4j.ResourceObjectReference;
import com.github.resource4j.ResourceValueReference;
import com.github.resource4j.resources.context.ResourceResolutionContext;
import com.github.resource4j.spring.AutowiredResource;
import com.github.resource4j.spring.ResourceValueBeanPostProcessor;

/**
 * @author IvanGammel
 */
@SuppressWarnings("deprecation")
public final class AutowiredResourceCallback implements FieldCallback {
	
	private static final Logger LOG = LoggerFactory.getLogger(ResourceValueBeanPostProcessor.class); 
	private final Object bean;
	private final String beanName;
	private Resources resources;

	public AutowiredResourceCallback(Resources resources, Object bean, String beanName) {
		this.resources = resources;
		this.bean = bean;
		this.beanName = beanName;
	}

	@Override
	public void doWith(Field field) throws IllegalArgumentException,
			IllegalAccessException {

		Object value = null;
		OptionalString string = null;
		Class<?> type = field.getType();
		String id;
		AutowiredResource autowired = field.getAnnotation(AutowiredResource.class);
		ResourceResolutionContext context = contextOf(autowired);

		if (ResourceObjectReference.class.equals(type)) {
			String name = FileNamePattern.build(bean.getClass(), beanName, autowired.bundle());
			id = name;
			value = new GenericResourceObjectReference(resources, name);
		} else if (ResourceObject.class.equals(type)) {
			String name = FileNamePattern.build(bean.getClass(), beanName, autowired.bundle());
			id = name;
			value = resources.contentOf(name, context);
	    } else if (type.isArray() && (type.getComponentType() == Byte.TYPE)) {
				String name = FileNamePattern.build(bean.getClass(), beanName, autowired.bundle());
				id = name;
				value = resources.contentOf(name, context).parsedTo(binary()).asIs();
		} else {
			ResourceKey key = buildKey(bean, field);
			id = key.toString();
			if (ResourceValueReference.class.equals(type)) {
				value = new GenericResourceValueReference(resources, key);
			} else if (ResourceProvider.class.equals(type)) {
				value = resources.forKey(key);
			} else {
				string = resources.get(key, context);
			}
		}
		if (value == null) {
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
				id);
	}
	private ResourceResolutionContext contextOf(AutowiredResource autowired) {
		String contextString = autowired.context();
		List<Object> components = new ArrayList<>();
		if (autowired.localized()) {
			components.add(getCurrentLocale());
		}
		if (contextString.length() > 0) {
			components.add(contextString);
		}
		ResourceResolutionContext context = in(components.toArray());
		return context;
	}
	
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