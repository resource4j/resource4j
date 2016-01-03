package com.github.resource4j.spring.annotations.support;

import static com.github.resource4j.resources.context.ResourceResolutionContext.withoutContext;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.util.ReflectionUtils.FieldCallback;

import com.github.resource4j.MandatoryValue;
import com.github.resource4j.OptionalValue;
import com.github.resource4j.MissingResourceObjectException;
import com.github.resource4j.objects.parsers.ByteArrayParser;
import com.github.resource4j.objects.parsers.ResourceParser;
import com.github.resource4j.objects.parsers.StringParser;
import com.github.resource4j.resources.Resources;
import com.github.resource4j.resources.context.ResourceResolutionContext;
import com.github.resource4j.spring.annotations.InjectResource;
import com.github.resource4j.spring.context.ResolutionContextProvider;

public class InjectResourceCallback implements FieldCallback {
	
	private static final Logger LOG = LoggerFactory.getLogger(InjectValueCallback.class);

	private Object bean;
	
	private BeanFactory beanFactory;
	
	private Resources resources;

	private String beanName;

	public InjectResourceCallback(Object bean, String beanName, BeanFactory beanFactory, Resources resources) {
		this.bean = bean;
		this.beanName = beanName;
		this.beanFactory = beanFactory;
		this.resources = resources;
	}

	@Override
	public void doWith(Field field) throws IllegalArgumentException, IllegalAccessException {
		
		Class<?> type = field.getType();
		InjectResource annotation = field.getAnnotation(InjectResource.class);
		
		ResourceResolutionContext context = getContext(annotation.resolvedBy());

		String fileName = fileSpecifiedBy(annotation);
		
		Object value = null;
		try {
			@SuppressWarnings({ "unchecked", "rawtypes" })
			OptionalValue<? extends Object> optional = resources.contentOf(fileName, context)
					.parsedTo((ResourceParser) formatSpecifiedBy(type, annotation));
			
			if (type.equals(OptionalValue.class) && genericMatch(field, optional.asIs())) {
				value = optional;
			} else if (type.equals(MandatoryValue.class) && genericMatch(field, optional.asIs())) {
				value = optional.notNull();
			} else if (optional.asIs() == null) {
				value = null;
			}  else if (type.isAssignableFrom(optional.asIs().getClass())) {
				value = optional.asIs();
			} else {
				throw new IllegalArgumentException("Incompatible data type: expected " 
						+ type.getName() + " for content of " + fileName);
			}
		} catch (MissingResourceObjectException e) {
			// ignore
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
				fileName);
		
	}

	private boolean genericMatch(Field field, Object value) {
		if (value == null) {
			return true;
		}
		Type genericType = field.getGenericType();
		if (genericType instanceof ParameterizedType) {
			ParameterizedType parameterizedType = (ParameterizedType) genericType;
			Type[] args = parameterizedType.getActualTypeArguments();
			if (args.length == 1) {
				if (args[0] instanceof Class) {
					return value.getClass().equals(args[0]);
				} 
				// TODO: add support for wildcard types here				
			}
		} 
		return false;
	}

	private String fileSpecifiedBy(InjectResource annotation) {
		String name = annotation.value().isEmpty() ? bean.getClass().getName() : 
			FileNamePattern.build(bean.getClass(), beanName, annotation.value());
		return name;
	}

	@SuppressWarnings({"unchecked", "rawtypes"})
	private ResourceParser<? extends Object, ? extends OptionalValue<?>> formatSpecifiedBy(Class<?> type, InjectResource annotation) {
		Class<? extends ResourceParser> parserType = annotation.parsedBy();
		
		ResourceParser parser = null;
		if (parserType.equals(ResourceParser.class)) {
			if (type.equals(byte[].class)) {
				parser = ByteArrayParser.getInstance();
			} else if (type.equals(String.class)) {
				parser = StringParser.getInstance();
			}
		} else {
			try {
				parser = beanFactory.getBean(parserType);
			} catch (NoSuchBeanDefinitionException e) {
				try {
					parser = parserType.newInstance();
				} catch (Exception ex) {
					// ignore
				}
			}
		}
		if (parser == null) {
			throw new IllegalArgumentException("Cannot instantiate parser: " + parserType.getName());	
		}
		return parser;
	}

	private ResourceResolutionContext getContext(Class<? extends ResolutionContextProvider> contextProviderType) {
		ResourceResolutionContext ctx = withoutContext();
		try {
			ResolutionContextProvider provider = 
				(ResolutionContextProvider) beanFactory.getBean(contextProviderType);
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
