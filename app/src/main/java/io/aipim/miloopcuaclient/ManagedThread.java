package io.aipim.miloopcuaclient;

import java.util.concurrent.ConcurrentLinkedQueue;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ManagedThread extends Thread {

	int interval = 10;
	ConcurrentLinkedQueue<ThreadMessage> q;
	Runnable cb;

	ManagedThread(
		ConcurrentLinkedQueue<ThreadMessage> q,
		Runnable cb
	) {
		this.q = q;
		this.cb = cb;
	}

	ManagedThread(
		ConcurrentLinkedQueue<ThreadMessage> q,
		int interval,
		Runnable cb
	) {
		this(q, cb);
		this.interval = interval;
	}

	@Override
	public void run() {
		cb.run();
		loop:while (true) if (q.peek() == null) try {
			Thread.sleep(interval);
		} catch (InterruptedException e) {
			log.error("ManagedThread sleep interrupted", e);
		} else switch (q.poll()) {
			case EXIT:
				break loop;
		}
	}

	void exit() {
		log.info(
			"exit ManagedThread: {}",
			Thread.currentThread().getName()
		);
	}
}
