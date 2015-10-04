package com.github.resource4j.resources.resolution;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class StringResolutionComponent implements ResourceResolutionComponent, java.io.Serializable {

	private static final long serialVersionUID = "3.0".hashCode();
	
	private List<String> sections;

	public StringResolutionComponent(String... sections) {
		this(Arrays.asList(sections));
	}

	public StringResolutionComponent(List<String> sections) {
		this.sections = Collections.unmodifiableList(sections);
	}

	@Override
	public List<String> sections() {
		return this.sections;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + sections.hashCode();
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof StringResolutionComponent))
			return false;
		StringResolutionComponent other = (StringResolutionComponent) obj;
		if (!sections.equals(other.sections))
			return false;
		return true;
	}

	@Override
	public StringResolutionComponent reduce() {
		if (sections.size() < 2) {
			throw new IllegalStateException("Cannot reduce to empty component");
		}
		return new StringResolutionComponent(sections.subList(0, sections.size() - 1));
	}

	@Override
	public boolean isReducible() {
		return sections.size() > 1;
	}

	@Override
	public String toString() {
		return sections.stream().collect(Collectors.joining("_"));
	}
	
}
