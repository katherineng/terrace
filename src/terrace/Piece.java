package terrace;

public class Piece implements Comparable<Piece> {
	PieceSize size;
	boolean isTPiece;
	Posn posn;
	
	public Piece(PieceSize size, boolean isTPiece, Posn posn) {
		this.size = size;
		this.isTPiece = isTPiece;
		this.posn = posn;
	}

	@Override
	public int compareTo(Piece o) {
		return this.size.compareTo(o.size);
	}
	
}
