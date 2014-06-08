package com.github.resource4j.files.parsers;

public class ResourceParsers {

    /**
     * @return a string parser configured to use UTF-8 charset
     */
    public static StringParser string() {
        return StringParser.getInstance();
    }

    /**
     * @return a string parser configured to use given charset
     */
    public static StringParser string(String charsetName) {
        return new StringParser(charsetName);
    }
    
    public static IconParser icon() {
        return IconParser.getInstance();
    }

    public static PropertiesParser properties() {
        return PropertiesParser.getInstance();
    }

}
