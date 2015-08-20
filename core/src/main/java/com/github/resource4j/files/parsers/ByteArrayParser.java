package com.github.resource4j.files.parsers;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import com.github.resource4j.OptionalValue;
import com.github.resource4j.ResourceKey;
import com.github.resource4j.files.ResourceFile;
import com.github.resource4j.files.ResourceFileException;
import com.github.resource4j.generic.GenericOptionalValue;

public class ByteArrayParser extends AbstractParser<byte[], OptionalValue<byte[]>> {

	private static final ByteArrayParser INSTANCE = new ByteArrayParser();

	@Override
	protected OptionalValue<byte[]> createValue(ResourceFile file,
			ResourceKey key, byte[] value, Throwable suppressedException) {
		return new GenericOptionalValue<byte[]>(file.resolvedName(), key, value, suppressedException);
	}

	@Override
	protected byte[] parse(ResourceFile file) throws IOException, ResourceFileException {
		InputStream is = file.asStream();
		try (ByteArrayOutputStream os = new ByteArrayOutputStream()) {
			byte[] buffer = new byte[0xFFFF];
			for (int len; (len = is.read(buffer)) != -1;) {
				os.write(buffer, 0, len);
			}
			os.flush();
			return os.toByteArray();
		} 
	}

	public static ByteArrayParser getInstance() {
		return INSTANCE;
	}
}
