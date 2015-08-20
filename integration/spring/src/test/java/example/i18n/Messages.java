package example.i18n;

import com.github.resource4j.MandatoryString;
import com.github.resource4j.resources.references.ResourceValueReference;
import com.github.resource4j.spring.annotations.InjectResource;
import com.github.resource4j.spring.annotations.InjectValue;
import com.github.resource4j.spring.context.RequestResolutionContextProvider;

public class Messages {
	
	@InjectValue
	private String welcome;
	
	@InjectValue("goodbye")
	private ResourceValueReference goodbyeValue;
	
	@InjectValue(resolvedBy = RequestResolutionContextProvider.class)
	private MandatoryString application;
	
	@InjectResource("*.txt")
	private String warning;

	public String getWelcome() {
		return welcome;
	}

	public ResourceValueReference getGoodbyeValue() {
		return goodbyeValue;
	}

	public MandatoryString getApplication() {
		return application;
	}

	public String getWarning() {
		return warning;
	}
		
}