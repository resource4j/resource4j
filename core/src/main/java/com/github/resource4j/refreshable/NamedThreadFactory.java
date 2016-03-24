package com.github.resource4j.refreshable;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

class NamedThreadFactory implements ThreadFactory {
    private final AtomicInteger counter = new AtomicInteger();
    private final String name;
    private static final String THREAD_NAME_PATTERN = "%s-%d";

    public NamedThreadFactory(String name) {
        this.name = name;
    }

    @Override
    public Thread newThread(Runnable r) {
        final String threadName = String.format(THREAD_NAME_PATTERN, name, counter.incrementAndGet());
        return new Thread(r, threadName);

    }
}
