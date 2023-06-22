package io.aipim.miloopcuaclient;

import java.util.concurrent.ConcurrentLinkedQueue;

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
			e.printStackTrace();
		} else switch (q.poll()) {
			case EXIT:
				System.out.println("exit thread");
				break loop;
		}
	}
}
