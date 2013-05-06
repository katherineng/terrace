package terrace;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import terrace.ai.AI;
import terrace.gui.game.LocalPlayer;
import terrace.network.ClientConnection;
import terrace.network.HostServer;
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
		switch (_localPlayers) {
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
			Callback<ClientConnection> newRequest
	) {
		_type = NetworkType.HOST;
		
		_es.submit(new HostServer(port, _es, newRequest));
	}
	public GameServer startGame() {
		List<Player> players = new LinkedList<>();
		
		int playerNum = 0;
		
		for (int i = 0; i < _localPlayers; i++) {
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
