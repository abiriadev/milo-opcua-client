package io.aipim.miloopcuaclient;

import io.aipim.miloopcuaclient.Exporter.JsonExporter;
import io.aipim.miloopcuaclient.TargetReader.PropTargetReader;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Slf4j
public class App {

	public static void main(String[] args) {
		new Initializer(
			new PropTargetReader(),
			// new ConsoleExporter()
			new JsonExporter()
		)
			.run();
		log.debug("Initialization ended");
	}
}
