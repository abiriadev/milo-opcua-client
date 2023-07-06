package io.aipim.miloopcuaclient;

import io.aipim.miloopcuaclient.Exporter.Exporter;
import io.aipim.miloopcuaclient.TargetReader.TargetReader;
import java.util.concurrent.ConcurrentLinkedQueue;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@AllArgsConstructor
public final class Initializer implements Runnable {

	private TargetReader tr;
	private Exporter exporter;
	private Config config;

	@Override
	public void run() {
		final var th = new ManagedThread(
			new ConcurrentLinkedQueue<ThreadMessage>(),
			() -> new Watcher(tr.getMap(), exporter, config)
		);

		Runtime
			.getRuntime()
			.addShutdownHook(new Thread(() -> th.exit()));

		th.start();

		try {
			// blocks the main thread for picocli
			th.join();
		} catch (InterruptedException e) {
			log.error("ManagedThread join interrupted", e);
		}
	}
}
