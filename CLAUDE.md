# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

Resource4J is a Java library for loading and accessing localization resources via a fluent DSL. It replaces Java's `ResourceBundle` with support for complex i18n scenarios, safe configuration access, and arbitrary resource files. Current version: 3.4.1-SNAPSHOT, requires Java 17+.

## Build Commands

```bash
# Build entire project (all modules)
mvn clean install

# Run unit tests only
mvn clean test

# Run unit + integration tests
mvn clean verify

# Run a single test class
mvn test -pl core -Dtest=ResourceKeyTest

# Run a single integration test
mvn verify -pl integration/spring -Dit.test=ResourceObjectAutowiringIT

# Run tests for a specific module
mvn test -pl converters
mvn test -pl core
mvn test -pl integration/spring
```

The `-pl <module>` flag targets a specific module. Integration tests use the `*IT.java` suffix and run via maven-failsafe-plugin.

## Module Structure

```
resource4j/
├── converters/          # Type conversion framework (String→Number, Date↔Calendar, etc.)
├── core/                # Core API: Resources, ResourceKey, ResourceObject, value resolution
├── integration/
│   ├── spring/          # Spring Framework: @InjectValue, @InjectBundle, MessageSource adapter
│   ├── thymeleaf3/      # Thymeleaf 3 template engine integration
│   ├── autoconfigure-thymeleaf3/  # Combined Spring+Thymeleaf auto-configuration
│   └── parsers/         # Additional parsers: Jackson JSON, Typesafe HOCON, XStream XML
└── demo/                # Spring Boot demo app (not part of main build)
```

## Core Architecture

### Key Abstractions (core module, `com.github.resource4j`)

- **`ResourceKey`** — Immutable identifier = bundle name + value id. Factory methods: `key(Class, String)`, `bundle(Class)`, `plain(String)`. Bundle names map to file paths via dot→slash conversion.
- **`Resources`** — Main API interface. `get(key, context)` returns `OptionalString`; `contentOf(name, context)` returns `ResourceObject`.
- **`RefreshableResources`** — Primary `Resources` implementation with caching, async resolution, and runtime refresh. Configured via `ResourcesConfigurationBuilder`.
- **`ResourceResolutionContext`** — Locale-based context for bundle file resolution (e.g., `in(Locale.US)` tries `_en_US`, `_en`, then default).
- **`ResourceObject`** — Handle to a binary resource (file, classpath entry). Parsed via `ResourceParser<T,V>` implementations.
- **`OptionalString` / `MandatoryString`** — Safe value wrappers. Chain with `.notNull()`, `.as(Type.class)` for type conversion.
- **`ResourceProvider`** — Scoped access to values within a single bundle via `resources.forKey(bundle)`.

### Resource Loading Pipeline

1. `ResourceObjectProvider` locates resource files (classpath, filesystem, Spring context)
2. `ResourceBundleParser` parses files into key-value maps (`.properties`, `.conf`, `.json`)
3. `ResourceValuePostProcessor` applies macro substitution via a state-machine parser
4. `TypeConverter` (converters module) handles value type conversion
5. Results are cached in `Cache<K,V>` implementations (basic or no-op)

### Configuration Pattern

```java
new RefreshableResources(configure()
    .sources(classpath(), fileSystem("/path"))
    .defaultBundle("i18n.resources")
    .formats(format(propertyMap()), format(jsonMap()))
    .postProcessing(new BasicValuePostProcessor())
    .get());
```

### Spring Integration

- `@InjectValue` / `@InjectBundle` / `@InjectResource` — Field injection annotations processed by `ResourceValueBeanPostProcessor`
- `Resource4jAutoConfiguration` — Spring Boot auto-configuration
- `Resource4jMessageSource` — Bridges Spring's `MessageSource` interface
- `ResolutionContextProvider` — Resolves context from request locale

## Test Framework

- JUnit 5 (Jupiter) + Hamcrest assertions
- `AbstractResource4JTest` — Base test class providing `RefreshableResources` with `HeapResourceObjectRepository` for in-memory test data
- Test contract interfaces: `MandatoryStringContracts`, `OptionalValueContracts`, etc. — reusable assertion sets
- JaCoCo for coverage (separate UT/IT reports in `target/jacoco-ut/` and `target/jacoco-it/`)

## Key Dependencies

| Dependency | Version | Scope |
|-----------|---------|-------|
| Java | 17+ | required |
| Spring Framework | 6.2.11 | provided (integration) |
| Spring Boot | 3.5.6 | optional (auto-config) |
| Thymeleaf | 3.1.3.RELEASE | provided (integration) |
| JUnit Jupiter | 6.0.0 | test |
