package io.aipim.miloopcuaclient.Exporter;

import io.aipim.miloopcuaclient.ExportData;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ConsoleExporter implements Exporter {

	@Override
	public void export(ExportData data) {
		for (final var k : data.targets.keySet()) System.out.format(
			"%s: %s,\t",
			k,
			data.targets.get(k).value
		);
		System.out.println();
	}
}
