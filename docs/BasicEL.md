Expression Language
===================

`BasicValuePostProcessor` performs macro substitution in resource values. When enabled,
resource values can contain expressions that reference other values, apply formatting,
and transform text.

To enable macro substitution, configure post-processing:

```java
import static com.github.resource4j.resources.processors.BasicValuePostProcessor.macroSubstitution;

Resources resources = new RefreshableResources(configure()
        .sources(classpath())
        .postProcessingBy(macroSubstitution())
        .get());
```

Macro Syntax
------------

A macro is enclosed in curly braces: `{name}`. It is replaced by the value of the key `name`
resolved from the same bundle.

```properties
greeting=Hello, {name}!
name=World
```

```java
resources.get(key("bundle", "greeting"), withoutContext()).notNull().asIs();
// "Hello, World!"
```

### Parameters

Parameters can be passed from Java code via the resolution context and referenced in macros:

```java
resources.get(key("bundle", "greeting"), in(Locale.ENGLISH).with("name", "Alice"))
        .notNull().asIs();
// "Hello, Alice!"
```

Context parameters take priority over bundle values with the same key.

### Literals

A macro starting with `:` produces a literal — the text inside the braces, wrapped in `{}`:

```properties
pattern=Use macros like {:name} in your resources
```

Resolves to: `Use macros like {name} in your resources`

### Escape Sequences

Use backslash to escape special characters:

| Sequence | Result | Notes |
|----------|--------|-------|
| `\{`     | `{`    | Opening brace, not a macro start |
| `\}`     | `}`    | Closing brace in text |
| `\\`     | `\`    | Literal backslash |

Inside a macro, backslash escapes the next character (useful for `;` and `:`):

```properties
escaped=You can use macros like \{name\} and '\\' for backslash.
```

Resolves to: `You can use macros like {name} and '\' for backslash.`

Properties
----------

A macro can apply a **property** to its resolved value using the `:` separator:

```
{key:property}
```

The value of `key` is resolved first, then the property is applied. The following
built-in property resolvers are available.

### Date formatting

When the value is a `TemporalAccessor` (e.g. `LocalDate`, `LocalDateTime`), the property
is treated as a reference to a format pattern in the bundle:

```properties
custom_format=MMMM d, yyyy
today=Today is {date:custom_format}
```

```java
resources.get(key("bundle", "today"),
        in(Locale.US).with("date", LocalDate.of(2018, 1, 1)))
        .notNull().asIs();
// "Today is January 1, 2018"
```

The value of `custom_format` is looked up in the bundle and used as a `DateTimeFormatter` pattern.

### String case transforms

When the value is a `String`, the following properties transform its case using the
context locale:

| Property     | Effect                         | Example           |
|-------------|--------------------------------|-------------------|
| `upper`      | Convert to upper case          | `hello` -> `HELLO` |
| `lower`      | Convert to lower case          | `HELLO` -> `hello` |
| `upperFirst` | Capitalize first character     | `hello` -> `Hello` |
| `lowerFirst` | Lowercase first character      | `Hello` -> `hEllo` |

```properties
greeting={name:upper}, welcome!
```

```java
resources.get(key("bundle", "greeting"),
        in(Locale.ENGLISH).with("name", "alice"))
        .notNull().asIs();
// "ALICE, welcome!"
```

### Pluralization

The `:pluralize` property selects the correct plural form for a given number, based on
[CLDR plural rules](https://cldr.unicode.org/index/cldr-spec/plural-rules).

```properties
item_count={count} {item;count:pluralize}
item_one=item
item_other=items
```

The `{item;count:pluralize}` expression works as follows:
1. The value of parameter `count` is read (passed via the resolution context)
2. The `:pluralize` property applies CLDR rules for the current locale to determine the
   plural category (`one`, `other`, `few`, `many`, `zero`, `two`)
3. The result (e.g. `other`) is appended to the key `item` to form `item_other`
4. The value of `item_other` is resolved and substituted

```java
// English: "1 item"
resources.get(key("messages", "item_count"), in(Locale.ENGLISH).with("count", 1))
        .notNull().asIs();

// English: "5 items"
resources.get(key("messages", "item_count"), in(Locale.ENGLISH).with("count", 5))
        .notNull().asIs();
```

Languages with more plural forms (e.g. Russian, Arabic, Polish) work the same way — just
provide the keys for each CLDR category that the language uses:

```properties
# Russian: one, few, many, other
file_count={count} {file;count:pluralize}
file_one=файл
file_few=файла
file_many=файлов
file_other=файлов
```

### JavaBean property access

When none of the above strategies match, the property is resolved via JavaBean getter
conventions on the value object. For a property `name`, the resolver tries `getName()`,
`isName()`, and `name()` in order.

Macro Parameters
----------------

Parameters allow passing additional context into a macro using `;` as separator:

```
{key;param1;param2}
```

Each parameter is a key whose value is looked up from the resolution context. The resolved
parameter values are passed to the key's resolver as a map with string indices (`"0"`, `"1"`, etc.).

This is primarily used with pluralization, where the parameter provides the number
to pluralize by:

```properties
message={count} {item;count:pluralize}
```

Here `count` is both displayed as text (`{count}`) and passed as a parameter to the
`{item;...}` macro for pluralization.

Use `\;` inside a macro to include a literal semicolon in a parameter value.

Error Handling
--------------

When macro substitution fails (unresolved key, syntax error, unmatched braces),
the value is considered missing. Accessing it via `notNull()` throws
`MissingValueException` whose cause is a `ValuePostProcessingException` containing
the partial result:

```java
try {
    resources.get(key("my", "broken"), withoutContext()).notNull().asIs();
} catch (MissingValueException e) {
    ValuePostProcessingException cause = (ValuePostProcessingException) e.getCause();
    String partial = cause.getPartialResult();
}
```

---

Previous: [Configuring resources](Configuration.md)
Next: [Integration with Spring Framework](SpringIntegration.md)
