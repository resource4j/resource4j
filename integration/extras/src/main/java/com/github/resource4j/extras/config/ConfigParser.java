package com.github.resource4j.extras.config;

import com.github.resource4j.ResourceObject;
import com.github.resource4j.ResourceObjectException;
import com.github.resource4j.objects.parsers.AbstractValueParser;
import com.github.resource4j.objects.parsers.ResourceObjectFormatException;
import com.typesafe.config.*;

import java.io.IOException;
import java.io.InputStreamReader;

@SuppressWarnings("WeakerAccess")
public class ConfigParser extends AbstractValueParser<Config> {

	private ConfigParseOptions options;

	public static ConfigParser config() {
		return new ConfigParser();
	}

	public static ConfigParser config(ConfigParseOptions options) {
		return new ConfigParser(options);
	}
	
	public ConfigParser() {
		 options = ConfigParseOptions.defaults()
				 .setSyntax(ConfigSyntax.CONF)
				 .setAllowMissing(false);		
	}
	
	public ConfigParser(ConfigParseOptions options) {
		super();
		this.options = options;
	}

	@Override
	protected Config parse(ResourceObject file) throws IOException,
			ResourceObjectException {
		try {
			InputStreamReader reader = new InputStreamReader(file.asStream());
            return ConfigFactory.parseReader(reader, options);
		} catch (ConfigException e) {
			throw new ResourceObjectFormatException(file, e);
		}
	}

}
