package com.github.resource4j.i18n;

import java.util.function.Predicate;

public class GenericPluralizationCase implements PluralizationCase {

    private final PluralCategory category;
    private final Predicate<Number> predicate;

    public static GenericPluralizationCase aCase(Predicate<Number> predicate,
                                                 PluralCategory category) {
        return new GenericPluralizationCase(category, predicate);
    }

    public GenericPluralizationCase(PluralCategory category,
                                    Predicate<Number> predicate) {
        this.category = category;
        this.predicate = predicate;
    }

    @Override
    public PluralCategory category() {
        return this.category;
    }

    @Override
    public boolean includes(Number number) {
        return this.predicate.test(number);
    }
}
