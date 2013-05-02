package terrace;

import terrace.util.Copyable;
import terrace.util.Posn;

import com.google.common.base.*;

public class Piece implements Comparable<Piece>, Copyable<Piece> {
	protected final int _size;
	protected Posn _posn;
	protected PlayerColor _color;
	
	public Piece(int size, Posn posn, PlayerColor color) {
		_size = size;
		_posn = posn;
		_color = color;
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
		return _color;
	}
	
	@Override
	public int compareTo(Piece o) {
		return _size - o.getSize();
	}
	
	@Override
	public String toString() {
		return "(" + _color.toString() + ", " + _size + ")";
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(_size, _posn, _color);
	}
	
	@Override
	public boolean equals(Object o) {
		if (o == null) return false;
		if (o == this) return true;
		if (o.getClass() != getClass()) return false;
		
		Piece p = (Piece)o;
		return p._size == _size && p._color == _color && p._posn.equals(_posn);
	}
	
	@Override
	public Piece copy() {
		return new Piece(_size, _posn, _color);
	}
}
