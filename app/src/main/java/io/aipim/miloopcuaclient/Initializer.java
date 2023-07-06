package io.aipim.miloopcuaclient;

import io.aipim.miloopcuaclient.Exporter.Exporter;
import io.aipim.miloopcuaclient.TargetReader.TargetReader;
import java.util.concurrent.ConcurrentLinkedQueue;

public final class Initializer implements Runnable {

	private TargetReader tr;
	private Exporter exporter;

	Initializer(TargetReader tr, Exporter exporter) {
		this.tr = tr;
		this.exporter = exporter;
	}

	@Override
	public void run() {
		final var th = new ManagedThread(
			new ConcurrentLinkedQueue<ThreadMessage>(),
			() -> new Watcher(tr.getMap(), exporter)
		);

		Runtime
			.getRuntime()
			.addShutdownHook(new Thread(() -> th.exit()));

		th.start();

		try {
			// blocks the main thread for picocli
			th.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
