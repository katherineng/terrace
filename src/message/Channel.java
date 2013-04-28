package message;

import java.util.concurrent.TimeUnit;

public class Channel<M> {
	private final Port<M> _port;
	
	Channel(Port<M> port) {
		_port = port;
	}
	public void send(M msg) throws InterruptedException {
		_port._queue.put(msg);
	}
	public boolean send(M msg, long timeout, TimeUnit unit) {
		try {
			return _port._queue.offer(msg, timeout, unit);
		} catch (InterruptedException e) {
			return false;
		}
	}
}
