package com.github.resource4j.extras.xstream;

import com.github.resource4j.ResourceObject;
import com.github.resource4j.ResourceObjectException;
import com.github.resource4j.objects.parsers.AbstractValueParser;
import com.github.resource4j.objects.parsers.ResourceObjectFormatException;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.XStreamException;
import com.thoughtworks.xstream.io.xml.StaxDriver;

import java.io.IOException;
import java.io.InputStream;

public class XStreamParser<T> extends AbstractValueParser<T> {

	private Class<T> type;
	
	private XStream xstream;
	
	public static <T> XStreamParser<T> xml(Class<T> type) {
		XStream xstream = new XStream(new StaxDriver());
		XStream.setupDefaultSecurity(xstream);
		xstream.allowTypesByWildcard(new String[] {
				type.getPackage().getName() + ".**"
		});
		xstream.processAnnotations(type);
		return new XStreamParser<>(type, xstream);
	}

	public static <T> XStreamParser<T> xml(Class<T> type, XStream xstream) {
		return new XStreamParser<>(type, xstream);
	}
	
	public XStreamParser(Class<T> type, XStream xstream) {
		this.type = type;
		this.xstream = xstream;
	}
	
	@Override
	protected T parse(ResourceObject file) throws IOException, ResourceObjectException {
		Object object = null;
		try (InputStream stream = file.asStream()) {
			object = xstream.fromXML(stream);
			return type.cast(object);
		} catch (XStreamException e) {
			throw new ResourceObjectFormatException(file, "Cannot parse resource object");
		} catch (ClassCastException e) {
			throw new ResourceObjectFormatException(file,
					"Resource data type " + object.getClass().getName() 
					+ " does not match requested " + type.getName());
		}
	}

}
