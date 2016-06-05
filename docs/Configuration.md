
Configuring resources
=====================

You can customize the Resources configuration to add additional data sources,
set up resource file formats or enable post-processing of loaded values.

To apply the configuration, you must use ResourcesConfigurationBuilder as following:
```Java
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
                        .when(".feature1+", filesIn("/config/feature1")
                        .when(".feature2+", filesIn("/config/feature2"))
                        .otherwise(runtimeStorage)
                    .objectsLike(name(".\\.conf$"))
            )
            .formats(
                format(propertyMap(), ".properties"),
                format(configMap(), ".conf")
            )
            .postProcessingBy(macroSubstitution())
    );
```

Default bundle
--------------
The default bundle is the name of resource, which is used by default for keys without bundle specified.
This is a good place to put some common settings for your application.

Sources
-------
Sources define where the framework will look for your resources. Upon request, the framework
looks up for a resource object in all configured sources, loading the first discovered object.
All sources must implement at least ResourceObjectProvider interface.

You can use following sources provided by the framework:
* classpath (using given classloader)
* file system (in given folder)
* heap (for configuration data that can exist only in runtime)
* pattern matching - a composite source, that allows to select a source by file name pattern.

As an alternative to pattern matching source, you can configure following filters for each specific source separately:
* name
* resolution context

Namespace class ResourceObjectProviderPredicates contains some useful predicates for these filters.


The [Spring integration library](SpringIntegration.md) adds SpringResourceObjectProvider to support
discovery of resources via Spring Framework.

Formats
-------
The core library supports only standard .properties files,
however you can use our [extras](Extras.md) component to support HOCON configs.

Post-processing
---------------
Once the value is loaded from source bundle, framework can process it via given post-processor.
BasicValuePostProcessor performs macro substitution, using following syntax:

    File my.properties

    message=Hello, {name}!
    name=John

    details=You have been {:0} times here.
    escaped=You can use macros like \{name\} and character '\\' to escape curly braces.

    incorrect1={unknown} message
    incorrect2=\{bad} escaping
    incorrect3=Not closed { oops!

The returned values for this resource bundle will be:

    my.message -> Hello, John!
    my.details -> You have been {0} times here.
    my.escaped -> You can use macros like {name} and character '\' to escape curly braces.

Failed macro substitutions and syntax errors will result in missing value. When casting to MandatoryValue, you'll
get MissingValueException with parse error message, as can be seen in this example:
```Java
    try {
        resources.get(key("my","error1"), withoutContext()).notNull().asIs();
    } catch (MissingValueException e) {
        assertTrue(e.getCause() instanceof ValuePostProcessingException);
        ValuePostProcessingException ex = (ValuePostProcessingException) e;
        assertEquals("{unknown} message", ex.getPartialResult());
    }
```

What's next?
------------
3. [Integration with Spring Framework](docs/SpringIntegration.md)
4. [Integration with Thymeleaf 2.1](docs/ThymeleafIntegration.md)
5. [Extras](docs/Extras.md)
