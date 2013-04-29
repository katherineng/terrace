package terrace;

import java.util.List;

public interface Board<T extends Board<T>> extends Cloneable{
	int getWidth();
	int getHeight();
	
	/**
	 * Given a position on the board, returns the positions of the surrounding cells
	 * @param posn A position on the board
	 * @return A list of adjacent positions
	 */
	List<Posn> getNeighbors(Posn posn);
	
	/**
	 * Given a piece on the board, finds all the possible positions to which the piece can move
	 * @param A piece on the board
	 * @return A set of all the positions
	 */
	List<Posn> getMoves(Piece piece);
	
	/**
	 * Returns the piece at the given position
	 * @param posn A position
	 * @return The piece at the position
	 */
	Piece getPieceAt(Posn posn);
	
	/**
	 * Sets the board at position (x, y) to the given piece
	 * @param x The x-coordinate
	 * @param y The y-coordinate
	 * @param piece The piece to which position (x, y) should be set
	 */
	void setPiece(int x, int y, Piece piece);
	
	/**
	 * Updates the board by moving a piece from one position to another
	 * @param from The position from which to move the piece
	 * @param to The position to which the piece is moved
	 * @return The captured piece, if any; if no piece was captured, returns null
	 */
	Piece movePiece(Posn from, Posn to);
	
	/**
	 * Creates a deep copy of the board for an AIPlayer to use to score future states
	 * @return A deep copy of the board
	 */
	T copyBoard();
	
	/**
	 * Given a position on the board, finds its elevation
	 * @param p A position
	 * @return The elevation of the position as an integer
	 */
	int getElevation(Posn p);
	
	/**
	 * Finds all the positions on the same terrace as the given position
	 * @param p A position for which to find all the other positions on the same terrace
	 * @return A list of positions on the same terrace
	 */
	List<Posn> getTerracePosns(Posn p);
	
	
}
