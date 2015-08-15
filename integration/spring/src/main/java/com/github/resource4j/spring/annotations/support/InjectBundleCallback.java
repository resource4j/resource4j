package com.github.resource4j.spring.annotations.support;

import java.lang.reflect.Field;

import org.springframework.util.ReflectionUtils.FieldCallback;

import com.github.resource4j.resources.Resources;

public class InjectBundleCallback implements FieldCallback {

	public InjectBundleCallback(Resources resources, Object bean, String beanName) {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void doWith(Field field) throws IllegalArgumentException, IllegalAccessException {
		// TODO Auto-generated method stub

	}

}
