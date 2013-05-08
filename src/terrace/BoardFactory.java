package terrace;

import java.io.BufferedReader;
import java.io.EOFException;
import java.io.IOException;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import terrace.util.Posn;

public class BoardFactory {
	private static final Pattern piece = Pattern.compile("\\(([0-4])\\:([T0-9])\\)");
	private static final Pattern standardBoard = Pattern.compile("([A-Z]+) ([0-9]+)");
	
	public static Board read(BufferedReader in, List<Player> players) throws IOException {
		String typeLine = in.readLine();
		
		if (typeLine == null) throw new EOFException("Server connection closed");
		
		Matcher m = standardBoard.matcher(typeLine); 
		
		if (!m.matches()) throw new IOException("Server sent bad type line.");
		
		try {
			Variant v = Variant.valueOf(m.group(1));
			Board board;
			
			switch(v) {
			case TRIANGLE:
				board = new TriangleBoard(Integer.parseInt(m.group(2)));
				break;
			default:
				board = new DefaultBoard(Integer.parseInt(m.group(2)), v);
				break;
			}
			readPieces(in, players, board);
			return board;
		} catch (IllegalArgumentException e) {
			throw new IOException("Server sent bad board type");
		}
	}
	
	private static void readPieces(BufferedReader in, List<Player> players, Board board) throws IOException {
		for (int y = 0; y < board.getHeight(); y++) {
			String row = in.readLine();
			
			if (row == null) throw new EOFException("Server connection closed");
			
			String[] cells = row.split("\\.");
			if (cells.length != board.getWidth()) {
				throw new IOException("Server sent " + cells.length + " columns but expected " + board.getWidth());
			}
			
			for (int x = 0; x < board.getWidth(); x++) {
				Posn p = new Posn(x, y);
				board.setPieceAt(p, readPiece(p, cells[x], players));
			}
		}
	}
	
	private static Piece readPiece(Posn p, String cell, List<Player> players) throws IOException {
		if (cell.equals("     ")) {
			return null;
		} else {
			Matcher m = piece.matcher(cell);
			if (!m.matches()) throw new IOException("Server sent bad piece");
			
			int player = Integer.parseInt(m.group(1));
			if (player >= players.size()) throw new IOException("Server sent bad player number");
			
			if (m.group(2).equals("T")) {
				// We don't know the goal but that is ok because the server knows.
				return new TPiece(p, players.get(player), null);
			} else {
				return new Piece(Integer.parseInt(m.group(2)), p, players.get(player));
			}
		}
	}
	
	public static Board create(List<Player> players, int size, Variant variant) {
		Board board;
		
		switch(variant) {
		case TRIANGLE:
			board = new TriangleBoard(size);
			setUpPieces(players, board);
			break;
		default:
			board = new DefaultBoard(size, variant);
			setUpPieces(players, board);
			break;
		}
		return board;
	}
	
	/**
	 * Sets up pieces on the board
	 */
	static void setUpPieces(List<Player> players, Board board) {
		int numPlayers = players.size();

		if (numPlayers == 2) {
			Player p1 = players.get(0);
			Player p2 = players.get(1);

			if (board instanceof DefaultBoard) {
				setUpDefault2P(p1, p2, board);
			} else {
				setUpTriangle2P(p1, p2, board);
			}

		} else if (numPlayers == 4) {
			Player p1 = players.get(0);
			Player p2 = players.get(1);
			Player p3 = players.get(2);
			Player p4 = players.get(3);

			if (board instanceof DefaultBoard) {
				setUpDefault4P(p1, p2, p3, p4, board);
			} else {
				System.err.println("ERROR: Triangle board can't have 4 players");
			}
		} else {
			System.err.println("ERROR: Bad number of players");
		}
	}

	/**
	 * Places the pieces on the board for a 2 player game
	 * @param p1 Player 1
	 * @param p2 Player 2
	 */
	private static void setUpDefault2P(Player p1, Player p2, Board board) {
		int dim = board.getWidth();
		int numTerraces = dim / 2;
		
		for (int i = 0; i < dim; i++) {
			if (i == 0) {
				Piece tPiece = new TPiece(new Posn(0,0), p1, new Posn(dim - 1, dim - 1));
				Piece p2Piece = new Piece(numTerraces - 1, new Posn(i, dim - 1), p2);
				
				board.setPieceAt(new Posn(i, 0), tPiece);
				board.setPieceAt(new Posn(i, dim - 1), p2Piece);
			} else if (i == dim - 1) {
				Piece tPiece = new TPiece(new Posn(dim - 1, dim - 1), p2, new Posn(0, 0));
				Piece p1Piece = new Piece(i / 2, new Posn(i, 0), p1);
				
				board.setPieceAt(new Posn(i, i), tPiece);
				board.setPieceAt(new Posn(i, 0), p1Piece);
			} else {
				Piece p1Piece = new Piece(i / 2, new Posn(i, 0), p1);
				Piece p2Piece = new Piece(numTerraces - (i / 2) - 1, new Posn(i, dim - 1), p2);
				
				board.setPieceAt(new Posn(i, 0), p1Piece);
				board.setPieceAt(new Posn(i, dim - 1), p2Piece);
			}
			
			Piece p1Piece = new Piece(numTerraces - (i / 2) - 1, new Posn(i, 1), p1);
			Piece p2Piece = new Piece(i / 2, new Posn(i, dim  - 2), p2);
			
			board.setPieceAt(new Posn(i, 1), p1Piece);
			board.setPieceAt(new Posn(i, dim - 2), p2Piece);
		}
	}
	
