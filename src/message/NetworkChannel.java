package message;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.Socket;
import java.util.concurrent.TimeUnit;

public class NetworkChannel<M extends Serializable> implements Channel<M> {
	private final Socket _socket;
	private final ObjectOutputStream _out;
	
	public NetworkChannel(String host, int port) throws IOException {
		_socket = new Socket(host, port);
		_out = new ObjectOutputStream(_socket.getOutputStream());
	}
	@Override
	public void send(M msg) throws InterruptedException {
		try {
			_out.writeObject(msg);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	@Override
	public boolean send(M msg, long timeout, TimeUnit unit) {
		try {
			int old = _socket.getSoTimeout();
			
			try {
				_socket.setSoTimeout((int)unit.toMillis(timeout));
				_out.writeObject(msg);
			} finally {
				_socket.setSoTimeout(old);
			}
		} catch (IOException e) {
			return false;
		}
		return true;
	}
	@Override
	public void close() throws IOException {
		_socket.close();
	}
}
