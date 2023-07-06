package io.aipim.miloopcuaclient.TargetReader;

import io.aipim.miloopcuaclient.App;
import io.aipim.miloopcuaclient.Target;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.json.JSONObject;

public class JsonTargetReader implements TargetReader {

	JSONObject json;

	public JsonTargetReader() {
		try {
			json =
				new JSONObject(
					IOUtils.toString(
						App.class.getClassLoader()
							.getResourceAsStream("ua.json"),
						StandardCharsets.UTF_8
					)
				);
		} catch (IOException e) {
			e.printStackTrace();
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
