package com.github.resource4j.i18n.plural_rules;

public interface PluralCase {

    PluralCategory category();

    boolean includes(Number number);

}
