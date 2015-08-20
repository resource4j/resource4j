package com.github.resource4j.spring.annotations.support;

public class FileNamePattern {
	
	public static String build(Class<?> beanType, String beanName, String pattern) {
		StringBuilder path = new StringBuilder();
		if (!pattern.startsWith("/")) { // is relative path?
			path.append('/').append(beanType.getPackage().getName().replace('.', '/')).append('/');
		}
		path.append(pattern);
		int maskPosition = path.lastIndexOf("*");
		if (maskPosition >= 0) {
			path.replace(maskPosition, maskPosition+1, beanName != null ? beanName : beanType.getSimpleName());
		}
		return path.toString();
	}
}
