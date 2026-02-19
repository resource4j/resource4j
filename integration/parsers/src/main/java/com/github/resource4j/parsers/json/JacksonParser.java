package com.github.resource4j.parsers.json;

import com.github.resource4j.ResourceObject;
import com.github.resource4j.ResourceObjectException;
import com.github.resource4j.objects.parsers.AbstractValueParser;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.json.JsonMapper;

import java.io.IOException;
import java.io.InputStream;

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
public class JacksonParser<T> extends AbstractValueParser<T> {

    private final Class<T> contentType;

    private final JsonMapper mapper;

    /**
     * Creates typed parser using shared instance of JsonMapper
     * @param contentType class of parsed object
     * @param <T> type of parsed object
     * @return parser of objects of given type
     */
    public static <T> JacksonParser<T> json(Class<T> contentType) {
        return new JacksonParser<>(null, contentType);
    }

    /**
     * Creates typed parser using given json mapper from Jackson
     * @param mapper the mapper used to create the parser and parse the object
     * @param contentType class of parsed object
     * @param <T> type of parsed object
     * @return parser of objects of given type
     */
    public static <T> JacksonParser<T> json(JsonMapper mapper, Class<T> contentType) {
        return new JacksonParser<>(mapper, contentType);
    }

    /**
     * Creates generic parser using shared instance of JsonMapper
     * @return parser that produces JsonNode objects
     */
    public static JacksonParser<JsonNode> json() {
        return new JacksonParser<>();
    }

    JacksonParser() {
        this(null, null);
    }

    JacksonParser(JsonMapper mapper, Class<T> contentType) {
        this.mapper = mapper != null ? mapper : JsonMapper.shared();
        this.contentType = contentType;
    }

    @Override
    @SuppressWarnings("unchecked")
    protected T parse(ResourceObject object) throws IOException, ResourceObjectException {
        try (InputStream stream = object.asStream()) {
            if (contentType != null) {
                return mapper.readValue(stream, contentType);
            } else {
                return (T) mapper.readTree(stream);
            }
        }
    }

}
