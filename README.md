resource4j - resource loader for Java
=====================================
Overview
--------
Resource4j library provides an API for loading key/value pairs and arbitrary content from application resource files stored in classpath, file system or (with custom code) from any other source. This library extends the functionality of resource bundles in Java SE with fluent DSL for enforcing not-null constraints and type conversions and allows to customize file name resolution for specific locale (or even define own resolution context).

Get Started
-----------
1. Add <code>resource4j-core.jar</code> to your classpath.
2. Create new instance of DefaultResources:
	<pre><code>	Resources resources = new DefaultResources();</code></pre>
3. Get some value:
	<pre><code>	int population = resources.get(key(Country.class, "population"), in(Locale.US))
			.notNull()
			.as(Integer.class);</code></pre>
4. Load content:
	<pre><code>	String text = resources.contentOf("documentation.txt", in(Locale.US))
			.parsedTo(string())
			.asIs();</code></pre>

Resource keys
---------------
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
Initially resource value is returned as OptionalString wrapper: using methods from fluent DSL you can convert it to MandatoryString (<code>notNull()</code> and <code>orDefault()</code> methods), to value of another type (method <code>as()</code>) or get the wrapped string. Have a look at Javadoc for details.

Resource files and parsers
--------------------------

Resource providers and resolution contexts
------------------------------------------

Configuration
-------------

Testing
-------
Compared to Cal10n, Resource4J does not provide compile-time verification of resources - in large projects it may not always be possible to have all translations by the beginning of development, so developers need more flexible approach. Thus, this library prefers post-compile checks, allowing to write simple unit tests for verification of resource keys. Next versions of this library will provide support for post-deployment verification to support plugin architectures. 

Spring and Thymeleaf integration
================================
Overview
--------

Dependency injection
--------------------

Template and message resolution
-------------------------------

Web application example
======================= 