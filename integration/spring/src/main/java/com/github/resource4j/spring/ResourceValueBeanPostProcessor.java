package com.github.resource4j.spring;

import static com.github.resource4j.ResourceKey.key;
import static com.github.resource4j.files.parsers.ResourceParsers.string;
import static com.github.resource4j.resources.resolution.ResourceResolutionContext.in;
import static org.springframework.util.ReflectionUtils.doWithFields;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.ConversionNotSupportedException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.util.ReflectionUtils.FieldCallback;
import org.springframework.util.ReflectionUtils.FieldFilter;

import com.github.resource4j.MandatoryString;
import com.github.resource4j.OptionalString;
import com.github.resource4j.ResourceKey;
import com.github.resource4j.files.ResourceFile;
import com.github.resource4j.files.parsers.ResourceParser;
import com.github.resource4j.resources.ResourceProvider;
import com.github.resource4j.resources.Resources;
import com.github.resource4j.resources.references.GenericResourceFileReference;
import com.github.resource4j.resources.references.GenericResourceValueReference;
import com.github.resource4j.resources.references.ResourceFileReference;
import com.github.resource4j.resources.references.ResourceValueReference;
import com.github.resource4j.resources.resolution.ResourceResolutionContext;

public class ResourceValueBeanPostProcessor implements BeanPostProcessor, BeanFactoryAware {

	private static final Logger LOG = LoggerFactory.getLogger(ResourceValueBeanPostProcessor.class); 
	
	@Autowired
	private Resources resources;

	
	private BeanFactory beanFactory;
	
	@Override
	public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
		this.beanFactory = beanFactory;
	}

	@Override
	public Object postProcessBeforeInitialization(Object bean, String beanName)
			throws BeansException {
		return bean;
	}

	@Override
	public Object postProcessAfterInitialization(final Object bean,
			final String beanName) throws BeansException {
		doWithFields(bean.getClass(), new FieldCallback() {
			@Override
			public void doWith(Field field) throws IllegalArgumentException,
					IllegalAccessException {

				Object value = null;
				OptionalString string = null;
				Class<?> type = field.getType();
				String id;
				AutowiredResource autowired = field.getAnnotation(AutowiredResource.class);
				ResourceResolutionContext context = contextOf(autowired);

				if (!autowired.source().isEmpty()) {
					String name = buildFileName(bean.getClass(), beanName, autowired.source());
					id = name;
					if (ResourceFileReference.class.equals(type)) {
						value = new GenericResourceFileReference(resources, name);
					} else if (ResourceFile.class.equals(type)) {
						value = resources.contentOf(name, context);
				    } else if (autowired.parsedBy() != Object.class) {
						// this is file resource
						if (!ResourceParser.class.isAssignableFrom(autowired.parsedBy())) {
							ClassCastException cause = new ClassCastException("Invalid resource specification for " 
									+ beanName + "." + field.getName() + autowired.parsedBy() 
									+ " cannot be cast to ResourceParser");
							throw new ConversionNotSupportedException(null, type, cause);
						}
						@SuppressWarnings("unchecked")
						ResourceParser<Object,?> parser = (ResourceParser<Object,?>) 
								beanFactory.getBean(autowired.parsedBy());
						value = resources.contentOf(name, context).parsedTo(parser);
					} else {
						string = resources.contentOf(name, context).parsedTo(string());
					}
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

			private ResourceResolutionContext contextOf(
					AutowiredResource autowired) {
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
