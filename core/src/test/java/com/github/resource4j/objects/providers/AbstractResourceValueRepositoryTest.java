package com.github.resource4j.objects.providers;

import com.github.resource4j.MissingValueException;
import com.github.resource4j.OptionalString;
import com.github.resource4j.ResourceKey;
import com.github.resource4j.objects.providers.mutable.ResourceObjectRepository;
import com.github.resource4j.objects.providers.mutable.ResourceValueRepository;
import org.junit.jupiter.api.Test;

import java.nio.file.Path;
import java.time.Clock;
import java.util.Locale;

import static com.github.resource4j.resources.context.ResourceResolutionContext.in;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public abstract class AbstractResourceValueRepositoryTest extends AbstractResourceObjectRepositoryTest {

    @Override
    protected final ResourceObjectRepository createRepository(Clock clock, Path tmp) {
        return doCreateRepository(clock);
    }

    protected abstract ResourceValueRepository doCreateRepository(Clock clock);

    protected ResourceValueRepository repository() {
        return (ResourceValueRepository) super.repository();
    }

    @Test
    public void shouldReturnValueAfterPut() {
        ResourceKey key = ResourceKey.key("bundle", "id");
        String someValue = "Some value";
        repository().put(key, in(Locale.US), someValue);
        OptionalString s = repository().get(key, in(Locale.US));
        assertEquals(someValue, s.notNull().asIs());
    }

    @Test
    public void shouldReturnMissingValueAfterRemoveLastValueInBundle() {
        assertThrows(MissingValueException.class, () -> {
            ResourceKey key = ResourceKey.key("bundle", "id");
            String someValue = "Some value";
            repository().put(key, in(Locale.US), someValue);
            repository().remove(key, in(Locale.US));
            OptionalString s = repository().get(key, in(Locale.US));
            s.notNull();
        });
    }

}