	/**
	 * Sets up the pieces on the board for a 4-player game
	 * @param p1 Player 1
	 * @param p2 Player 2
	 * @param p3 Player 3
	 * @param p4 Player 4
	 */
	private static void setUpDefault4P(Player p1, Player p2, Player p3, Player p4, Board board) {
		int dim = board.getWidth();
		int numTerraces = dim / 2;
		
		for (int i = 1; i < dim - 1; i++) {
			if (i == 1) {
				Piece t1Piece = new TPiece(new Posn(1, 0), p1, new Posn(dim - 1, dim - 1));
				Piece t2Piece = new TPiece(new Posn(0, 1), p2, new Posn(dim - 1, dim - 1));
				Piece p3Piece = new Piece(numTerraces - (i / 2) - 1, new Posn(i, dim -1), p3);
				Piece p4Piece = new Piece(numTerraces - (i / 2) - 1, new Posn(dim -1, i), p4);
				
				board.setPieceAt(new Posn(i, 0), t1Piece);
				board.setPieceAt(new Posn(i, dim - 1), p3Piece);
				
				board.setPieceAt(new Posn(0, i), t2Piece);
				board.setPieceAt(new Posn(dim - 1, i), p4Piece);
			} else if(i == dim - 2) {
				Piece p1Piece = new Piece(i / 2, new Posn(i, 0), p1);
				Piece p2Piece = new Piece(i / 2, new Posn(0, i), p2);
				Piece t3Piece = new TPiece(new Posn(i, dim - 1), p3, new Posn(0, 0));
				Piece t4Piece = new TPiece(new Posn(dim - 1, i), p4, new Posn(0, 0));
				
				board.setPieceAt(new Posn(i, 0), p1Piece);
				board.setPieceAt(new Posn(i, dim - 1), t3Piece);
				
				board.setPieceAt(new Posn(0, i), p2Piece);
				board.setPieceAt(new Posn(dim - 1, i), t4Piece);
			} else {
				Piece p1Piece = new Piece(i / 2, new Posn(i, 0), p1);
				Piece p2Piece = new Piece(i / 2, new Posn(0, i), p2);
				Piece p3Piece = new Piece(numTerraces - (i / 2) - 1, new Posn(i, dim -1), p3);
				Piece p4Piece = new Piece(numTerraces - (i / 2) - 1, new Posn(dim -1, i), p4);
				
				board.setPieceAt(new Posn(i, 0), p1Piece);
				board.setPieceAt(new Posn(i, dim - 1), p3Piece);
				
				board.setPieceAt(new Posn(0, i), p2Piece);
				board.setPieceAt(new Posn(dim - 1, i), p4Piece);
			}
		}
	}
	
	private static void setUpTriangle2P(Player p1, Player p2, Board board) {
		for (int i = 0; i < board.getWidth(); i++) {
			Piece p1PieceEdge, p2PieceEdge;
			
			if (i == 0) {
				p1PieceEdge = new TPiece(new Posn(i, 0), p1, new Posn(board.getWidth() - 1, board.getHeight() - 1));
				p2PieceEdge = new Piece(board.getWidth() - i - 1, new Posn(i, board.getHeight() - 1), p2);
			} else if (i == board.getWidth() - 1) {
				p1PieceEdge = new Piece(i, new Posn(i, 0), p1);
				p2PieceEdge = new TPiece(new Posn(i, board.getHeight() - 1), p2, new Posn(0, 0));
			} else {
				p1PieceEdge = new Piece(i, new Posn(i, 0), p1);
				p2PieceEdge = new Piece(board.getWidth() - i - 1, new Posn(i, board.getHeight() - 1), p2);
			}
			
			Piece p1Piece = new Piece(board.getWidth() - i - 1, new Posn(i, 1), p1);
			Piece p2Piece = new Piece(i, new Posn(i, board.getHeight() - 2), p2);
			
			board.setPieceAt(new Posn(i, 0), p1PieceEdge);
			board.setPieceAt(new Posn(i, 1), p1Piece);
			board.setPieceAt(new Posn(i, board.getHeight() - 1), p2PieceEdge);
			board.setPieceAt(new Posn(i, board.getHeight() - 2), p2Piece);
		}
	}
}
