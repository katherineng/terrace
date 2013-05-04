package terrace.network;

import java.io.Closeable;
import java.io.IOException;
import java.net.ServerSocket;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ExecutorService;

public class HostServer implements Runnable, Closeable {
	private final int _port;
	private final ExecutorService _es;
	private boolean _closed = false;
	private Set<ClientConnection> _clients = new HashSet<>();
	
	public HostServer(int port, ExecutorService es) {
		_port = port;
		_es = es;
	}
	
	@Override
	public void run() {
		try (ServerSocket server = new ServerSocket(_port)) {
			while (!_closed) {
				ClientConnection _conn = new ClientConnection(server.accept()); 
				
				_clients.add(_conn);
				_es.submit(_conn);
			}
		} catch (IOException e) {
			System.err.println("LOG: " + e.getLocalizedMessage());
		}
	}
	
	@Override
	public void close() throws IOException {
		_closed = true;
		
		for (ClientConnection c : _clients) c.close();
	}
}
