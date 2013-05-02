package terrace;

import java.util.*;

import com.google.common.base.Optional;


public abstract class Player {
	private List<Piece> _pieces;
	PlayerColor _color;
	String _name;
	
	public Player(PlayerColor color) {
		_pieces = new LinkedList<Piece>();
		_color = color;
		_name = "";
	}
	
	/**
	 * Gets a player's next move.
	 * 
	 * @param timeout The maximum number of seconds to block
	 * @return        The player's desired move, if any
	 */
	public abstract Optional<Move> getMove(int timeout);
	
	/**
	 * Notifies the player about an update to the board
	 * 
	 * @param game The new state of the board
	 */
	public abstract void updateState(GameState game);
	
	/**
	 * Notifies the player about a winner.
	 * 
	 * @param winner The winner of the game
	 */
	public void notifyWinner(Player winner) {}
	
	/**
	 * Notifies the player about a loser. This doesn't mean the game is over.
	 * 
	 * @param loser The player who has lost
	 */
	public void notifyLoser(Player loser) {}
	
	/**
	 * @return true if a move was made (AI), false otherwise
	 */
	public boolean makeMove() {
		return false;
	}
	
	public void removePiece(Piece piece) {
		_pieces.remove(piece);
	}
	
	public void addPiece(Piece piece) {
		_pieces.add(piece);
	}
	
	public List<Piece> getPieces() {
		return _pieces;
	}
	
	public PlayerColor getColor() {
		return _color;
	}


	public String getName() {
		return _name;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Player other = (Player) obj;
		if (_color != other._color)
			return false;
		return true;
	}

	@Override
	public String toString(){
		return _color.toString();
	}
}
