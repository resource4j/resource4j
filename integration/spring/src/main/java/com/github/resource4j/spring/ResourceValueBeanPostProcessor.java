package com.github.resource4j.spring;

import static com.github.resource4j.ResourceKey.key;
import static org.springframework.util.ReflectionUtils.doWithFields;

import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.util.ReflectionUtils.FieldFilter;

import com.github.resource4j.ResourceKey;
import com.github.resource4j.resources.ResourceProvider;
import com.github.resource4j.resources.Resources;
import com.github.resource4j.spring.annotations.InjectBundle;
import com.github.resource4j.spring.annotations.InjectResource;
import com.github.resource4j.spring.annotations.InjectValue;
import com.github.resource4j.spring.annotations.support.AutowiredResourceCallback;
import com.github.resource4j.spring.annotations.support.InjectResourceCallback;
import com.github.resource4j.spring.annotations.support.InjectValueCallback;

@SuppressWarnings("deprecation")
public class ResourceValueBeanPostProcessor implements BeanPostProcessor, BeanFactoryAware {

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
		
		ResourceProvider provider = getResourceProvider(bean);
		
		doWithFields(bean.getClass(), 
			new AutowiredResourceCallback(resources, bean, beanName), 
			new FieldFilter() {
				@Override
				public boolean matches(Field field) {
					return field.isAnnotationPresent(AutowiredResource.class);
				}
		});
		doWithFields(bean.getClass(), 
				new InjectValueCallback(bean, beanFactory, resources, provider), 
				new FieldFilter() {
					@Override
					public boolean matches(Field field) {
						return field.isAnnotationPresent(InjectValue.class);
					}
		});
		doWithFields(bean.getClass(), 
				new InjectResourceCallback(bean, beanName, beanFactory, resources), 
				new FieldFilter() {
					@Override
					public boolean matches(Field field) {
						return field.isAnnotationPresent(InjectResource.class);
					}
		});
		return bean;
	}

	private ResourceProvider getResourceProvider(Object bean) {
		ResourceProvider provider = getProvider(bean.getClass().getName(), bean.getClass());
		if (provider == null) {
			for (Package pckg : packagesOf(bean.getClass())) {
				provider = getProvider(pckg.getName(), pckg);
				if (provider != null) {
					return provider;
				}
			}
		}
		return null;
	}

	private Iterable<Package> packagesOf(Class<?> clazz) {
		String name = clazz.getPackage().getName();
		String[] components = name.split("\\.");
		
		List<Package> packages = new ArrayList<>();
		StringBuilder builder = new StringBuilder();
		for (int i = 0; i < components.length; i++) {
			if (builder.length() > 0) {
				builder.append('.');
			}
			builder.append(components[i]);
			Package pckg = Package.getPackage(builder.toString());
			if (pckg != null) {
				packages.add(pckg);
			}
		}
		return packages;
	}

	private ResourceProvider getProvider(String name, AnnotatedElement element) {
		InjectBundle annotation = element.getAnnotation(InjectBundle.class);
		if (annotation == null) {
			return null;
		}
		String id = annotation.id().isEmpty() ? null : annotation.id();
		ResourceKey key;
		if (annotation.value().isEmpty()) {
			key = key(name, id);
		} else {
			key = key(annotation.value(), id);
		}
		return resources.forKey(key);
	}
	


}
