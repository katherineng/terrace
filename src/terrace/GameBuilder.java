package terrace;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import terrace.ai.AI;
import terrace.gui.game.LocalPlayer;
import terrace.network.ClientConnection;
import terrace.network.HostServer;
import terrace.network.NetworkedServerPlayer;
import terrace.util.Callback;

public class GameBuilder {
	public static final int DEFAULT_PORT = 5678;
	
	private final ExecutorService _es = Executors.newCachedThreadPool();
	
	private int _localPlayers;
	private int _networkPlayers = 0;
	private NetworkType _type;
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
	public void localGame() {
		_type = NetworkType.LOCAL;
	}
	
	public void hostGame(
			final int port,
			Callback<ClientConnection> newRequest,
			Callback<ClientConnection> connectionDropped
	) {
		_type = NetworkType.HOST;
		
		_es.submit(new HostServer(port, _es, newRequest, connectionDropped));
	}
	
	public GameServer startGame(List<ClientConnection> clients) {
		List<Player> players = new LinkedList<>();
		List<AI> aiPlayers = new LinkedList<>();
		
		int playerNum = 0;
		
		for (int i = 0; i < _localPlayers; i++) {
			players.add(new LocalPlayer(PlayerColor.values()[playerNum]));
			playerNum++;
		}
		for (ClientConnection conn : clients) {
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
			conn.updateGameState(initialState);
		}
		
		_es.submit(new Runnable() {
			@Override
			public void run() {
				try {
					s.run();
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
}
