package com.github.resource4j.i18n.plural_rules;

import java.util.Locale;

public interface PluralRule {

    Locale language();

    PluralCategory pluralize(int number);

}
