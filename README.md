resource4j - resource loader for Java
=====================================
Overview
--------
Resource4j library is a replacement for Java ResourceBundle mechanism that supports complex i18n scenarios of large and legacy applications and provides safe access to key/value application configuration and arbitrary resource files. 
With well-designed API based on fluent DSL it is a solution for i18n just like SLF4J is a solution for logging
abstraction.

Key features of this library:

 * Support of **Java SE resource bundles**, **custom resource file formats** (HOCON) and **data sources** (files, database etc).
 * Fluent **DSL** for type conversions and enforcing not-null constraints
 * Locale-based and custom resolution of values
 * **Spring Framework** and **Thymeleaf** integration, including Spring EL and JavaConfig.
 * Full support of **Test-Driven Development**
 * **Modular architecture** allows to use only the part of implementation you really need 


Quick Start
-----------
1. Add `resource4j-core.jar` to your classpath.
2. Create new instance of RefreshableResources:
```Java
Resources resources = new RefreshableResources();
```

3. Add resource bundles and content files to your classpath:

 * in  `/com/mycompany/data/Country-en_US.properties`

```  
discount=0.1

# Formatting
custom_format=MMMM d, yyyy
today=Today is {date:custom_format}

# Pluralization
duration={count} {years;count:pluralize}
years_one=year
years_other=years
```

* in  `/docs/EULA.txt`

```
Lorem ipsum dolorem sit amet.
```
	         	
4. Get some value:

```Java
BigDecimal localDiscount = resources.get(key(Country.class, "discount"), in(Locale.US))
			.notNull()
			.as(BigDecimal.class); // 0.1

String date = resources.get(key(Country.class, "today"), in(Locale.US).with("date", LocalDate.of(2018, 1, 1)))
            .notNull()
            .asIs(); // "January 1, 2018"

String duration = resources.get(key(Country.class, "duration"), in(Locale.US).with("count", 3))
            .notNull()
            .asIs(); // "3 years"
```

5. Load content:

```Java
String eulaText = resources.contentOf("/docs/EULA.txt", in(Locale.US))
			.parsedTo(string())
			.asIs();
```

Modules
---------------------

 * converters
 * resource4j-core
 * resource4j-spring - integration with Spring Framework 7
 * resource4j-thymeleaf3 - integration with Thymeleaf 3.1
 * resource4j-autoconfigure-thymeleaf3 - integration with Spring Boot 4/Thymeleaf 3.1
 * resource4j-parsers - bundle parsers supporting JSON (via Jackson), XML (via XStream) and HOCON

Learn more
----------
1. [Basics](docs/Basics.md)
2. [Configuring resources](docs/Configuration.md)
3. [Expression language](docs/BasicEL.md)
3. [Integration with Spring Framework](docs/SpringIntegration.md)
4. [Integration with Thymeleaf](docs/ThymeleafIntegration.md)
5. [Auto-configuration with Spring Boot 4](docs/AutoConfiguration.md)
5. [Parsers](docs/Parsers.md)