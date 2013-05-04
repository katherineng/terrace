package terrace.network;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.LinkedList;
import java.util.List;

public class ClientConnection implements Closeable, Runnable {
	private final Socket _conn;
	
	public ClientConnection(Socket conn) {
		_conn = conn;
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
			
			List<String> names = new LinkedList<>();
			
			while (true) {
				String line = in.readLine();
				
				if (line == null) {
					return;
				} else if (line.equals("")) {
					break;
				} else {
					names.add(line);
				}
			}
		} catch (IOException e) {
			 System.err.println("LOG: " + e.getLocalizedMessage());
		}
	}
}
