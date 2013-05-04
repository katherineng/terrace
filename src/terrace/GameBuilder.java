package terrace;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;
import java.util.List;

import terrace.ai.AI;
import terrace.gui.LocalPlayer;
import terrace.network.Request;
import terrace.util.Callback;

public class GameBuilder {
	private int totalPlayers;
	private int localPlayers;
	private int networkPlayers;
	private GameType _type;
	private Variant _variant;
	private int _size = 8;
	
	private List<String> _names;
	
	public GameBuilder() {}
	
	public void setNumLocalPlayers(int num) {
		assert num >= 1 && num <= 4;
		localPlayers = num;
	}
	
	public int getNumLocalPlayers() {
		return localPlayers;
	}
	public int getNumAIPlayers() {
		switch (localPlayers) {
		case 1: return 1;
		case 2: return 0;
		case 3: return 1;
		case 4: return 0;
		}
		assert false;
		return -1;
	}
	public void setVariant(Variant variant) {
		_variant = variant;
	}
	public void setSize(int size) {
		_size = size;
	}
	public void localGame() {
		_type = GameType.Local;
	}
	public void hostGame(
			final int port,
			Callback<Request> newRequest,
			Callback<Request> requestDropped
	) {
		_type = GameType.Host;
		
		new Thread() {
			@Override
			public void run() {
				try {
					ServerSocket server = new ServerSocket(port);
					
					while (true) {
						final Socket client = server.accept();
						
						new Thread() {
							@Override
							public void run() {
								dispatchClient(client);
							}
						}.start();
					}
				} catch (IOException e) {
					System.err.println("LOG: " + e.getLocalizedMessage());
				}
			}
		}.start();
	}
	public GameServer startGame() {
		List<Player> players = new LinkedList<>();
		
		int playerNum = 0;
		
		for (int i = 0; i < localPlayers; i++) {
			players.add(new LocalPlayer(PlayerColor.values()[playerNum]));
			playerNum++;
		}
		for (int i = 0; i < getNumAIPlayers(); i++) {
			players.add(new AI(PlayerColor.values()[playerNum]));
			playerNum++;
		}
		
		GameState game = new GameState(BoardFactory.create(players, _size, _variant), players, 0);
		final GameServer s = new GameServer(game);
		
		int i = 0;
		for (final Player p : players) {
			if (p instanceof AI) {
				s.addUpdateStateCB(new Callback<GameState>() {
					@Override
					public void call(GameState state) {
						((AI)p).updateGameState(state);
					}
				});
			}
			p.setName(_names.get(i++));
		}
		
		new Thread() {
			@Override
			public void run() {
				try {
					s.run();
				} catch (Throwable t) {
					t.printStackTrace();
				}
			}
		}.start();
		
		return s;
	}
	
	private enum GameType {
		Local, Host, Client
	}
	
	public void setPlayerNames(List<String> names) {
		_names = names;
	}
	
	private void dispatchClient(Socket client) {
		try {
			client.getOutputStream().write("TERRACE-Server\n".getBytes());
			
			BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));
			
			if (!"TERRACE-Client".equals(in.readLine())) {
				System.err.println("LOG: Bad client. Dropping.");
				return;
			}
			
			
		} catch (IOException e) {
			 System.err.println("LOG: " + e.getLocalizedMessage());
		}
	}
}
