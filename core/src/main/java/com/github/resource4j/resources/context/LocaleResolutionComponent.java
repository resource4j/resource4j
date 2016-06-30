package com.github.resource4j.resources.context;

import java.util.List;
import java.util.Locale;
import java.util.Stack;
import java.util.stream.Collectors;

public class LocaleResolutionComponent extends StringResolutionComponent {

	private static final long serialVersionUID = 1123590L;
	
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
	
	public LocaleResolutionComponent reduce() {
		int size = sections().size();
		if (size < 2) {
			throw new IllegalStateException("Cannot reduce to empty locale");
		}
		String name = sections()
						.subList(0, size - 1)
						.stream()
						.collect(Collectors.joining("_"));		
		Locale locale = new Locale(name);
		return new LocaleResolutionComponent(locale);
	}

}
