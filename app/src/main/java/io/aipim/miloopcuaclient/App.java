package io.aipim.miloopcuaclient;

import io.aipim.miloopcuaclient.Exporter.ConsoleExporter;
import io.aipim.miloopcuaclient.TargetReader.JsonTargetReader;
import java.io.IOException;
import java.util.Properties;
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

	private Properties props = new Properties();

	public static void main(String[] args) {
		System.exit(
			new CommandLine(new App()).execute(args)
		);
	}

	@Override
	public void run() {
		try (
			final var st = App.class.getClassLoader()
				.getResourceAsStream("config.properties")
		) {
			props.load(st);
		} catch (IOException e) {
			log.error(
				"Config properties file not found",
				e
			);
		}

		new Initializer(
			new JsonTargetReader(),
			new ConsoleExporter(),
			configFactory()
		)
			.run();
		log.info("Stopping Main Thread");
	}

	private Config configFactory() {
		var config = new Config();
		if (url != null) config.setUrl(
			url
		); else config.setUrl(props.get("url").toString());
		return config;
	}
}
