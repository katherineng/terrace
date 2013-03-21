package terrace;

public class Move {
	private final Piece piece;
	private final Posn to;
	
	public Move(Piece piece, Posn to) {
		this.piece = piece;
		this.to = to;
	}
}
