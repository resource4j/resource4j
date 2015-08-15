package com.github.resource4j.extras.config;

import java.io.IOException;
import java.io.InputStreamReader;

import com.github.resource4j.files.ResourceFile;
import com.github.resource4j.files.ResourceFileException;
import com.github.resource4j.files.parsers.AbstractValueParser;
import com.github.resource4j.files.parsers.ResourceFileFormatException;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigException;
import com.typesafe.config.ConfigFactory;
import com.typesafe.config.ConfigParseOptions;
import com.typesafe.config.ConfigSyntax;

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
	protected Config parse(ResourceFile file) throws IOException,
			ResourceFileException {
		try {
			InputStreamReader reader = new InputStreamReader(file.asStream());
			Config config = ConfigFactory.parseReader(reader, options);
			return config;
		} catch (ConfigException e) {
			throw new ResourceFileFormatException(file, e);
		}
	}

}
