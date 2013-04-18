package terrace;

public class Piece implements Comparable<Piece> {
	private final PieceSize _size;
	private final boolean _isTPiece;
	private Posn _posn;
	private Player _player;
	
	public Piece(PieceSize size, boolean isTPiece, Posn posn, Player player) {
		_size = size;
		_isTPiece = isTPiece;
		_posn = posn;
		_player = player;
	}
	
	public void updatePosn(Posn posn) {
		_posn = posn;
	}
	
	public PieceSize getSize() {
		return _size;
	}
	
	public Posn getPosn() {
		return _posn;
	}
	
	public boolean isTPiece() {
		return _isTPiece;
	}
	
	public Player getPlayer() {
		return _player;
	}

	@Override
	public int compareTo(Piece o) {
		return _size.compareTo(o.getSize());
	}
	
	@Override
	public int hashCode() {
		return _posn.hashCode();
	}
	
	@Override
	public boolean equals(Object o) {
		if (this.getClass() == o.getClass()) {
			Piece p = (Piece) o;
			return _size == p.getSize() && _isTPiece == p.isTPiece() && _posn.equals(p.getPosn());
		}
		return false;
	}
	
}
