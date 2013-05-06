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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import terrace.util.Callback;
import terrace.util.Posn;

public class ClientConnection implements Closeable, Runnable {
	private static final Pattern MOVE = Pattern.compile("([0-9]+) ([0-9]+) ([0-9]+) ([0-9]+) ([0-9]+)");
	
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
			
			mainLoop();
			
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
	
	private void readNames() throws IOException {
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
	
	private void mainLoop() throws IOException {
		while (true) {
			String line = _in.readLine();
			
			if (_in == null) {
				throw new EOFException("Client connection closed");
			} else {
				Matcher m = MOVE.matcher(line);
				
				if (m.matches()) {
					int stateNum = Integer.parseInt(m.group(1));
					
					Posn from = new Posn(Integer.parseInt(m.group(2)), Integer.parseInt(m.group(3)));
					Posn to = new Posn(Integer.parseInt(m.group(4)), Integer.parseInt(m.group(5)));
					
					
				} else {
					// TODO
				}
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
