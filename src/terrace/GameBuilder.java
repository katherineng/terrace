package terrace;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import terrace.ai.AI;
import terrace.gui.LocalPlayer;
import terrace.util.Callback;

public class GameBuilder {
	private ExecutorService es = Executors.newCachedThreadPool();
	
	private int totalPlayers;
	private int localPlayers;
	private int networkPlayers;
	private GameType type;
	private Variant _variant;
	private int _size = 8;
	
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
		type = GameType.Local;
	}
	public void hostGame() {
		
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
		
		for (final Player p : players) {
			if (p instanceof AI) {
				s.addUpdateStateCB(new Callback<GameState>() {
					@Override
					public void call(GameState state) {
						((AI)p).updateGameState(state);
					}
				});
			}
		}
		
		es.submit(new Runnable() {
			@Override
			public void run() {
				s.run();
			}
		});
		
		return s;
	}
	
	private enum GameType {
		Local, Host, Client
	}
}
