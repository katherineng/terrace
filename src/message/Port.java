package message;

import java.io.IOException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import com.google.common.base.Optional;

public class Port<M> {
	final BlockingQueue<M> _queue;
	
	public Port() {
		_queue = new LinkedBlockingQueue<>();
	}
	public Port(int capacity) {
		_queue = new LinkedBlockingQueue<>(capacity);
	}
	public Channel<M> newChannel() {
		return new Channel<M>() {
			private boolean _closed = false;
			
			@Override
			public void send(M msg) throws IOException, InterruptedException {
				_queue.put(msg);
			}
			@Override
			public boolean send(M msg, long timeout, TimeUnit unit) {
				try {
					return _queue.offer(msg, timeout, unit);
				} catch (InterruptedException e) {
					return false;
				}
			}
			@Override
			public void close() throws IOException {
				_closed = true;
			}
		};
	}
	public M get() throws InterruptedException {
		return _queue.take();
	}
	public M get(long timeout, TimeUnit unit) throws InterruptedException, TimeoutException {
		M msg = _queue.poll(timeout, unit);
		
		if (msg == null) {
			throw new TimeoutException();
		} else {
			return msg;
		}
	}
	public Optional<M> tryGet(long timeout, TimeUnit unit) {
		try {
			return Optional.of(get(timeout, unit));
		} catch (Exception e) {
			return Optional.absent();
		}
	}
}
