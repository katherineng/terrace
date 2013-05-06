package terrace.network;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.LinkedList;
import java.util.List;

import terrace.util.Callback;

public class ClientConnection implements Closeable, Runnable {
	private final Socket _conn;
	private final Callback<ClientConnection> _onReady;
	private final Callback<ClientConnection> _onDrop;
	
	private final List<String> _names = new LinkedList<>();
	
	public ClientConnection(
			Socket conn,
			Callback<ClientConnection> onReady,
			Callback<ClientConnection> onDrop
	) {
		_conn = conn;
		_onReady = onReady;
		_onDrop = onDrop;
	}
	
	/**
	 * It is illegal to call this before the onReady callback has been executed.
	 * 
	 * @return The players' names.
	 */
	public List<String> getPlayerNames() {
		return _names;
	}
	
	@Override
	public void close() throws IOException {
		_conn.close();
	}
	
	@Override
	public void run() {
		try {
			PrintWriter out = new PrintWriter(_conn.getOutputStream(), true);
			BufferedReader in = new BufferedReader(new InputStreamReader(_conn.getInputStream()));
			
			out.println("TERRACE-Server");
			
			if (!"TERRACE-Client".equals(in.readLine())) {
				System.err.println("LOG: Bad client. Dropping.");
				return;
			}
			
			while (true) {
				String line = in.readLine();
				
				if (line == null) {
					return;
				} else if (line.equals("")) {
					break;
				} else {
					_names.add(line);
				}
			}
			_onReady.call(this);
			
			
		} catch (IOException e) {
			 System.err.println("LOG: " + e.getLocalizedMessage());
		} finally {
			_onDrop.call(this);
		}
	}
	
	@Override
	public String toString() {
		String result  = "";
		
		boolean first = true;
		for (String name : _names) {
			if (first) {
				first = false;
			} else {
				result += ", ";
			}
			result += name;
		}
		
		return result;
	}
}
