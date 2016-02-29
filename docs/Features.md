Resource4j Features
=====================================
Core
--------
 * Fluent **DSL** for obtaining resource values, type conversions and enforcement of not-null constraints.
 * Support for loading **binary** resource objects - images, document templates etc.
 * Search of values in **multiple data sources** based on context **resolution rules**, e.g. from locale-specific resource files.
 * Resource values can be updated in **runtime** (e.g. deployment of new language bundles).
 * Fully testable, designed for **TDD**.
 * Can be used in **standalone** or **JEE** environment.
 
Integration with Spring
--------------------------
 * **Annotation-based** autowiring of resource values and binary objects in Spring beans
 * **@EnableResource4J** annotation for configuration in **Spring Boot** applications
 * Message and view resolution for **WebMVC**

More integrations
--------------------
 * Seamless integration for use in **[Thymeleaf](http://www.thymeleaf.org)** templates
 * **[HOCON](https://github.com/typesafehub/config/blob/master/HOCON.md)** configuration files can be used as data sources
 
 Read our [QuickStart guide](./QuickStart.md) to start using Resource4j!