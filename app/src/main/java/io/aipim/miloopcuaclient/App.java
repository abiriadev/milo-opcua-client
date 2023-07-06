package io.aipim.miloopcuaclient;

import io.aipim.miloopcuaclient.Exporter.ConsoleExporter;
import io.aipim.miloopcuaclient.TargetReader.JsonTargetReader;
import lombok.extern.slf4j.Slf4j;
import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

@Slf4j
@Command(
	name = "milo-opcua-client",
	version = "v0.1.0",
	mixinStandardHelpOptions = true
)
public class App implements Runnable {

	@Option(
		names = { "-u", "--url" },
		paramLabel = "<URL>",
		description = "URL to OPC UA Server"
	)
	String url;

	public static void main(String[] args) {
		System.exit(
			new CommandLine(new App()).execute(args)
		);
	}

	@Override
	public void run() {
		new Initializer(
			new JsonTargetReader(),
			new ConsoleExporter()
		)
			.run();
		log.info("Stopping Main Thread");
	}
}
