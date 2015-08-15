package com.github.resource4j.spring.annotations.support;

import static com.github.resource4j.ResourceKey.key;
import static com.github.resource4j.files.parsers.ResourceParsers.binary;
import static com.github.resource4j.resources.resolution.ResourceResolutionContext.in;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.ReflectionUtils.FieldCallback;

import com.github.resource4j.MandatoryString;
import com.github.resource4j.OptionalString;
import com.github.resource4j.ResourceKey;
import com.github.resource4j.files.ResourceFile;
import com.github.resource4j.resources.ResourceProvider;
import com.github.resource4j.resources.Resources;
import com.github.resource4j.resources.references.GenericResourceFileReference;
import com.github.resource4j.resources.references.GenericResourceValueReference;
import com.github.resource4j.resources.references.ResourceFileReference;
import com.github.resource4j.resources.references.ResourceValueReference;
import com.github.resource4j.resources.resolution.ResourceResolutionContext;
import com.github.resource4j.spring.AutowiredResource;
import com.github.resource4j.spring.ResourceValueBeanPostProcessor;

/**
 * @author IvanGammel
 * @deprecated since 2.1 
 */
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

		if (ResourceFileReference.class.equals(type)) {
			String name = buildFileName(bean.getClass(), beanName, autowired.bundle());
			id = name;
			value = new GenericResourceFileReference(resources, name);
		} else if (ResourceFile.class.equals(type)) {
			String name = buildFileName(bean.getClass(), beanName, autowired.bundle());
			id = name;
			value = resources.contentOf(name, context);
	    } else if (type.isArray() && (type.getComponentType() == Byte.TYPE)) {
				String name = buildFileName(bean.getClass(), beanName, autowired.bundle());
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
	
	// TODO: add support for localization of session-scoped beans here
	@SuppressWarnings("static-method")
	private Locale getCurrentLocale() {
		return Locale.getDefault();
	}

	public static String buildFileName(Class<?> beanType, String beanName, String source) {
		StringBuilder path = new StringBuilder();
		if (!source.startsWith("/")) { // is relative path?
			path.append('/').append(beanType.getPackage().getName().replace('.', '/')).append('/');
		}
		path.append(source);
		int maskPosition = path.lastIndexOf("*");
		if (maskPosition >= 0) {
			path.replace(maskPosition, maskPosition+1, beanName != null ? beanName : beanType.getSimpleName());
		}
		return path.toString();
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