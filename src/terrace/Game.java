package terrace;

import java.util.*;

public class Game {
	private DefaultBoard _board;
	private Player _currPlayer;
	private List<Player> _players;
	private final int _numPlayers;
	private boolean _isGameOver;
	
	public Game(int numPlayers) {
		_numPlayers = numPlayers;
		_board = new DefaultBoard();
		_isGameOver = false;
		
		_players = new ArrayList<Player>();
		for (int i = 0; i < numPlayers; i++) {
			_players.add(new Player());
		}
		
		setUpPieces();
	}
	
	public DefaultBoard getBoard() {
		return _board;
	}
	
	public Player getCurrentPlayer() {
		return _currPlayer;
	}
	
	/**
	 * @return All the players in the game
	 */
	public List<Player> getPlayers() {
		return _players;
	}
	
	/**
	 * @param p A player
	 * @return  All the live pieces owned by that player
	 */
	public Set<Piece> getPiecesOf(Player p) {
		return p.getPieces();
	}
	
	/**
	 * Sets up pieces on the board
	 */
	private void setUpPieces() {
		
		if (_numPlayers == 2) {
			Player p1 = _players.get(0);
			Player p2 = _players.get(1);
						
			setUp2Player(p1, p2);
			
		} else if (_numPlayers == 4) {
			Player p1 = _players.get(0);
			Player p2 = _players.get(1);
			Player p3 = _players.get(2);
			Player p4 = _players.get(3);
			
			setUp4Player(p1, p2, p3, p4);
		} else {
			System.err.println("NO");
		}
	}
	
	/**
	 * Places the pieces on the board for a 2 player game
	 * @param p1 Player 1
	 * @param p2 Player 2
	 */
	private void setUp2Player(Player p1, Player p2) {
		PieceSize[] sizes = PieceSize.values();
		int dimensions = _board.getWidth();
		
		for (int i = 0; i < dimensions; i++) {
			
			if (i == 0) {
				Piece tPiece = new Piece(PieceSize.SMALL, true, new Posn(0, 0), p1);
				Piece p2Piece = new Piece(sizes[sizes.length - (i / 2) - 1], false, new Posn(i, dimensions - 1), p2);
				p1.addPiece(tPiece);
				p2.addPiece(p2Piece);
				
				_board.setPiece(i, 0, tPiece);
				_board.setPiece(i, dimensions - 1, p2Piece);
			
			} else if (i == dimensions - 1) {
				Piece tPiece = new Piece(PieceSize.SMALL, true, new Posn(dimensions - 1, dimensions - 1), p2);
				Piece p1Piece = new Piece(sizes[i / 2], false, new Posn(i, 0), p1);
				p1.addPiece(p1Piece);
				p2.addPiece(tPiece);
				
				_board.setPiece(i, i, tPiece);
				_board.setPiece(i, 0, p1Piece);
			} else {
				Piece p1Piece = new Piece(sizes[i / 2], false, new Posn(i, 0), p1);
				Piece p2Piece = new Piece(sizes[i / 2], false, new Posn(i, dimensions - 1), p2);
				p1.addPiece(p1Piece);
				p2.addPiece(p2Piece);
				
				_board.setPiece(i, 0, p1Piece);
				_board.setPiece(i, dimensions - 1, p2Piece);
			}
			
			Piece p1Piece = new Piece(sizes[sizes.length - (i / 2) - 1], false, new Posn(i, 1), p1);
			Piece p2Piece = new Piece(sizes[sizes.length - (i / 2) - 1], false, new Posn(i, dimensions  - 2), p2);
			p1.addPiece(p1Piece);
			p2.addPiece(p2Piece);
			
			_board.setPiece(i, 1, p1Piece);
			_board.setPiece(i, dimensions - 2, p2Piece);
			
		}
	}
	
	/**
	 * Sets up the pieces on the board for a 4-player game
	 * @param p1 Player 1
	 * @param p2 Player 2
	 * @param p3 Player 3
	 * @param p4 Player 4
	 */
	private void setUp4Player(Player p1, Player p2, Player p3, Player p4) {
		PieceSize[] sizes = PieceSize.values();
		int dimensions = _board.getWidth();
		
		for (int i = 1; i < dimensions - 1; i++) {
			if (i == 1) {
				Piece t1Piece = new Piece(PieceSize.SMALL, true, new Posn(1, 0), p1);
				Piece t2Piece = new Piece(PieceSize.SMALL, true, new Posn(0, 1), p2);
				Piece p3Piece = new Piece(sizes[sizes.length - (i / 2) - 1], false, new Posn(i, dimensions -1), p3);
				Piece p4Piece = new Piece(sizes[sizes.length - (i / 2) - 1], false, new Posn(dimensions -1, i), p4);
				p1.addPiece(t1Piece);
				p2.addPiece(t2Piece);
				p3.addPiece(p3Piece);
				p4.addPiece(p4Piece);
				
				_board.setPiece(i, 0, t1Piece);
				_board.setPiece(i, dimensions - 1, p3Piece);
				
				_board.setPiece(0, i, t2Piece);
				_board.setPiece(dimensions - 1, i, p4Piece);
				
			} else if(i == dimensions - 2) {
				Piece p1Piece = new Piece(sizes[i / 2], false, new Posn(i, 0), p1);
				Piece p2Piece = new Piece(sizes[i / 2], false, new Posn(0, i), p2);
				Piece t3Piece = new Piece(PieceSize.SMALL, true, new Posn(i, dimensions - 1), p3);
				Piece t4Piece = new Piece(PieceSize.SMALL, true, new Posn(dimensions - 1, i), p4);
				p1.addPiece(p1Piece);
				p2.addPiece(p2Piece);
				p3.addPiece(t3Piece);
				p4.addPiece(t4Piece);
				
				_board.setPiece(i, 0, p1Piece);
				_board.setPiece(i, dimensions - 1, t3Piece);
				
				_board.setPiece(0, i, p2Piece);
				_board.setPiece(dimensions - 1, i, t4Piece);
			} else {
				Piece p1Piece = new Piece(sizes[i / 2], false, new Posn(i, 0), p1);
				Piece p2Piece = new Piece(sizes[i / 2], false, new Posn(0, i), p2);
				Piece p3Piece = new Piece(sizes[sizes.length - (i / 2) - 1], false, new Posn(i, dimensions -1), p3);
				Piece p4Piece = new Piece(sizes[sizes.length - (i / 2) - 1], false, new Posn(dimensions -1, i), p4);
				p1.addPiece(p1Piece);
				p2.addPiece(p2Piece);
				p3.addPiece(p3Piece);
				p4.addPiece(p4Piece);
				
				_board.setPiece(i, 0, p1Piece);
				_board.setPiece(i, dimensions - 1, p3Piece);
			
				_board.setPiece(0, i, p2Piece);
				_board.setPiece(dimensions - 1, i, p4Piece);
			}
		}
	}
}
