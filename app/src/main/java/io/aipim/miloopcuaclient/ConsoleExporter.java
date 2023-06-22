package io.aipim.miloopcuaclient;

public class ConsoleExporter implements Exporter {

	@Override
	public void export(ExportData data) {
		for (var k : data.hsm.keySet()) System.out.format(
			"%s: %s,\t",
			k,
			data.hsm.get(k)
		);
		System.out.println("");
	}
}
