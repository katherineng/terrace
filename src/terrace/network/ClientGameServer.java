package terrace.network;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.List;

import terrace.GameServer;
import terrace.Move;

public class ClientGameServer extends GameServer {
	private final Socket _conn;
	private final BufferedReader _in;
	private final PrintWriter _out;
	private final List<String> _names;
	private final Runnable _onDrop;
	
	public ClientGameServer(
			String host,
			int port,
			List<String> names,
			Runnable onDrop
	) throws IOException {
		_conn = new Socket(host, port);
		_in = new BufferedReader(new InputStreamReader(_conn.getInputStream()));
		_out = new PrintWriter(_conn.getOutputStream(), true);
		_names = names;
		_onDrop = onDrop;
	}
	
	@Override
	public void close() throws IOException {
		_conn.close();
	}
	
	@Override
	public void run() {
		try {
			if (!"TERRACE-Server".equals(_in.readLine())) {
				System.err.println("LOG: Bad server. Disconnecting.");
				return;
			}
			for (String name : _names) {
				_out.println(name);
			}
			_out.println();
		} catch (IOException e) {
			_onDrop.run();
		}
	}
	
	public void sendMove(Move m, int turnNumber) {
		_out.println(
				turnNumber + " " +
				m.getPiece().getPosn().getX() + " " +
				m.getPiece().getPosn().getY() + " " +
				m.getTo().getX() + " " + m.getTo().getY()
		);
	}
}
