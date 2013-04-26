package terrace;

import java.util.*;

public class Player {
	private Set<Piece> _pieces;
	PlayerColor _color;
	String _name;
	
	public Player(PlayerColor color) {
		_pieces = new HashSet<Piece>();
		_color = color;
		_name = "";
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
	
	public String getName() {
		return _name;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((_color == null) ? 0 : _color.hashCode());
		result = prime * result + ((_name == null) ? 0 : _name.hashCode());
		return result;
	}

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
}
