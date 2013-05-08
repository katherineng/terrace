package terrace;

import java.io.Closeable;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import terrace.ai.AI;
import terrace.network.ClientConnection;
import terrace.network.ClientGameServer;
import terrace.network.HostServer;
import terrace.network.NetworkedServerPlayer;
import terrace.util.Callback;

public class GameBuilder implements Closeable {
	public static final int DEFAULT_PORT = 5678;
	
	private final ExecutorService _es = Executors.newCachedThreadPool();
	private List<Closeable> _resources = new LinkedList<>();
	
	private int _localPlayers;
	private int _networkPlayers = 0;
	private Variant _variant;
	private int _size = 8;
	
	private List<String> _names;
	
	public GameBuilder() {}
	
	public void setNumLocalPlayers(int num) {
		assert num >= 1 && num <= 4;
		_localPlayers = num;
	}
	
	public int getNumLocalPlayers() {
		return _localPlayers;
	}
	
	public int getNumAIPlayers() {
		switch (_localPlayers + _networkPlayers) {
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
	
	public Variant getVariant() {
		return _variant;
	}
	
	public void setSize(int size) {
		_size = size;
	}
	
	public void hostGame(
			final int port,
			Callback<ClientConnection> newRequest,
			Callback<ClientConnection> connectionDropped
	) {
		HostServer hs = new HostServer(port, _es, newRequest, connectionDropped);
		
		_resources.add(hs);
		_es.submit(hs);
	}
	
	public GameServer startGame(List<ClientConnection> clients) {
		List<Player> players = new LinkedList<>();
		List<AI> aiPlayers = new LinkedList<>();
		
		int playerNum = 0;
		Map<ClientConnection, Integer> clientPlayerStartIdxs = new HashMap<>();
		
		for (int i = 0; i < _localPlayers; i++) {
			players.add(new LocalServerPlayer(PlayerColor.values()[playerNum]));
			playerNum++;
		}
		for (ClientConnection conn : clients) {
			clientPlayerStartIdxs.put(conn, playerNum);
			for (String name : conn.getPlayerNames()) {
				_names.add(name);
				players.add(new NetworkedServerPlayer(conn, name, PlayerColor.values()[playerNum]));
				_networkPlayers++;
				playerNum++;
			}
		}
		for (int i = 0; i < getNumAIPlayers(); i++) {
			aiPlayers.add(new AI(PlayerColor.values()[playerNum]));
		}
		players.addAll(aiPlayers);
		
		GameState initialState = new GameState(BoardFactory.create(players, _size, _variant), players, 0, 0);
		final GameServer s = new LocalGameServer(initialState);
		_resources.add(s);
		
		for (final ClientConnection conn : clients) {
			s.addUpdateStateCB(new Callback<GameState>() {
				@Override
				public void call(final GameState state) {
					_es.submit(new Runnable() {
						@Override
						public void run() {
							conn.updateGameState(state);
						}
					});
				}
			});
		}
		for (final AI ai : aiPlayers) {
			s.addUpdateStateCB(new Callback<GameState>() {
				@Override
				public void call(GameState state) {
					ai.updateGameState(state);
				}
			});
		}
		
		int i = 0;
		for (final Player p : players) {
			if (i >= _names.size()) {
				p.setName("CPU");
			} else {
				p.setName(_names.get(i));
			}
			i++;
		}
		
		for (ClientConnection conn : clients) {
			conn.initializeGameState(initialState, clientPlayerStartIdxs.get(conn));
		}
		
		_es.submit(new Runnable() {
			@Override
			public void run() {
				try {
					s.run(null);
				} catch (Throwable t) {
					t.printStackTrace();
				}
			}
		});
		
		return s;
	}
	
	public void setPlayerNames(List<String> names) {
		_names = names;
	}
	
	public void joinGame(
			final String host,
			final int port,
			final Callback<GameServer> onStart,
			final Runnable onDrop
	) {
		_es.submit(new Runnable() {
			@Override
			public void run() {
				try (final GameServer gs = new ClientGameServer(
						host,
						port,
						_names,
						onDrop
				)) {
					gs.run(new Runnable() {
						@Override
						public void run() {
							_resources.add(gs);
							onStart.call(gs);
						}
					});
				} catch (IOException e) {
					System.err.println("LOG: " + e.getLocalizedMessage());
					onDrop.run();
				}
			}
		});
	}
	
	@Override
	public void close() {
		for (Closeable resource : _resources) {
			try {
				resource.close();
			} catch (IOException e) {
				System.err.println("LOG: " + e.getLocalizedMessage());
			}
		}
	}
}
