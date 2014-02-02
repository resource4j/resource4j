package com.github.resource4j.resources.resolution;

import java.util.*;

public class LocaleResolutionComponent extends StringResolutionComponent {

	private static final long serialVersionUID = "2.0".hashCode();
	
	private final Locale locale;

	public LocaleResolutionComponent(Locale locale) {
		super(buildSections(locale));
		this.locale = locale;
	}

	public Locale locale() {
		return locale;
	}
	
	private static List<String> buildSections(Locale locale) {
		Stack<String> stack = new Stack<>();
		stack.push(locale.getLanguage());
		stack.push(locale.getCountry());
		stack.push(locale.getVariant());
		stack.push(locale.getScript());
		while (!stack.isEmpty() && "".equals(stack.peek())) {
			stack.pop();
		}
		return stack;
	}

}
