package com.github.resource4j.i18n;

public interface PluralizationCase {

    PluralCategory category();

    boolean includes(Number number);

}
