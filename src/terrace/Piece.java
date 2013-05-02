package terrace;

import terrace.util.Copyable;
import terrace.util.Posn;

import com.google.common.base.*;

public class Piece implements Comparable<Piece>, Copyable<Piece> {
	protected final int _size;
	protected Posn _posn;
	protected Player _player;
	
	public Piece(int size, Posn posn, Player player) {
		assert(player != null);
		_size = size;
		_posn = posn;
		_player = player;
	}
	
	public void updatePosn(Posn posn) {
		_posn = posn;
	}
	
	public int getSize() {
		return _size;
	}
	
	public Posn getPosn() {
		return _posn;
	}
	
	public void setPosn(Posn p) {
		_posn.x = p.x;
		_posn.y = p.y;
	}
	
	public PlayerColor getColor() {
		return _player.getColor();
	}
	
	@Override
	public int compareTo(Piece o) {
		return _size - o.getSize();
	}
	
	@Override
	public String toString() {
		return "(" + getColor().toString() + ", " + _size + ")";
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(_size, _posn, getColor());
	}
	
	@Override
	public boolean equals(Object o) {
		if (o == null) return false;
		if (o == this) return true;
		if (o.getClass() != getClass()) return false;
		
		Piece p = (Piece)o;
		return p._size == _size && p.getColor().equals(getColor()) && p._posn.equals(_posn);
	}
	
	@Override
	public Piece copy() {
		return new Piece(_size, _posn.clone(), _player);
	}

	public Player getPlayer() {
		return _player;
	}
}
