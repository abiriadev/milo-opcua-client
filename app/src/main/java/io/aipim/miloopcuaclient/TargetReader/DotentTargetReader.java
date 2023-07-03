package io.aipim.miloopcuaclient.TargetReader;

import io.aipim.miloopcuaclient.Target;
import io.github.cdimascio.dotenv.Dotenv;
import io.github.cdimascio.dotenv.DotenvEntry;
import java.util.HashMap;

public class DotentTargetReader implements TargetReader {

	private Dotenv dotenv = Dotenv.load();

	@Override
	public Target getMap() {
		var hm = new Target(new HashMap<String, String>());

		for (DotenvEntry e : dotenv.entries()) {
			if (e.getKey().startsWith("AAS_")) hm.put(
				e.getKey().replaceFirst("^AAS_", ""),
				e.getValue()
			);
		}

		return hm;
	}
}
