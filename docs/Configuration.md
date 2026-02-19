
Configuring resources
=====================

You can customize the Resources configuration to add additional data sources,
set up resource file formats or enable post-processing of loaded values.

To apply the configuration, use `ResourcesConfigurationBuilder`. The `configure()` method
starts a builder chain; call `.get()` at the end to produce a `RefreshableResourcesConfigurator`
that you pass to the `RefreshableResources` constructor:

```java
import static com.github.resource4j.resources.ResourcesConfigurationBuilder.configure;
import static com.github.resource4j.objects.providers.ResourceObjectProviders.*;
import static com.github.resource4j.objects.providers.resolvers.ResourceObjectProviderPredicates.*;
import static com.github.resource4j.resources.processors.BasicValuePostProcessor.macroSubstitution;

HeapResourceObjectRepository runtimeStorage = inHeap();

Resources resources = new RefreshableResources(
    configure()
        .defaultBundle("common")
        .sources(
            // load properties files from classpath
            classpathOf(Application.class.getClassLoader())
                .objectsLike(name(".\\.properties$")),

            // web resources from "www" folder
            filesIn("www")
                .objectsLike(name(".\\.html$").or(name(".\\.css$")))
                .acceptContext(i18n()),

            // load configuration files for application features
            // from feature-specific folders or runtime storage
            patternMatching()
                    .when(".feature1+", filesIn("/config/feature1"))
                    .when(".feature2+", filesIn("/config/feature2"))
                    .otherwise(runtimeStorage)
                .objectsLike(name(".\\.conf$"))
        )
        .formats(
            format(propertyMap(), ".properties"),
            format(configMap(), ".conf")
        )
        .postProcessingBy(macroSubstitution())
        .get());
```

Default bundle
--------------
The default bundle is the name of resource, which is used by default for keys without bundle specified.
This is a good place to put some common settings for your application.

Sources
-------
Sources define where the framework will look for your resources. Upon request, the framework
looks up for a resource object in all configured sources, loading the first discovered object.
All sources must implement at least `ResourceObjectProvider` interface.

You can use following sources provided by the framework:
* classpath (using given classloader)
* file system (in given folder)
* heap (for configuration data that can exist only in runtime)
* pattern matching - a composite source, that allows to select a source by file name pattern.

As an alternative to pattern matching source, you can configure following filters for each specific source separately:
* name
* resolution context

Namespace class `ResourceObjectProviderPredicates` contains some useful predicates for these filters.


The [Spring integration library](SpringIntegration.md) adds `SpringResourceObjectProvider` to support
discovery of resources via Spring Framework.

Formats
-------
The core library supports only standard `.properties` files,
however you can use our [parsers](Parsers.md) component to support HOCON configs and JSON bundles.

Post-processing
---------------
Once the value is loaded from source bundle, framework can process it via given post-processor.
`BasicValuePostProcessor` performs macro substitution — see [Expression language](BasicEL.md)
for the full syntax reference.

Quick example:

```properties
# my.properties
message=Hello, {name}!
name=John

details=You have been {:0} times here.
escaped=You can use macros like \{name\} and character '\\' to escape curly braces.
```

The returned values for this resource bundle will be:

```
my.message -> Hello, John!
my.details -> You have been {0} times here.
my.escaped -> You can use macros like {name} and character '\' to escape curly braces.
```

Failed macro substitutions and syntax errors will result in missing value. When casting to MandatoryValue, you'll
get `MissingValueException` with parse error message, as can be seen in this example:

```java
try {
    resources.get(key("my","error1"), withoutContext()).notNull().asIs();
} catch (MissingValueException e) {
    assertTrue(e.getCause() instanceof ValuePostProcessingException);
    ValuePostProcessingException ex = (ValuePostProcessingException) e;
    assertEquals("{unknown} message", ex.getPartialResult());
}
```

---

Previous: [Basics](Basics.md)
Next: [Expression language](BasicEL.md)
