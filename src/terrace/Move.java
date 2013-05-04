package terrace;

import terrace.util.Posn;

import com.google.common.base.Optional;

public class Move {
	private final Piece _piece;
	private final Posn _to;
	private final Optional<Piece> _captured;
	
	public Move(Piece piece, Posn to) {
		_piece = piece;
		_to = to;
		_captured = Optional.absent();
	}
	
	public Move(Piece piece, Posn to, Piece captured) {
		_piece = piece;
		_to = to;
		_captured = Optional.fromNullable(captured);
	}
	
	public Piece getPiece() {
		return _piece;
	}
	
	public Posn getTo() {
		return _to;
	}

	public Optional<Piece> getCapturedPiece() {
		return _captured;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null) return false;
		if (getClass() != obj.getClass()) return false;
		Move move = (Move) obj;
		
		return _piece.equals(move._piece) && _to.equals(move._to);
	}
	
	@Override
	public String toString() {
		return
				_piece + ": " +
				_piece.getPosn() + " -> " +
				_to +
				(_captured.isPresent() ? " [captured: " + _captured.get() + "]" : "");
	}
}
