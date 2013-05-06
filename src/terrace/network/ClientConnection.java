package terrace.network;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.LinkedList;
import java.util.List;

import terrace.util.Callback;

public class ClientConnection implements Closeable, Runnable {
	private final Socket _conn;
	private final BufferedReader _in;
	private final PrintWriter _out;
	
	private final Callback<ClientConnection> _onReady;
	private final Callback<ClientConnection> _onDrop;
	
	private final List<String> _names = new LinkedList<>();
	
	public ClientConnection(
			Socket conn,
			Callback<ClientConnection> onReady,
			Callback<ClientConnection> onDrop
	) throws IOException {
		_conn = conn;
		_in = new BufferedReader(new InputStreamReader(_conn.getInputStream()));
		_out = new PrintWriter(_conn.getOutputStream(), true);
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
			_out.println("TERRACE-Server");
			
			if (!"TERRACE-Client".equals(_in.readLine())) {
				System.err.println("LOG: Bad client. Dropping.");
				return;
			}
			
			readNames();
			
			_onReady.call(this);
		} catch (IOException e) {
			 System.err.println("LOG: " + e.getLocalizedMessage());
		} finally {
			_onDrop.call(this);
			
			try {
				close();
			} catch (IOException e) {
				System.err.println("LOG: " + e.getLocalizedMessage());
			}
		}
	}
	
	public void readNames() throws IOException {
		while (true) {
			String line = _in.readLine();
			
			if (line == null) {
				throw new EOFException("Client connection closed");
			} else if (line.equals("")) {
				return;
			} else {
				_names.add(line);
			}
		}
	}
	
	@Override
	public String toString() {
		String result  = "<html>";
		
		boolean first = true;
		for (String name : _names) {
			if (first) {
				first = false;
			} else {
				result += "<br>";
			}
			result += name;
		}
		result += "</html>";
		
		return result;
	}
}
