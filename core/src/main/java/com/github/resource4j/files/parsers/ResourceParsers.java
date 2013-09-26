package com.github.resource4j.files.parsers;

public class ResourceParsers {

    public static StringParser string() {
        return StringParser.getInstance();
    }

    public static IconParser icon() {
        return IconParser.getInstance();
    }

    public static PropertiesParser properties() {
        return PropertiesParser.getInstance();
    }

}
