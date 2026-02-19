Integration with Thymeleaf
=================================

Overview
--------------------
Resource4j support for Thymeleaf includes not only support for localized messages,
but also locale-aware resolution of templates themselves. This means that you can have
completely different template files for different locales, not just different message strings.

Dependencies
--------------------
The Thymeleaf integration is split into two modules:

- **resource4j-thymeleaf3** — core integration providing `Resource4jTemplateResolver`
  and `Resource4jMessageResolver`. Use this if you configure Thymeleaf manually.
- **resource4j-autoconfigure-thymeleaf3** — Spring Boot auto-configuration that sets up
  all the beans automatically. Use this with Spring Boot applications.

Message resolution
--------------------
`Resource4jMessageResolver` implements Thymeleaf's `IMessageResolver` interface, resolving
`#{...}` expressions in templates through Resource4j:

```html
<p th:text="#{greeting}">Default greeting</p>
```

The message key is resolved as a plain key (not bound to any class bundle), using the
current request locale. Message parameters are supported:

```html
<p th:text="#{welcome(${user.name})}">Welcome</p>
```

Parameters are passed to the Resource4j resolution context, so they can be used
in macro expressions within the property file:

```properties
greeting=Hello!
welcome=Welcome, {0}!
```

Template resolution
--------------------
`Resource4jTemplateResolver` resolves templates through the Resource4j resource loading
pipeline. This enables locale-specific template files — for example, you can provide
completely different page layouts for different languages:

```
templates/article.html          (default)
templates/article-en_US.html    (US English)
templates/article-de.html       (German)
```

The resolver uses the standard Thymeleaf prefix/suffix configuration to construct
the resource name, then delegates to `Resources.contentOf()` with the current locale.

Manual configuration
--------------------
If you are not using Spring Boot, configure the template engine manually:

```java
Resource4jTemplateResolver templateResolver = new Resource4jTemplateResolver(resources);
templateResolver.setPrefix("templates/");
templateResolver.setSuffix(".html");
templateResolver.setTemplateMode(TemplateMode.HTML);
templateResolver.setCharacterEncoding("UTF-8");

Resource4jMessageResolver messageResolver = new Resource4jMessageResolver(resources);

TemplateEngine engine = new TemplateEngine();
engine.setTemplateResolver(templateResolver);
engine.setMessageResolver(messageResolver);
```

Spring Boot auto-configuration
--------------------
With the **resource4j-autoconfigure-thymeleaf3** dependency, all of the above is set up
automatically. The auto-configuration:

- Creates a `Resource4jTemplateResolver` using the standard `spring.thymeleaf.*` properties
- Registers a `Resource4jMessageSource` as the Spring `MessageSource`
- Wraps the `SpringTemplateEngine` to pass locale and other context attributes
  to the template resolver

You can customize the template resolution by providing `ContextSpecificAttributeResolver`
beans, which add custom attributes to the template resolution context beyond just locale.

See [Auto-configuration](AutoConfiguration.md) for more details.

---

Previous: [Integration with Spring Framework](SpringIntegration.md)
Next: [Auto-configuration with Spring Boot](AutoConfiguration.md)
