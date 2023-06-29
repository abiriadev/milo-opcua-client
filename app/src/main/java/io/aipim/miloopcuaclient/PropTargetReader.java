package io.aipim.miloopcuaclient;

import java.io.IOException;
import java.util.HashMap;
import java.util.Properties;

public class PropTargetReader implements TargetReader {

	Properties prop = new Properties();

	PropTargetReader() {
		try (
			final var inpst = App.class.getClassLoader()
				.getResourceAsStream("ua.properties")
		) {
			prop.load(inpst);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public Target getMap() {
		final var hash = new HashMap<String, String>();
		for (final String k : prop.stringPropertyNames()) hash.put(
			k,
			prop.getProperty(k)
		);
		return new Target(hash);
	}
}
