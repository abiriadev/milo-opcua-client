package io.aipim.miloopcuaclient;

import org.json.JSONObject;

public class JsonExporter implements Exporter {

	@Override
	public void export(ExportData data) {
		System.out.println(new JSONObject(data.hsm));
	}
}
