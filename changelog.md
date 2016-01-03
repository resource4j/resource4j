Version 3.0
-----------
1. Java 8 is now minimal requirement for using this library.
2. API extended to include interoperability with Optional type.
3. Added support for java.time API and lambdas.

Breaking changes:
1. Introduced core ResourceObject interface. ResourceFile dependencies updated.
2. Changed ResourceBundleParser API - getResourceFileName replaced with ContentType annotation for bundle parsers.
3. Implementation packages reorganized

Version 2.1
-----------
This version has minor incompatible changes with version 2.0:
1. #17, #23 - [spring] added @InjectValue, @InjectResource and @InjectBundle annotations, which replaced @AutowiredResource
2. #19 - [core] resource key id component separator changed to underscore (incompatible change)
3. #20 - [core] added ByteArrayParser (ResourceParsers.binary())
4. #21 - [core] TypeConverter fixed to support String/Date conversions with default ISO8601 pattern
5. #13 - [spring] added support for session-scoped and request-scoped beans
6. #15 - [extras] - new library with support for HOCON and XStream parsers