package com.github.resource4j.resources.context;

import java.util.List;

public interface ResourceResolutionComponent {

	List<String> sections();

	ResourceResolutionComponent reduce();

	boolean isReducible();
	
}
