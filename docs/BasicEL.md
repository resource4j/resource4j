Pluralization
-------------
Resource4j supports locale-aware pluralization based on [CLDR plural rules](https://cldr.unicode.org/index/cldr-spec/plural-rules).
The `:pluralize` property in a macro expression selects the correct plural form for a given number.

Given a bundle `messages.properties`:
```properties
item_count={count} {item;count:pluralize}
item_one=item
item_other=items
```

The `{item;count:pluralize}` expression works as follows:
1. The value of parameter `count` is read (passed via the resolution context)
2. The `:pluralize` property applies CLDR rules for the current locale to determine the plural category (`one`, `other`, `few`, `many`, `zero`, `two`)
3. The result (e.g. `other`) is appended to the key `item` to form `item_other`
4. The value of `item_other` is resolved and substituted

```Java
Resources resources = new RefreshableResources(configure()
        .sources(classpath())
        .postProcessingBy(new BasicValuePostProcessor())
        .get());

// English: "1 item"
resources.get(key("messages", "item_count"), in(Locale.ENGLISH).with("count", 1))
        .notNull().asIs();

// English: "5 items"
resources.get(key("messages", "item_count"), in(Locale.ENGLISH).with("count", 5))
        .notNull().asIs();
```

Languages with more plural forms (e.g. Russian, Arabic, Polish) work the same way — just provide
the keys for each CLDR category that the language uses:

```properties
# Russian: one, few, many, other
file_count={count} {file;count:pluralize}
file_one=файл
file_few=файла
file_many=файлов
file_other=файлов
```