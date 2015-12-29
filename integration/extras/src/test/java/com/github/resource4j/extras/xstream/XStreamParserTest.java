package com.github.resource4j.extras.xstream;

import static com.github.resource4j.ResourceKey.bundle;
import static com.github.resource4j.extras.xstream.XStreamParser.xml;
import static org.junit.Assert.assertEquals;

import java.io.IOException;

import com.github.resource4j.ResourceObject;
import org.junit.Test;

import com.github.resource4j.generic.objects.ByteArrayResourceObject;
import com.github.resource4j.ResourceObjectException;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

public class XStreamParserTest {

	@XStreamAlias("model")
	public static class Model {
		@XStreamAsAttribute
		private String message;
		@XStreamAlias("value")
		private int value;
		public String getMessage() {
			return message;
		}
		public void setMessage(String message) {
			this.message = message;
		}
		public int getValue() {
			return value;
		}
		public void setValue(int value) {
			this.value = value;
		}		
	} 

	@Test
	public void testXStreamParser() throws ResourceObjectException, IOException {
		String xml = "<?xml version=\"1.0\" encoding=\"utf-8\"?><model message=\"Lorem ipsum\"><value>1</value></model>";
		
		ResourceObject file = new ByteArrayResourceObject("test", "test", xml.getBytes(), 0);
		Model object = file.parsedTo(xml(Model.class)).asIs();
		assertEquals("Lorem ipsum", object.getMessage());
		assertEquals(1, object.getValue());
	}
	
	
}
