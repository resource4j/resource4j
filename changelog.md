Version 2.1
-----------
This version has minor incompatible changes with version 2.0:
1. #17 - [spring] @AutowiredResource annotation now supports injection of file content
2. #19 - [core] resource key id component separator changed to underscore (incompatible change)
3. #20 - [core] added ByteArrayParser (ResourceParsers.binary())
4. #21 - [core] TypeConverter fixed to support String/Date conversions with default ISO8601 pattern