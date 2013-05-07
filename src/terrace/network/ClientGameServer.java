package terrace.network;

import java.io.BufferedReader;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.LinkedList;
import java.util.List;

import terrace.GameServer;
import terrace.GameState;
import terrace.Move;
import terrace.Player;
import terrace.PlayerColor;
import terrace.util.Callback;

public class ClientGameServer extends GameServer {
	private final Socket _conn;
	private final BufferedReader _in;
	private final PrintWriter _out;
	private final List<String> _localNames;
	private final Runnable _onDrop;
	private final List<Player> _players = new LinkedList<>();
	
	public ClientGameServer(
			String host,
			int port,
			List<String> names,
			Runnable onDrop
	) throws IOException {
		_conn = new Socket(host, port);
		_in = new BufferedReader(new InputStreamReader(_conn.getInputStream()));
		_out = new PrintWriter(_conn.getOutputStream(), true);
		_localNames = names;
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
			for (String name : _localNames) {
				_out.println(name);
			}
			_out.println();
			
			readPlayers();
			
			while (true) {
				GameState state = GameState.read(_in, _players);
				
				for (Callback<GameState> cb : _updateStateCBs) {
					cb.call(state);
				}
				if (state.getWinner().isPresent()) return;
			}
		} catch (IOException e) {
			_onDrop.run();
		}
	}
	
	private void readPlayers() throws IOException {
		String idxLine = _in.readLine();
		
		if (idxLine == null) throw new EOFException("Server connection closed");
		
		int idx;
		try {
			idx = Integer.parseInt(idxLine);
		} catch (NumberFormatException e) {
			throw new IOException("Server sent bad data");
		}
		if (idx < 0) throw new IOException("Server sent bad data");
		
		List<String> playerNames = new LinkedList<>();
		
		while (true) {
			String line = _in.readLine();
			
			if (line == null) throw new EOFException("Server connection closed");
			if (line.equals("")) break;
			
			playerNames.add(line);
		}
		if (playerNames.size() != 2 && playerNames.size() != 4) {
			throw new IOException("Server sent bad number of players");
		}
		if (idx > playerNames.size() - _localNames.size()) {
			throw new IOException("Server sent bad local player index");
		}
		
		int i = 0;
		for (String name : playerNames) {
			Player p;
			
			if (i >= idx && i < idx + _localNames.size()) {
				p = new ClientLocalPlayer(PlayerColor.values()[i], this);
			} else {
				p = new NetworkedClientPlayer(PlayerColor.values()[i]);
			}
			_players.add(p);
			p.setName(name);
			i++;
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
