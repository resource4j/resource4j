Resource4j Extras
=================================

The resource4j-extras library is a collection of small integrations with other APIs, which currently includes:
1. HOCON format support
2. XStream XML parser support

HOCON
-------------
HOCON is a convenient configuration format,
[described here](https://github.com/typesafehub/config/blob/master/HOCON.md).
You can use ConfigParser to parse values to Config objects:
```Java
import static com.github.resource4j.extras.config.ConfigParser.config;

Config myConfig = resources.get("my.config", in("debug")).parsedTo(config()).asIs();
```

Besides that, ConfigMapParser treats HOCON config files as bundles, so that each configuration
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
```Java
RefreshableResources resources = new RefreshableResources(
                configure()
                        .formats(format(configMap(), ".conf"))
                        .get());
String host = resources.get(key("app","mail.server.host"), withoutContext()).asIs();
assertEquals("localhost", host); // true
```

XStream
-------------
Integration with XStream provides a parser for resource objects in XML format, as shown in this example:
```Java
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
...
import static com.github.resource4j.extras.xstream.XStreamParser.xml;
...
Model object = resources.get("my.xml", withoutContext()).parsedTo(xml(Model.class)).asIs();
```

What's next?
----------
5. [Try our Demo application](../demo/README.md)

Previous sections
-----------------
1. [Basics](Basics.md)
2. [Configuring resources](Configuration.md)
3. [Integration with Spring Framework](SpringIntegration.md)
4. [Integration with Thymeleaf 2.1](ThymeleafIntegration.md)

