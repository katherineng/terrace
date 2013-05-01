package terrace;

import java.util.List;

import terrace.util.Posn;

public class BoardFactory {
	
	public static Board create(List<Player> players, Variant variant) {
		Board board;
		
		switch(variant) {
		case TRIANGLE:
			board = new TriangleBoard(players.size());
			setUpPieces(players, board);
			break;
		default:
			board = new DefaultBoard(players.size(), variant);
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
				setUpTriangle4P(p1, p2, p3, p4, board);
			}
		} else {
			System.err.println("NO");
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
				Piece tPiece = new Piece(0, true, new Posn(0, 0), dim, p1);
				Piece p2Piece = new Piece(numTerraces - 1, false, new Posn(i, dim - 1), dim, p2);
				p1.addPiece(tPiece);
				p2.addPiece(p2Piece);

				board.setPieceAt(new Posn(i, 0), tPiece);
				board.setPieceAt(new Posn(i, dim - 1), p2Piece);

			} else if (i == dim - 1) {
				Piece tPiece = new Piece(0, true, new Posn(dim - 1, dim - 1), dim, p2);
				Piece p1Piece = new Piece(i / 2, false, new Posn(i, 0), dim, p1);
				p1.addPiece(p1Piece);
				p2.addPiece(tPiece);

				board.setPieceAt(new Posn(i, i), tPiece);
				board.setPieceAt(new Posn(i, 0), p1Piece);
			} else {
				Piece p1Piece = new Piece(i / 2, false, new Posn(i, 0),dim, p1);
				Piece p2Piece = new Piece(numTerraces - (i / 2) - 1, false, new Posn(i, dim - 1), dim, p2);
				p1.addPiece(p1Piece);
				p2.addPiece(p2Piece);

				board.setPieceAt(new Posn(i, 0), p1Piece);
				board.setPieceAt(new Posn(i, dim - 1), p2Piece);
			}

			Piece p1Piece = new Piece(numTerraces - (i / 2) - 1, false, new Posn(i, 1), dim, p1);
			Piece p2Piece = new Piece(i / 2, false, new Posn(i, dim  - 2), dim, p2);
			p1.addPiece(p1Piece);
			p2.addPiece(p2Piece);

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
				Piece t1Piece = new Piece(0, true, new Posn(1, 0), dim, p1);
				Piece t2Piece = new Piece(0, true, new Posn(0, 1), dim, p2);
				Piece p3Piece = new Piece(numTerraces - (i / 2) - 1, false, new Posn(i, dim -1), dim, p3);
				Piece p4Piece = new Piece(numTerraces - (i / 2) - 1, false, new Posn(dim -1, i), dim, p4);
				p1.addPiece(t1Piece);
				p2.addPiece(t2Piece);
				p3.addPiece(p3Piece);
				p4.addPiece(p4Piece);

				board.setPieceAt(new Posn(i, 0), t1Piece);
				board.setPieceAt(new Posn(i, dim - 1), p3Piece);

				board.setPieceAt(new Posn(0, i), t2Piece);
				board.setPieceAt(new Posn(dim - 1, i), p4Piece);

			} else if(i == dim - 2) {
				Piece p1Piece = new Piece(i / 2, false, new Posn(i, 0), dim, p1);
				Piece p2Piece = new Piece(i / 2, false, new Posn(0, i), dim, p2);
				Piece t3Piece = new Piece(0, true, new Posn(i, dim - 1), dim, p3);
				Piece t4Piece = new Piece(0, true, new Posn(dim - 1, i), dim, p4);
				p1.addPiece(p1Piece);
				p2.addPiece(p2Piece);
				p3.addPiece(t3Piece);
				p4.addPiece(t4Piece);

				board.setPieceAt(new Posn(i, 0), p1Piece);
				board.setPieceAt(new Posn(i, dim - 1), t3Piece);

				board.setPieceAt(new Posn(0, i), p2Piece);
				board.setPieceAt(new Posn(dim - 1, i), t4Piece);
			} else {
				Piece p1Piece = new Piece(i / 2, false, new Posn(i, 0), dim, p1);
				Piece p2Piece = new Piece(i / 2, false, new Posn(0, i), dim, p2);
				Piece p3Piece = new Piece(numTerraces - (i / 2) - 1, false, new Posn(i, dim -1), dim, p3);
				Piece p4Piece = new Piece(numTerraces - (i / 2) - 1, false, new Posn(dim -1, i), dim, p4);
				p1.addPiece(p1Piece);
				p2.addPiece(p2Piece);
				p3.addPiece(p3Piece);
				p4.addPiece(p4Piece);

				board.setPieceAt(new Posn(i, 0), p1Piece);
				board.setPieceAt(new Posn(i, dim - 1), p3Piece);

				board.setPieceAt(new Posn(0, i), p2Piece);
				board.setPieceAt(new Posn(dim - 1, i), p4Piece);
			}
		}
	}
	
	private static void setUpTriangle2P(Player p1, Player p2, Board board) {
		
	}
	
	private static void setUpTriangle4P(Player p1, Player p2, Player p3, Player p4, Board board) {
		
	}
}
