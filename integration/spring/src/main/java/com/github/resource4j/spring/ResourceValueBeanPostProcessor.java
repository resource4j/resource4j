package com.github.resource4j.spring;

import static org.springframework.util.ReflectionUtils.doWithFields;

import java.lang.reflect.Field;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.util.ReflectionUtils.FieldFilter;

import com.github.resource4j.resources.Resources;
import com.github.resource4j.spring.annotations.InjectBundle;
import com.github.resource4j.spring.annotations.InjectResource;
import com.github.resource4j.spring.annotations.InjectValue;
import com.github.resource4j.spring.annotations.support.AutowiredResourceCallback;
import com.github.resource4j.spring.annotations.support.InjectBundleCallback;
import com.github.resource4j.spring.annotations.support.InjectResourceCallback;
import com.github.resource4j.spring.annotations.support.InjectValueCallback;

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
		doWithFields(bean.getClass(), 
			new AutowiredResourceCallback(resources, bean, beanName), 
			new FieldFilter() {
				@Override
				public boolean matches(Field field) {
					return field.isAnnotationPresent(AutowiredResource.class);
				}
		});
		doWithFields(bean.getClass(), 
				new InjectValueCallback(beanFactory, resources, bean, beanName), 
				new FieldFilter() {
					@Override
					public boolean matches(Field field) {
						return field.isAnnotationPresent(InjectValue.class);
					}
		});
		doWithFields(bean.getClass(), 
				new InjectBundleCallback(resources, bean, beanName), 
				new FieldFilter() {
					@Override
					public boolean matches(Field field) {
						return field.isAnnotationPresent(InjectBundle.class);
					}
		});
		doWithFields(bean.getClass(), 
				new InjectResourceCallback(resources, bean, beanName), 
				new FieldFilter() {
					@Override
					public boolean matches(Field field) {
						return field.isAnnotationPresent(InjectResource.class);
					}
		});
		return bean;
	}
	


}
