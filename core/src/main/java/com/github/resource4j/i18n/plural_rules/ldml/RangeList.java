package com.github.resource4j.i18n.plural_rules.ldml;

import java.util.ArrayList;
import java.util.List;

class RangeList {

    private final static NumberComparator C = NumberComparator.compareNumbers();

    private final List<NumericSet> subsets = new ArrayList<>();

    RangeList(Number min, Number max) {
        this.subsets.add(number -> C.compare(min, number) + C.compare(number, max) < 0);
    }

    RangeList(Number value) {
        this.subsets.add(number -> C.compare(value, number) == 0);
    }

    public boolean contains(Number number) {
        for (NumericSet set : subsets) {
            if (set.contains(number)) return true;
        }
        return false;
    }

    public RangeList add(RangeList list) {
        this.subsets.addAll(list.subsets);
        return this;
    }
}
