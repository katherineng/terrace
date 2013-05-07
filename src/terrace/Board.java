package terrace;

import java.io.PrintWriter;
import java.util.*;
import terrace.util.Posn;
import terrace.util.Copyable;

public abstract class Board implements Copyable<Board> {
	protected Piece[][] _pieces;
	
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
		return _pieces[posn.x][posn.y];
	}
	
	/**
	 * Sets the board at position (x, y) to the given piece
	 * @param posn  The position to set
	 * @param piece The piece to which posn should be set
	 */
	public void setPieceAt(Posn posn, Piece piece) {
		_pieces[posn.getX()][posn.getY()] = piece;
	}
	
	/**
	 * Makes a move
	 * 
	 * @param move The move to make
	 */
	public void makeMove(Move move) {
		Posn from = move.getPiece().getPosn();
		Posn to = move.getTo();
		Piece piece = _pieces[from.getX()][from.getY()];

		assert _pieces[from.getX()][from.getY()] != null;
		_pieces[from.getX()][from.getY()] = null;
		piece.updatePosn(move.getTo());
		_pieces[to.getX()][to.getY()] = piece;
	}
	
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
		
		for (Piece[] row : _pieces) {
			for (Piece p : row) {
				if (p != null) result.add(p);
			}
		}
		return result;
	}
	
	public List<Piece> getPlayerPieces(Player player) {
		List<Piece> toRet = new LinkedList<Piece>();
		for (int y = 0; y < getHeight(); y++) {
			for (int x = 0; x < getWidth(); x++) {
				Piece p = _pieces[x][y];
				if (p != null && p.getPlayer() == player) {
					toRet.add(p);
				}
			}
		}
		return toRet;
	}
	
	public void removePlayer(Player player) {
		for (int y = getHeight() - 1; y >= 0; y--) {
			for (int x = 0; x < getWidth(); x++) {
				Piece p = _pieces[x][y];
				if (p != null && p.getColor().equals(player.getColor())) 
					_pieces[x][y] = null;
			}
		}
	}
	
	public abstract String elevationsToString();
	
	public String piecesToString() {
		String pieces = "";
		
		for (int y = getHeight() - 1; y >= 0; y--) {
			pieces += "[ ";
			for (int x = 0; x < getWidth(); x++) {
				Piece p = _pieces[x][y];
				if (p != null) {
					pieces += p.toString() + "\t";
				} else {
					pieces += "(..........)\t";
				}
			}
			pieces += "]\n";
		}
		return pieces;
	}
	
	public abstract void serialize(PrintWriter out);
	
	protected void serializePieces(PrintWriter out) {
		for (int x = 0; x < getWidth(); x++) {
			for (int y = 0; y < getHeight(); y++) {
				if (_pieces[x][y] == null) {
					out.print("     ");
				} else {
					_pieces[x][y].serialize(out);
				}
			}
			out.println();
		}
	}
}
