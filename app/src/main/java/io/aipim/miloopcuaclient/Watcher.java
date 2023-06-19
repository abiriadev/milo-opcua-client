package io.aipim.miloopcuaclient;

/**
 * Watcher
 */
public class Watcher {

	Exporter exporter;

	Watcher(Target target, Exporter exporter) {
		this.exporter = exporter;
	}

	void send(ExportData data) {
		exporter.export(data);
	}
}
