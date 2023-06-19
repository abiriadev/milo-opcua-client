package io.aipim.miloopcuaclient;

/**
 * Initializer
 */
public class Initializer {

	Initializer(TargetReader tr, Exporter exporter) {
		new Watcher(tr.getMap(), exporter);
	}
}
