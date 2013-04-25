package terrace;

import java.util.*;

public class Player {
	private Set<Piece> _pieces;
	public PlayerColor _color;
	
	public Player(PlayerColor color) {
		_pieces = new HashSet<Piece>();
		_color = color;
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
	
	public PlayerColor getColor() {
		return _color;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((_color == null) ? 0 : _color.hashCode());
		return result;
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
}
