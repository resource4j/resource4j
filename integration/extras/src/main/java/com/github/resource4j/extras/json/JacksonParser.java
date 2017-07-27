package com.github.resource4j.extras.json;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.resource4j.ResourceObject;
import com.github.resource4j.ResourceObjectException;
import com.github.resource4j.objects.parsers.AbstractValueParser;

import java.io.IOException;

/**
 * Parser for values in JSON format. Example:
 * <code>
 *     // parse to object of given type:
 *     MyType type = json(MyType.class).parse(resourceObject);
 *
 *     // parse to Json mode
 *     JsonNode node = json().parse(resourceObject);
 * </code>
 * @since 3.1
 * @param <T> type of parsed object, if specified.
 */
public class JacksonParser<T> extends AbstractValueParser {

    private Class<T> contentType;

    private ObjectMapper mapper;

    /**
     * Creates typed parser
     * @param contentType class of parsed object
     * @param <T> type of parsed object
     * @return parser of objects of given type
     */
    public static <T> JacksonParser<T> json(Class<T> contentType) {
        return new JacksonParser<>(null, contentType);
    }

    /**
     * Creates generic parser
     * @return parser that produces Jackson JsonNode objects
     */
    public static JacksonParser<JsonNode> json() {
        return new JacksonParser<>();
    }

    JacksonParser() {
        this(null, null);
    }

    JacksonParser(ObjectMapper mapper, Class<T> contentType) {
        this.mapper = mapper == null ? createMapper() : mapper;
        this.contentType = contentType;
    }

    private static ObjectMapper createMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.findAndRegisterModules();
        return mapper;
    }

    @Override
    protected T parse(ResourceObject object) throws IOException, ResourceObjectException {
        if (contentType != null) {
            return this.mapper.readValue(object.asStream(), contentType);
        } else {
            @SuppressWarnings({ "unchecked", "raw" })
            T tree = (T) this.mapper.readTree(object.asStream());
            return tree;
        }
    }

}
