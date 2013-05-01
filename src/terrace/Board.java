package terrace;

import java.util.LinkedList;
import java.util.List;
import terrace.util.Posn;

public abstract class Board{

	protected Piece[][] _board;
	
	public abstract int getWidth();
	public abstract int getHeight();
	
	/**
	 * Given a position on the board, returns the positions of the surrounding cells
	 * @param posn A position on the board
	 * @return A list of adjacent positions
	 */
	public abstract List<Posn> getNeighbors(Posn posn);
	
	/**
	 * Given a piece on the board, finds all the possible Moves to which the piece can make
	 * @param A piece on the board
	 * @return A set of all the Moves
	 */
	public abstract List<Move> getMoves(Piece piece);
	
	/**
	 * Returns the piece at the given position
	 * @param posn A position
	 * @return The piece at the position
	 */
	public Piece getPieceAt(Posn posn) {
		return _board[posn.x][posn.y];
	}
	
	/**
	 * Sets the board at position (x, y) to the given piece
	 * @param posn  The position to set
	 * @param piece The piece to which posn should be set
	 */
	public void setPieceAt(Posn posn, Piece piece) {
		_board[posn.getX()][posn.getY()] = piece;
	}
	
	/**
	 * Makes a move
	 * 
	 * @param move The move to make
	 */
	public void makeMove(Move move) {
		Posn from = move.getPiece().getPosn();
		Posn to = move.getTo();
		
		_board[from.getX()][from.getY()] = null;
		move.getPiece().updatePosn(move.getTo());
		_board[to.getX()][to.getY()] = move.getPiece();
	}
	
	/**
	 * Creates a deep copy of the board for an AIPlayer to use to score future states
	 * @return A deep copy of the board
	 */
	public abstract Board copyBoard();
	
	/**
	 * Given a position on the board, finds its elevation
	 * @param p A position
	 * @return The elevation of the position as an integer
	 */
	public abstract int getElevation(Posn p);
	
	/**
	 * @return All the pieces on the board
	 */
	public List<Piece> getPieces() {
		List<Piece> result = new LinkedList<>();
		
		for (Piece[] row : _board) {
			for (Piece p : row) {
				if (p != null) result.add(p);
			}
		}
		return result;
	}
}
