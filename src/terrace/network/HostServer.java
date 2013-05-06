package terrace.network;

import java.io.Closeable;
import java.io.IOException;
import java.net.ServerSocket;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ExecutorService;

import terrace.util.Callback;

public class HostServer implements Runnable, Closeable {
	private final int _port;
	private final ExecutorService _es;
	private final Callback<ClientConnection> _newRequest;
	private final Callback<ClientConnection> _connDropped;
	
	private boolean _closed = false;
	private Set<ClientConnection> _clients = new HashSet<>();
	
	public HostServer(
			int port,
			ExecutorService es,
			Callback<ClientConnection> newRequest,
			Callback<ClientConnection> connectionDropped
	) {
		_port = port;
		_es = es;
		_newRequest = newRequest;
		_connDropped = connectionDropped;
	}
	
	@Override
	public void run() {
		try (ServerSocket server = new ServerSocket(_port)) {
			while (!_closed) {
				ClientConnection conn = new ClientConnection(server.accept(), _newRequest, _connDropped);
				
				_clients.add(conn);
				_es.submit(conn);
			}
		} catch (IOException e) {
			System.err.println("LOG: " + e.getLocalizedMessage());
		}
	}
	
	@Override
	public void close() {
		_closed = true;
		
		for (ClientConnection c : _clients) {
			try {
				c.close();
			} catch (IOException e) {
				System.err.println("LOG: " + e.getLocalizedMessage());
				// Swallow
			}
		}
	}
}
