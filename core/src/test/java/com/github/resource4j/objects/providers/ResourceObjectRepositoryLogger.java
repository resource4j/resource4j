package com.github.resource4j.objects.providers;

import com.github.resource4j.objects.providers.events.ResourceObjectRepositoryEvent;
import com.github.resource4j.objects.providers.events.ResourceObjectRepositoryListener;
import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.hamcrest.Matcher;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public class ResourceObjectRepositoryLogger implements ResourceObjectRepositoryListener {

    private List<ResourceObjectRepositoryEvent> events = new ArrayList<>();

    @Override
    public void repositoryUpdated(ResourceObjectRepositoryEvent event) {
        events.add(event);
    }

    public int received() {
        return events.size();
    }

    public static Matcher<ResourceObjectRepositoryLogger> contain(Predicate<ResourceObjectRepositoryEvent> matcher) {
        return new BaseMatcher<ResourceObjectRepositoryLogger>() {
            @Override
            public boolean matches(Object o) {
                ResourceObjectRepositoryLogger logger = (ResourceObjectRepositoryLogger) o;
                return logger.events.stream().filter(matcher).findAny().isPresent();
            }
            @Override
            public void describeTo(Description description) {
                description.appendText("contain " + matcher);
            }
        };
    }


}
