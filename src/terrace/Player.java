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
	 * @param board The new state of the board
	 */
	public abstract void updateBoard(Board board);
	
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
	
	//void move(Piece piece, Posn to) {}
	
	public PlayerColor getColor() {
		return _color;
	}

//	@Override
//	public int hashCode() {
//		final int prime = 31;
//		int result = 1;
//		result = prime * result + ((_color == null) ? 0 : _color.hashCode());
//		result = prime * result + ((_name == null) ? 0 : _name.hashCode());
//		return result;
//	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof Player))
			return false;
		Player other = (Player) obj;
		if (_color != other._color)
			return false;
		if (_name == null) {
			if (other._name != null)
				return false;
		} else if (!_name.equals(other._name))
			return false;
		return true;
	}

	public String getName() {
		return _name;
	}
	
	@Override
	public String toString(){
		return _color.toString();
	}
}
