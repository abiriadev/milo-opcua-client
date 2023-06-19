package io.aipim.miloopcuaclient;

/**
 * ConsoleExporter
 */
public class ConsoleExporter implements Exporter {

	@Override
	public void export(ExportData data) {
		System.out.println(data);
	}
}
