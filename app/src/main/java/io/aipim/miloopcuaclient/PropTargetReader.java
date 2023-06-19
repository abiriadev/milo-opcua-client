package io.aipim.miloopcuaclient;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Properties;

/**
 * PropTargetReader
 */
public class PropTargetReader implements TargetReader {

	Properties prop;

	PropTargetReader() {
		prop = new Properties();
		InputStream inpst = null;
		try {
			inpst = new FileInputStream("ua.properties");
			prop.load(inpst);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (inpst != null) {
				try {
					inpst.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public Target getMap() {
		var hash = new HashMap<String, String>();
		for (final String k : prop.stringPropertyNames()) hash.put(
			k,
			prop.getProperty(k)
		);
		return new Target(hash);
	}
}
