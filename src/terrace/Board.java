package terrace;

import java.util.List;

public abstract class Board implements Cloneable {
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
	public abstract Piece getPieceAt(Posn posn);
	
	/**
	 * Sets the board at position (x, y) to the given piece
	 * @param x The x-coordinate
	 * @param y The y-coordinate
	 * @param piece The piece to which position (x, y) should be set
	 */
	public abstract void setPiece(int x, int y, Piece piece);
	
	/**
	 * Makes a move
	 * 
	 * @param move The move to make
	 */
	public abstract void makeMove(Move move);
	
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
}
