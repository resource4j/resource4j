package com.github.resource4j;

import java.util.Locale;

import javax.swing.Icon;

/**
 *
 * @author Ivan Gammel
 * @since 1.0
 */
public interface Resources {

    String ICON = "icon";

    String LABEL = "label";

    OptionalString get(ResourceKey key, Locale locale);

    OptionalString get(String key, Locale locale);

    <T> OptionalString get(Class<T> clazz, String key, Locale locale);

    <E extends Enum<E>> OptionalString get(E value, String key, Locale locale);

    OptionalValue<Icon> icon(ResourceKey key, Locale locale);

    ResourceProvider forKey(ResourceKey key);

}
