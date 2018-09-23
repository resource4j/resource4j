package example.document;

import com.github.resource4j.ResourceObjectReference;
import com.github.resource4j.spring.annotations.InjectResource;
import org.springframework.stereotype.Component;

@Component
public class DocumentTemplate {

	@InjectResource("/example/document/logo.png")
	private byte[] logo;
	
	@InjectResource(value = "*.txt", required = false)
	private ResourceObjectReference content;

	public byte[] getLogo() {
		return logo;
	}

	public ResourceObjectReference getContent() {
		return content;
	}
	
}
