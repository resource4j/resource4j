Integration with Spring Framework
=================================

Overview
--------------------
You can easily configure an implementation of Resources interface in DI container.
To use the advanced features of Resource4j Spring integration add
resource4j-spring library dependency to your project. These features include:

1. Support of annotation-driven injection using **ResourceValueBeanPostProcessor**.
2. Support for Spring-based discovery of resource bundles.
3. Support for session-scoped and request-scoped beans.

Configuration
-------------
Resource4j supports Spring Boot auto-configuration. By adding **resource4j-spring** library
as a dependency you will automatically get pre-configured Resource4j bean.

You can customize Resource4j configuration by adding a RefreshableResourcesConfigurator bean,
as shown in example below:

```Java
import static com.github.resource4j.resources.ResourcesConfigurationBuilder.configure;
import static com.github.resource4j.objects.providers.ResourceObjectProviders.bind;
import static com.github.resource4j.objects.providers.ResourceObjectProviders.patternMatching;

@Bean
public RefreshableResourcesConfigurator resourcesConfiguration(SpringResourceObjectProvider springResourceObjects) {
    return configure().sources(
                patternMatching()
                    .when(".+\\.properties$", bind(springResourceObjects).to("classpath:/i18n"))
                    .otherwise(bind(springResourceObjects).to("classpath:/templates")),
                i18nDatabase())
                .get();
}
```

Annotation-driven injection
---------------------------
With ResourceValueBeanPostProcessor bean configured, it is possible to inject resource providers,
resource value references and even the values of any type that can be instantiated from a string.
Below is an example:

```Java
	public class MyService {

		/*
		 * injected value is a provider for bundle(MyService.class)
		 */
		@InjectValue
		private ResourceProvider resources;

		/* Value is injected for key(MyService.class, "date") 
		 * and request-scoped resolution context 
		 */
		@InjectValue(value="date", 
		             resolvedBy = RequestResolutionContextProvider.class)
		private MandatoryString dateFormat; 

		/* 
		 * Value is injected for key(MyService.class, "applicationName")
		 */
		@InjectValue
		private String applicationName;

	} 
```

Class **ResourceValueReference** is particularly useful in this scenario:

```Java		
	/* 
	 * value is injected for key(MyService.class, "population") 
	 */
	@InjectValue
	private ResourceValueReference population;
	...	
	System.out.println(population.fetch(in(Locale.US)).notNull().asIs());

	// or simply...
	System.out.println(population.in(Locale.US).notNull().asIs());
```

You may even inject file content:

```Java
package com.mycompany.legal;

@Component
class EULA {
    /* 
     * Injected binary content of file /logo.jpg
     */
    @InjectResource("/logo.jpg")
    private byte[] logo;
	
	/* 
     * Content will be loaded on demand from /com/mycompany/legal/EULA.html
     */
    @InjectResource("*.html")
    private ResourceFileReference content;
}
...
    @Autowire
    private EULA eula;
    ...
    pdfGenerator.generateEULA(eula);
```


What's next?
----------
4. [Integration with Thymeleaf 2.1](ThymeleafIntegration.md)
5. [Extras](Extras.md)

Previous sections
-----------------
1. [Basics](Basics.md)
2. [Configuring resources](Configuration.md)
