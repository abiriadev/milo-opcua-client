package io.aipim.miloopcuaclient.TargetReader;

import io.aipim.miloopcuaclient.App;
import io.aipim.miloopcuaclient.Target;
import java.io.IOException;
import java.util.HashMap;
import java.util.Properties;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class PropTargetReader implements TargetReader {

	Properties prop = new Properties();

	public PropTargetReader() {
		try (
			final var inpst = App.class.getClassLoader()
				.getResourceAsStream("ua.properties")
		) {
			prop.load(inpst);
		} catch (IOException e) {
			log.error("UA properties file not found", e);
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
