package io.aipim.miloopcuaclient;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Slf4j
public class App {

	// NOTE: logger property should be static and package-private.
	static final Logger logger = LoggerFactory.getLogger(
		App.class
	);

	public static void main(String[] args) {
		new Initializer(
			new PropTargetReader(),
			new ConsoleExporter()
		)
			.run();
		logger.error("Initialization ended");
	}
}
