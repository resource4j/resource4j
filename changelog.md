Version 3.0.1
-------------
1. \#32 - added shortcuts for typical use cases in OptionalValue and Resources

Version 3.0
-----------
1. Java 8 is now minimal requirement for using this library.
2. API extended to include interoperability with Optional type.
3. Added support for java.time API and lambdas.
4. Resources can be updated in runtime, if object or value provider supports it.
5. Added resource value post-processing on loading.
6. Spring Boot Auto-Configuration support

Breaking changes:

1. With introduction of ResourceObject interface and related APIs, all ResourceFile APIs have been removed.
2. Implementation packages reorganized.
3. Dropped support of default bundles, since there exists a preferable and more transparent alternative in DSL
4. TypeConverter moved to converters.jar and rewritten to support java.time.API

Version 2.1
-----------
This version has minor incompatible changes with version 2.0:

1. \#17, #23 - [spring] added @InjectValue, @InjectResource and @InjectBundle annotations, which replaced @AutowiredResource
2. \#19 - [core] resource key id component separator changed to underscore (incompatible change)
3. \#20 - [core] added ByteArrayParser (ResourceParsers.binary())
4. \#21 - [core] TypeConverter fixed to support String/Date conversions with default ISO8601 pattern
5. \#13 - [spring] added support for session-scoped and request-scoped beans
6. \#15 - [extras] - new library with support for HOCON and XStream parsers