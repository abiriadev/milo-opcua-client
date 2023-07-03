package io.aipim.miloopcuaclient.TargetReader;

import io.aipim.miloopcuaclient.App;
import io.aipim.miloopcuaclient.Target;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import org.apache.commons.io.FileUtils;
import org.json.JSONObject;

public class JsonTargetReader implements TargetReader {

	JSONObject json;

	public JsonTargetReader() {
		try {
			json =
				new JSONObject(
					FileUtils.readFileToString(
						new File(
							App.class.getClassLoader()
								.getResource("ua.json")
								.getFile()
						),
						"UTF-8"
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
