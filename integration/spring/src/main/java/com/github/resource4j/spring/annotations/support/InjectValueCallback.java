package com.github.resource4j.spring.annotations.support;

import static com.github.resource4j.ResourceKey.bundle;
import static com.github.resource4j.ResourceKey.join;
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
		ResourceResolutionContext ctx = getContext(annotation.resolvedBy());
		if (annotation.params().length > 0) {
			String[] params = annotation.params();
			Field[] fields = new Field[params.length];
			for (int i = 0; i < params.length; i++) {
				try {
					fields[i] = type.getDeclaredField(params[i]);
				} catch (NoSuchFieldException | SecurityException e) {
					throw new IllegalStateException(String.format("%s - %s Value type %s does not support autowiring of %s", 
							bean.getClass().getSimpleName(),
							field.getName(),
							type.getName(),
							params[i]), e);
				}
			}
			try {
				value = type.newInstance();
				for (Field valueField : fields) {
					Object fieldValue = getValue(actualProvider, valueField.getType(), join(name, valueField.getName()), ctx);
					boolean acc = valueField.isAccessible();
					valueField.setAccessible(true);
					valueField.set(value, fieldValue);
					valueField.setAccessible(acc);
				}
			} catch (Exception e) {
				throw new IllegalStateException(String.format("%s - %s Value type %s does not support autowiring.", 
						bean.getClass().getSimpleName(),
						field.getName(),
						type.getName()), e);
				
			}
		} else {
			value = getValue(actualProvider, type, name, ctx);
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

	private Object getValue(ResourceProvider provider, Class<?> expectedType, String name,
			ResourceResolutionContext ctx) {
		Object value;
		if (ResourceValueReference.class.equals(expectedType)) {		
			value = new GenericResourceValueReference(provider, name);
		} else if (ResourceProvider.class.equals(expectedType)) {
			value = provider;
		} else {
			OptionalString string = provider.get(name, ctx);
			if (OptionalString.class.equals(expectedType)) {
				value = string;
			} else if (MandatoryString.class.equals(expectedType)) {
				value = string.notNull();
			} else {
				value = string.notNull().as(expectedType);
			}
		}
		return value;
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
