package com.github.resource4j;

import java.util.Locale;

import javax.swing.Icon;

/**
 *
 * @author Ivan Gammel
 * @since 1.0
 */
public interface ResourceProvider {

    OptionalString get(String name, Locale locale);

    OptionalValue<Icon> icon(String name, Locale locale);

}
