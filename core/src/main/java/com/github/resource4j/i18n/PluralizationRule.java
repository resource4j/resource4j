package com.github.resource4j.i18n;

import java.util.Locale;

public interface PluralizationRule {

    Locale language();

    PluralCategory pluralize(int number);

}
