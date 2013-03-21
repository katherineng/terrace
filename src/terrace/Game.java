package terrace;

import java.util.List;
import java.util.Set;

public class Game {
	public Game(int numPlayers) {
		
	}
	public Board getBoard() {
		
	}
	public Player getCurrentPlayer() {
		
	}
	/**
	 * @return All the players in the game
	 */
	public List<Player> getPlayers();
	/**
	 * @param p A player
	 * @return  All the live pieces owned by that player
	 */
	public Set<Piece> getPiecesOf(Player p);
}
