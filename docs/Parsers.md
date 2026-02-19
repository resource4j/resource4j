Parsers
=================================

The resource4j-parsers library is a collection of small integrations with other APIs, which currently includes:
1. HOCON format support
2. Jackson JSON parser support
3. XStream XML parser support

HOCON
-------------
HOCON is a convenient configuration format,
[described here](https://github.com/typesafehub/config/blob/master/HOCON.md).
You can use `ConfigParser` to parse values to Config objects:

```java
import static com.github.resource4j.parsers.config.ConfigParser.config;

Config myConfig = resources.get("my.config", in("debug")).parsedTo(config()).asIs();
```

Besides that, `ConfigMapParser` treats HOCON config files as bundles, so that each configuration
value can be extracted via resource key.

**Example:**

File `app.config`:
```
mail {
    user : guest
    password : guest
    server {
        host : localhost
        port : 25
    }
}
```
Following test will pass:
```java
RefreshableResources resources = new RefreshableResources(
                configure()
                        .formats(format(configMap(), ".conf"))
                        .get());
String host = resources.get(key("app","mail.server.host"), withoutContext()).asIs();
assertEquals("localhost", host); // true
```

Jackson JSON
-------------
Integration with Jackson provides a bundle parser for JSON resource files. You can use
`JacksonBundleParser` to treat JSON files as key-value bundles:

```java
import static com.github.resource4j.parsers.json.JacksonBundleParser.jsonMap;

Resources resources = new RefreshableResources(
                configure()
                        .formats(format(jsonMap(), ".json"))
                        .get());
```

Or use `JacksonParser` to parse JSON into typed objects:

```java
import static com.github.resource4j.parsers.json.JacksonParser.json;

MyModel model = resources.contentOf("data.json", withoutContext())
        .parsedTo(json(MyModel.class))
        .notNull().asIs();
```

XStream
-------------
Integration with XStream provides a parser for resource objects in XML format, as shown in this example:

```java
@XStreamAlias("model")
public class Model {
    @XStreamAsAttribute
    private String message;
    @XStreamAlias("value")
    private int value;
    public String getMessage() {
        return message;
    }
    public void setMessage(String message) {
        this.message = message;
    }
    public int getValue() {
        return value;
    }
    public void setValue(int value) {
        this.value = value;
    }
}
```

```java
import static com.github.resource4j.parsers.xstream.XStreamParser.xml;

Model object = resources.get("my.xml", withoutContext()).parsedTo(xml(Model.class)).asIs();
```

---

Previous: [Auto-configuration with Spring Boot](AutoConfiguration.md)
