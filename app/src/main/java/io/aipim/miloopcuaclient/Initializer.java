package io.aipim.miloopcuaclient;

import java.util.concurrent.ConcurrentLinkedQueue;

public class Initializer {

	Initializer(TargetReader tr, Exporter exporter) {
		var q = new ConcurrentLinkedQueue<ThreadMessage>();

		var th = new ManagedThread(
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
