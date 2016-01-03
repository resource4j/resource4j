package com.github.resource4j.resources.context;

import static com.github.resource4j.resources.context.ResourceResolutionContext.withoutContext;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class DefaultResolutionContextMatcher implements ResolutionContextMatcher {

	@Override
	public List<ResourceResolutionContext> matches(ResourceResolutionContext context) {
		LinkedList<ResourceResolutionContext> result = new LinkedList<>();
		enumerate(result, Arrays.asList(context.components()));
		result.add(withoutContext());
		return result;
	}

	private void enumerate(List<ResourceResolutionContext> result,  
			List<ResourceResolutionComponent> list) {
		if (!list.isEmpty()) {
			enumerateWithFirst(result, list.get(0), list);
			enumerate(result, list.subList(1, list.size()));
		}
	}

	/**
	 * @param result
	 * @param component
	 * @param list
	 */
	private void enumerateWithFirst(List<ResourceResolutionContext> result, 
			ResourceResolutionComponent component,
			List<ResourceResolutionComponent> list) {
		if (!list.isEmpty()) {
			List<ResourceResolutionComponent> reduced = list.subList(1, list.size());
			result.add(new ResourceResolutionContext(join(component, reduced)));
			ResourceResolutionComponent reducedComponent = component;
			while (reducedComponent.isReducible()) {
				reducedComponent = reducedComponent.reduce();
				result.add(new ResourceResolutionContext(join(reducedComponent, reduced)));
			}
			enumerateWithFirst(result, component, reduced);
		}
	}

	private ResourceResolutionComponent[] join(ResourceResolutionComponent component,
			List<ResourceResolutionComponent> list) {
		List<ResourceResolutionComponent> comps = new ArrayList<>();
		comps.add(component);
		comps.addAll(list);
		ResourceResolutionComponent[] array = comps.toArray(new ResourceResolutionComponent[0]);
		return array;
	}


}
