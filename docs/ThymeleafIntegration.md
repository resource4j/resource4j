Integration with Thymeleaf 2.1
=================================

Overview
--------------------
Resource4j support for Thymeleaf 2.1 includes not only support for localized messages,
but also resolution of templates.

Configuration
-------------
Resource4j supports Spring Boot auto-configuration for Thymeleaf.
By adding **resource4j-thymeleaf21** library as a dependency you will automatically
get pre-configured SpringTemplateEngine bean with custom message and template resolvers
based on Resource4j. You can add more Thymeleaf dialects (e.g. Spring Security dialect from
Thymeleaf Extras) by simply adding IDialect implementations as Spring beans.
You can try our demo application to see, how it all works together.

What's next?
----------
5. [Extras](Extras.md)

Previous sections
-----------------
1. [Basics](Basics.md)
2. [Configuring resources](Configuration.md)
3. [Integration with Spring Framework](SpringIntegration.md)
