package com.github.resource4j.resources;

import com.github.resource4j.MandatoryString;
import com.github.resource4j.resources.processors.BasicValuePostProcessor;
import org.junit.Test;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;

import static com.github.resource4j.ResourceKey.key;
import static com.github.resource4j.objects.providers.ResourceObjectProviders.classpath;
import static com.github.resource4j.resources.ResourcesConfigurationBuilder.configure;
import static com.github.resource4j.resources.context.ResourceResolutionContext.with;
import static com.github.resource4j.resources.context.ResourceResolutionContext.withoutContext;
import static org.junit.Assert.assertEquals;

public class ParamValueResolutionTest {

    private Resources resources = new RefreshableResources(configure()
            .sources(classpath())
            .postProcessingBy(new BasicValuePostProcessor())
            .get());

    @Test
    public void testMultipleStringsSameParamWithPluralizationOne() {
        MandatoryString value = resources.get(key("temporal", "years"), with("count", 2)).notNull();
        assertEquals("years", value.asIs());
    }


    @Test
    public void testPatternMatchingWithPluralizationOne() {
        MandatoryString value = resources.get(key("fruit", "apples"), with("count", 1)).notNull();
        assertEquals("Count: one apple", value.asIs());
    }

    @Test
    public void testPatternMatchingWithPluralizationOther() {
        MandatoryString value = resources.get(key("fruit", "apples"), with("count", 2)).notNull();
        assertEquals("Count: two apples", value.asIs());
    }

    @Test
    public void testDateFormatPropertyResolution() {
        LocalDate date = LocalDate.of(2017, 1, 1);
        MandatoryString value = resources.get(key("fruit", "today"), with("date", date)).notNull();
        assertEquals("Today is 01-01-2017", value.asIs());
    }

    @Test
    public void testUpperCaseFirstCharacterPropertyResolution() {
        MandatoryString value = resources.get(key("fruit", "upper"), withoutContext()).notNull();
        assertEquals("Apple", value.asIs());
    }


    @Test
    public void testPropertyResoltionWithParameters() {
        MandatoryString value = resources.get(key("fruit", "caps"), with("count", 1)).notNull();
        assertEquals("APPLE", value.asIs());
    }

}
