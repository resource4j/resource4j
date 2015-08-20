package com.github.resource4j.files.parsers;

import java.util.Properties;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

/**
 * Namespace class for functions returning various parser instances.
 * See methods descriptions for details.
 * @author Ivan Gammel
 */
public final class ResourceParsers {

	private ResourceParsers() {
	}
	
    /**
     * Returns an instance of {@link StringParser} that uses default charset (UTF-8).
     * @return a string parser configured to use UTF-8 charset
     */
    public static StringParser string() {
        return StringParser.getInstance();
    }

    /**
     * Creates an instance of StringParser with given charset.
     * @param charsetName charset to use for parsing the files. Cannot be <code>null</code>.
     * @return a string parser configured to use given charset
     * @throws IllegalArgumentException if charset is <code>null</code>
     */
    public static StringParser string(String charsetName) {
        return new StringParser(charsetName);
    }
    
    /**
     * Returns an instance of parser that loads an image in any of the formats 
     * supported by {@link ImageIO} into {@link ImageIcon} object.
     * @return an instance of parser that loads image files
     */
    public static IconParser icon() {
        return IconParser.getInstance();
    }

    /**
     * Returns an instance of parser that parses Java resource bundles in <code>.properties</code> format 
     * into java.util.Map&lt;String,String&gt; instance.
     * @return a parser of <code>.properties</code> files
     */
    public static KeyValueParser propertyMap() {
        return KeyValueParser.getInstance();
    }
    
    /**
     * Returns an instance of parser that parses Java resource bundles in <code>.properties</code> format 
     * into {@link Properties} instance.
     * @return a parser of <code>.properties</code> files
     */
    public static PropertiesParser properties() {
        return PropertiesParser.getInstance();
    }

	public static ByteArrayParser binary() {
		return ByteArrayParser.getInstance();
	}

}
