package com.github.resource4j.converters.impl;


import java.time.DateTimeException;
import java.time.ZoneId;

public final class TimeConversions {

    private TimeConversions() {
    }

    public static ZoneId toZoneId(Object zoneId) throws DateTimeException {
        ZoneId zone = ZoneId.of("UTC");
        if (zoneId != null) {
            if (zoneId instanceof String) {
                zone = ZoneId.of((String) zoneId);
            }
        }
        return zone;
    }

}
