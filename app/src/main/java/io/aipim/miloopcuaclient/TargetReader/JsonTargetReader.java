package io.aipim.miloopcuaclient.TargetReader;

import io.aipim.miloopcuaclient.App;
import io.aipim.miloopcuaclient.Target;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.json.JSONObject;

@Slf4j
public class JsonTargetReader implements TargetReader {

	JSONObject json;

	public JsonTargetReader() {
		try {
			var st =
				App.class.getClassLoader()
					.getResourceAsStream("ua.json");

			if (st == null) throw new IOException(
				"ua1.json file resource stream does not exist"
			);

			json =
				new JSONObject(
					IOUtils.toString(
						st,
						StandardCharsets.UTF_8
					)
				);
		} catch (IOException e) {
			log.error("UA JSON file not found", e);
		}
	}

	public Target getMap() {
		final var hash = new HashMap<String, String>();
		json
			.keys()
			.forEachRemaining(key ->
				hash.put(key, json.getString(key))
			);
		return new Target(hash);
	}
}
