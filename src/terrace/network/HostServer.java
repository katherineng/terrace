package terrace.network;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.concurrent.ExecutorService;

public class HostServer implements Runnable {
	private final int _port;
	private final ExecutorService _es;
	
	public HostServer(int port, ExecutorService es) {
		_port = port;
		_es = es;
	}
	
	@Override
	public void run() {
		try {
			ServerSocket server = new ServerSocket(_port);
			
			while (true) {
				_es.submit(new ClientConnection(server.accept()));
			}
		} catch (IOException e) {
			System.err.println("LOG: " + e.getLocalizedMessage());
		}
	}
}
