package terrace;

public class GameBuilder {
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
	
	
	private enum GameType {
		Local, Host, Client
	}
}
