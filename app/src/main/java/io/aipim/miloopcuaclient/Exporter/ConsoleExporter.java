package io.aipim.miloopcuaclient.Exporter;

import io.aipim.miloopcuaclient.ExportData;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ConsoleExporter implements Exporter {

	@Override
	public void export(ExportData data) {
		for (final var k : data.hsm.keySet()) System.out.format(
			"%s: %s,\t",
			k,
			data.hsm.get(k)
		);
		System.out.println();
	}
}
