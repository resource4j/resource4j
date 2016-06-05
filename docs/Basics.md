Basics
======

Resource keys
-------------
Each resource value is represented by a ResourceKey. Resource key represents both name of the resource bundle and name of the value in that bundle as a single immutable instance (compared to Java SE, where you have to work with them as separate strings). Compared to Cal10n, you don't need to create an enumeration for every set of resource keys (in large projects there can be thousands of them) and you may even generate resource keys dynamically using <code>child()</code> method.

Resource key can be created for arbitrary bundle, any class and enumeration constant. Below are some examples:

	// bundle=example.bundle id=someKey
	ResourceKey key1 = ResourceKey.key("example.bundle", "someKey");

	// bundle=example.MyService id=someKey
	ResourceKey key2 = ResourceKey.key(MyService.class, "someKey");

	// bundle=example.MyEnum id=SOME_VALUE
	ResourceKey key3 = ResourceKey.key(MyEnum.SOME_VALUE);

	// bundle=example.MyService id=null
	ResourceKey bundle = ResourceKey.bundle(MyService.class);

	// bundle=example.MyService id=someKey
	ResourceKey child = bundle.child("someKey");

	// bundle=example.MyService id=someKey.invalid
	ResourceKey invalid = child.child("invalid");

Resource values
---------------
Initially resource value is returned as OptionalString wrapper: using methods from fluent DSL you can convert it to MandatoryString (<code>notNull()</code> and <code>orDefault()</code> methods), to value of another type (method <code>as()</code>) or get the wrapped string. Here are some examples:

	OptionalString value = resources.get(key, in(Locale.US));
	String string = string.asIs(); // can be null here
	MandatoryString mandatory = value.notNull(); // may throw MissingValueException here
	MandatoryString m2 = value.or("example");
	OptionalValue<Integer> integer = value.ofType(Integer.class);
	OptionalValue<Integer> intUS = value.ofType(Integer.class, DecimalFormat.getNumberInstance(Locale.US));
	int intValue = integer.asIs(); // autoboxing
	int intValue = integer.orDefault(5); // autoboxing

	long fluentValue = resources.get(key, in(currentLocale)).ofType(Long.class).orDefault();

Resource objects and providers
------------------------------
Sometimes you need to load value as binary content of a large file. You can use ResourceObject and ResourceParser API for it:

	ResourceObject document = resources.contentOf("document.xml", in(Locale.US));
	OptionalString string = document.parsedTo(ResourceParsers.string("utf-8"));

Or shorter:

    String s = resources
    	.contentOf("document.xml", in(Locale.US))
    	.parsedTo(string("utf-8"))
    	.notNull().asIs();

You can even implement your own parser (below is the example that uses XStream for XML parsing):

	@XStreamAlias("config")
	public class MyConfig {
	}

	public class ConfigParser extends AbstractValueParser<MyConfig> {
		private final static ConfigParser INSTANCE = new ConfigParser();
		private XStream xstream = new XStream();
		{
			xstream.processAnnotations(MyConfig.class);
		}
		public static ConfigParser configuration() {
			return INSTANCE;
		}
		protected MyConfig parse(ResourceFile file) throws IOException {
			return (MyConfig) xstream.fromXML(file.asStream());
		}
	}
	...
	import static MyConfig.configuration;
	...
	MyConfig config = resources
		.contentOf("config.xml")
		.parsedTo(configuration())
		.notNull().asIs();

Oh, by the way, we already have XStream parser in [Extras](Extras.md) library!

Resolution contexts
-------------------
Resolution context is the way to specify which version of resource bundle to use for loading value in runtime. Resolution context in its simplest form consists of Locale object, defining the language and the country for which the value must be loaded. Resource4J library allows to define more complex resolution contexts, containing multiple components.
The **ResourceResolutionContext** class contains two useful factory methods for defining resolution context:

	ResourceResolutionContext in(Object... components);
	ResourceResolutionContext withoutContext();

The first one creates context defined by given components, the second one creates empty context.
Example of composite resolution context usage that allows different presentation of some resource values on the web:

	resources.get(key("bundle", id"), in(Locale.US, "WEB"));

When passed to Resources instance that uses basic naming strategy, this context defines following search order for resource bundles:


	bundle-en_US-WEB.properties
	bundle-en-WEB.properties
	bundle-WEB.properties
	bundle-en_US.properties
	bundle-en.properties
	bundle.properties

You may even not use locale-based resolution:

	resources.get(key("database", "password"), in("localhost", "mywebapp", "debug"))

This query may look up the value in resource bundles in following sequence:

	database-localhost-mywebapp-debug.conf
	database-mywebapp-debug.conf
	database-localhost-debug.conf
	database-debug.conf
	database-localhost-mywebapp.conf
	database-mywebapp.conf
	database-localhost.conf
	database.conf

Resource providers
------------------
Resource providers are the convenient way to work with a single bundle, addressing the values
by plain String keys. If you have a class that references a lot of localization resources
or configuration values,
using a ResourceProvider instead of Resources can be a good option:

	class MyClass {
		...
		ResourceProvider provider = resources.forKey(bundle(MyClass.class));
		OptionalString string = provider.get("someKey", in(Locale.US));
		...
	}


What's next?
----------
1. [Configuring resources](docs/Configuration.md)
2. [Integration with Spring Framework](docs/SpringIntegration.md)
3. [Integration with Thymeleaf 2.1](docs/ThymeleafIntegration.md)
4. [Extras](docs/Extras.md)