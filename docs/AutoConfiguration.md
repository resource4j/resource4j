Auto-configuration with Spring Boot
====================================

Overview
--------
Resource4j provides Spring Boot auto-configuration that sets up all necessary beans
with sensible defaults. There are two auto-configuration modules:

- **resource4j-spring** (`Resource4jAutoConfiguration`) — core auto-configuration
  providing `Resources`, `MessageSource`, annotation processing, and Spring EL support.
- **resource4j-autoconfigure-thymeleaf3** (`Resource4jThymeleafAutoConfiguration`) —
  adds Thymeleaf template and message resolution on top of the core configuration.

What gets configured
--------------------

### Core auto-configuration (resource4j-spring)

Adding `resource4j-spring` as a dependency automatically registers:

- **`Resources`** — a `RefreshableResources` instance configured with classpath source,
  `.properties` format, and macro substitution post-processing.
- **`ResourceValueBeanPostProcessor`** — enables `@InjectValue`, `@InjectBundle`,
  and `@InjectResource` annotation processing.
- **`SpringELValuePostProcessor`** — enables Spring Expression Language in resource values.
- **`SpringResourceObjectProvider`** — resolves resources through Spring's resource abstraction.
- **`MessageSource`** — a `Resource4jMessageSource` bridging Resource4j to Spring's
  message resolution.

### Thymeleaf auto-configuration (resource4j-autoconfigure-thymeleaf3)

Adding `resource4j-autoconfigure-thymeleaf3` additionally registers:

- **`Resource4jTemplateResolver`** — resolves Thymeleaf templates through Resource4j,
  respecting `spring.thymeleaf.*` properties (prefix, suffix, mode, encoding, caching).
- **`Resource4jMessageSource`** — replaces the default `MessageSource` with one backed
  by Resource4j.
- **Context-aware template engine** — wraps the `SpringTemplateEngine` to pass locale
  and custom attributes to the template resolver.

Customizing the configuration
-----------------------------
To customize the default `Resources` configuration, define a `RefreshableResourcesConfigurator` bean:

```java
import static com.github.resource4j.resources.ResourcesConfigurationBuilder.configure;
import static com.github.resource4j.objects.providers.ResourceObjectProviders.*;

@Bean
public RefreshableResourcesConfigurator resourcesConfiguration(SpringResourceObjectProvider springResourceObjects) {
    return configure()
            .defaultBundle("i18n.messages")
            .sources(
                patternMatching()
                    .when(".+\\.properties$", bind(springResourceObjects).to("classpath:/i18n"))
                    .otherwise(bind(springResourceObjects).to("classpath:/templates")))
            .postProcessingBy(macroSubstitution())
            .get();
}
```

When a `RefreshableResourcesConfigurator` bean is present, it replaces the default configuration
entirely. The auto-configured `Resources` bean is `@ConditionalOnMissingBean`, so you can
also define your own `Resources` bean to take full control.

### Adding bundle formats

Define additional `BundleFormat` beans to support more file formats. The auto-configuration
collects all `BundleFormat` beans and passes them to the `Resources` configuration:

```java
import static com.github.resource4j.resources.BundleFormat.format;
import static com.github.resource4j.parsers.json.JacksonBundleParser.jsonMap;

@Bean
public BundleFormat jsonFormat() {
    return format(jsonMap(), ".json");
}
```

### Custom context attributes for Thymeleaf

Implement `ContextSpecificAttributeResolver` and register it as a bean to pass custom
attributes to the template resolver. The built-in `LocaleAttributeResolver` passes the
locale; you can add your own for tenant, theme, or other context dimensions.

---

Previous: [Integration with Thymeleaf](ThymeleafIntegration.md)
Next: [Parsers](Parsers.md)
