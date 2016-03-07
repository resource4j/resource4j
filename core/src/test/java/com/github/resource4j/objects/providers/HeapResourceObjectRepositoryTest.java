package com.github.resource4j.objects.providers;

import com.github.resource4j.objects.providers.mutable.HeapResourceObjectRepository;
import com.github.resource4j.objects.providers.mutable.ResourceObjectRepository;

import java.time.Clock;

public class HeapResourceObjectRepositoryTest extends AbstractResourceObjectRepositoryTest {

    @Override
    protected ResourceObjectRepository createRepository(Clock clock) {
        return new HeapResourceObjectRepository(clock);
    }

}
