package terrace;

import java.util.List;
import java.util.Set;

public class Terrace {
	private final Game game;
	
	public Terrace(int localPlayers, int aiPlayers, int networkPlayers) {
		this.game = new Game(localPlayers + aiPlayers + networkPlayers);
		
		
	}
	
	public List<Player> getLocalPlayers() {
		return null;
	}
	
	public void setNetworkInfo() {
		
	}
}
