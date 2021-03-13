package com.github.resource4j.test;

import java.io.Serializable;
import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.time.ZoneId;

public class TestClock extends Clock implements Serializable  {
    private static final long serialVersionUID = 7430389292664866958L;
    private final Instant instant;
    private final ZoneId zone;
    private Duration duration = Duration.ZERO;

    public static TestClock testFixed(Clock baseClock) {
        return new TestClock(baseClock);
    }

    TestClock(Clock baseClock) {
        this.instant = baseClock.instant();
        this.zone = baseClock.getZone();
    }
    @Override
    public ZoneId getZone() {
        return zone;
    }
    @Override
    public Clock withZone(ZoneId zone) {
        if (zone.equals(this.zone)) {  // intentional NPE
            return this;
        }
        return Clock.fixed(instant(), zone);
    }
    @Override
    public long millis() {
        return instant().toEpochMilli();
    }
    @Override
    public Instant instant() {
        return instant;
    }
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof TestClock) {
            TestClock other = (TestClock) obj;
            return instant().equals(other.instant()) && zone.equals(other.zone);
        }
        return false;
    }
    @Override
    public int hashCode() {
        return instant().hashCode() ^ zone.hashCode();
    }
    @Override
    public String toString() {
        return "TestClock[" + instant() + "," + zone + "]";
    }

    public static DSL whenPassed(Duration duration) {
        return new DSL(duration);
    }

    public void reset() {
        this.duration = Duration.ZERO;
    }

    public static final class DSL {
        private Duration duration;
        private DSL(Duration duration) {
            this.duration = duration;
        }
        public void on(TestClock clock) {
            clock.duration = clock.duration.plus(this.duration);
        }
    }

}
