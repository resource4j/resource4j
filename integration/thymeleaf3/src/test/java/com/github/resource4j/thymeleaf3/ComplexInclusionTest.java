package com.github.resource4j.thymeleaf3;

import com.github.resource4j.resources.RefreshableResources;
import com.github.resource4j.resources.RefreshableResourcesConfigurator;
import com.github.resource4j.resources.Resources;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import static com.github.resource4j.objects.parsers.ResourceParsers.propertyMap;
import static com.github.resource4j.objects.providers.ResourceObjectProviders.bind;
import static com.github.resource4j.objects.providers.ResourceObjectProviders.classpath;
import static com.github.resource4j.objects.providers.resolvers.ResourceObjectProviderPredicates.name;
import static com.github.resource4j.resources.BundleFormat.format;
import static com.github.resource4j.resources.ResourcesConfigurationBuilder.configure;
import static com.github.resource4j.resources.processors.BasicValuePostProcessor.macroSubstitution;

public class ComplexInclusionTest {

    public static RefreshableResourcesConfigurator configurator() {
        return configure()
                .sources(bind(classpath())
                            .to("complex/templates")
                            .objectsLike(name(".\\.html$")),
                         bind(classpath())
                            .to("complex/strings")
                            .objectsLike(name(".\\.properties$")))
                .formats(format(propertyMap(), ".properties"))
                .postProcessingBy(macroSubstitution())
                .get();
    }

    private Resource4jTemplateEngine engine;

    @BeforeEach
    public void setupEngine() {
        Resources resources = new RefreshableResources(configurator());
        engine = new Resource4jTemplateEngine(resources, templates -> templates.setSuffix(".html"));
    }

    @Test
    public void testRenderWithInclusions() {
        Context context = new Context(new Locale("en", "US"));
        Map<String, Object> map = new HashMap<>();
        map.put("count", 1);
        context.setVariable("model", map);
        String content = engine.process("index", context);
        System.out.println(content);
    }

}
