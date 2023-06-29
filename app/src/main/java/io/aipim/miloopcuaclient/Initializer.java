package io.aipim.miloopcuaclient;

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
		final var q =
			new ConcurrentLinkedQueue<ThreadMessage>();

		final var th = new ManagedThread(
			q,
			() -> new Watcher(tr.getMap(), exporter)
		);

		Runtime
			.getRuntime()
			.addShutdownHook(
				new Thread(() -> {
					q.offer(ThreadMessage.EXIT);
					try {
						th.join();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				})
			);

		th.start();
	}
}
