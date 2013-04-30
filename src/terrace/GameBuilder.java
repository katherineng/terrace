package terrace;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class GameBuilder {
	private ExecutorService es = Executors.newCachedThreadPool();
	
	private int totalPlayers;
	private int localPlayers;
	private int aiPlayers;
	private int networkPlayers;
	private GameType type;
	
	public GameBuilder() {}
	
	public void localPlayers(int num) {
		assert num >= 1 && num <= 4;
		
		localPlayers = num;
	}
	public void localGame() {
		type = GameType.Local;
	}
	public void hostGame() {
		
	}
	public void startGame() {
		
	}
	
	private enum GameType {
		Local, Host, Client
	}
}
