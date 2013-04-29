package terrace;

import com.google.common.base.*;

public class Piece implements Comparable<Piece>, Cloneable{
	private final int _size;
	private Optional<Posn> _goalPosn;
	private Posn _posn;
	private Player _player;
	
	public Piece(int size, boolean isTPiece, Posn posn, int dimensions, Player player) {
		_size = size;
		_posn = posn;
		_player = player;
		
		if (isTPiece) {
			_goalPosn = Optional.of(new Posn(Math.abs(posn.x - (dimensions - 1)), Math.abs(posn.y - (dimensions - 1))));
		} else {
			_goalPosn = Optional.absent();
		}
		
	}
	
	public Piece clone() throws CloneNotSupportedException{
		Piece toRet = (Piece) super.clone();
		toRet._posn = _posn.clone();
		return toRet;
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
	
	public boolean isTPiece() {
		return _goalPosn.isPresent();
	}
	
	public Player getPlayer() {
		return _player;
	}
	
	public Optional<Posn> getGoalPosn() {
		return _goalPosn;
	}

	@Override
	public int compareTo(Piece o) {
		return _size - o.getSize();
	}
	
//	@Override
//	public int hashCode() {
//		return _posn.hashCode();
//	}
//	
//	@Override
//	public boolean equals(Object o) {
//		if (this.getClass() == o.getClass()) {
//			Piece p = (Piece) o;
//			return _size == p.getSize() && _goalPosn.equals(p.getGoalPosn()) &&  _posn.equals(p.getPosn());
//		}
//		return false;
//	}
	
	@Override
	public String toString() {
		if (_goalPosn.isPresent()) {
			return "(" + _player.getColor().toString() + ", T)";
		} else {
			return "(" + _player.getColor().toString() + ", " + _size + ")";
		}
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((_goalPosn == null) ? 0 : _goalPosn.hashCode());
		result = prime * result + ((_player == null) ? 0 : _player.hashCode());
		result = prime * result + ((_posn == null) ? 0 : _posn.hashCode());
		result = prime * result + _size;
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
		if (!(obj instanceof Piece))
			return false;
		Piece other = (Piece) obj;
		if (_goalPosn == null) {
			if (other._goalPosn != null)
				return false;
		} else if (!_goalPosn.equals(other._goalPosn))
			return false;
		if (_player == null) {
			if (other._player != null)
				return false;
		} else if (!_player.equals(other._player))
			return false;
		if (_posn == null) {
			if (other._posn != null)
				return false;
		} else if (!_posn.equals(other._posn))
			return false;
		if (_size != other._size)
			return false;
		return true;
	}
	
}
