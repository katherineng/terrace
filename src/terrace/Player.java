package terrace;

import java.util.*;

public class Player {
	private Set<Piece> _pieces;
	
	public Player() {
		_pieces = new HashSet<Piece>();
	}
	
	public void removePiece(Piece piece) {
		_pieces.remove(piece);
	}
	
	public void addPiece(Piece piece) {
		_pieces.add(piece);
	}
	
	public Set<Piece> getPieces() {
		return _pieces;
	}
	
	void move(Piece piece, Posn to) {}
}
