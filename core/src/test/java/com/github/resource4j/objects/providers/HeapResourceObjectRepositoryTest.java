package com.github.resource4j.objects.providers;

import com.github.resource4j.objects.providers.mutable.HeapResourceObjectRepository;
import com.github.resource4j.objects.providers.mutable.ResourceObjectRepository;
import com.github.resource4j.objects.providers.mutable.ResourceValueRepository;

import java.time.Clock;

public class HeapResourceObjectRepositoryTest extends AbstractResourceValueRepositoryTest {

    @Override
    protected ResourceValueRepository doCreateRepository(Clock clock) {
        return new HeapResourceObjectRepository(clock);
    }

}
