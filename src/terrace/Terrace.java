package terrace;

import java.util.List;
import java.util.Set;

public class Terrace {
	private final Game game;
	
	public Terrace(int localPlayers, int aiPlayers, int networkPlayers) {
		this.game = new Game(localPlayers + aiPlayers + netWorkPlayers);
		
		
	}
	public List<Player> getLocalPlayers() {
		
	}
	public void setNetworkInfo(...) {
		
	}
}
