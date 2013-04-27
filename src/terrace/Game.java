package terrace;

import java.util.*;
import terrace.exception.IllegalMoveException;
import com.google.common.base.*;

public class Game {
	private DefaultBoard _board;
	private int _dimensions;
	private int _currPlayer;
	private List<Player> _players;
	private final int _numPlayers;
	private int _playersAlive;		/** Number of players that are currently still playing **/
	private boolean _isGameOver;
	private Optional<Player> _winner;
	// private Variant _variant;
	
	public Game(int numPlayers, int dimensions, Variant variant) {
		_numPlayers = numPlayers;
		// _variant = variant;
		_dimensions = dimensions;
		_board = new DefaultBoard(dimensions, variant);
		_board.setUp();
		_isGameOver = false;
		_winner = Optional.absent();
		
		_players = new ArrayList<Player>();
		for (int i = 0; i < numPlayers; i++) {
			_players.add(new Player(PlayerColor.values()[i]));
		}		
		
		_playersAlive = _numPlayers;
		_currPlayer = 0;
		
		setUpPieces();
	}
	
	public DefaultBoard getBoard() {
		return _board;
	}
	
	public Player getCurrentPlayer() {
		return _players.get(_currPlayer);
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
	
	public boolean isGameOver() {
		return _isGameOver;
	}
	
	public Player getWinner() {
		return _winner.orNull();
	}
	
	private void changeTurn() {
		if (_currPlayer < _numPlayers - 1) _currPlayer++;
		else _currPlayer = 0;
	}
	
	
	/**
	 * Attempts to make a move from a given position to a given position and returns the captured piece, if any
	 * @param from The position from which to move
	 * @param to The destination position
	 * @return The captured piece, if any; otherwise null
	 * @throws IllegalMoveException When a move from the given position to the given destination position is not legal
	 */
	public Piece movePiece(Posn from, Posn to) throws IllegalMoveException {
		Piece playerPiece = _board.getPieceAt(from);
		if (playerPiece == null || !getCurrentPlayer().getPieces().contains(playerPiece)) {
			throw new IllegalMoveException("ERROR: " + _currPlayer + "\'s piece not found at " + from.toString());
		} else {
			 Set<Posn> possibleMoves = _board.getMoves(playerPiece);
			 
			 if (!possibleMoves.contains(to)) {
				 throw new IllegalMoveException("ERROR: Piece at " + from.toString() + " can't be moved to " + to.toString());
			 } else {
				 if (playerPiece.isTPiece() && playerPiece.getGoalPosn().equals(to)) {
					 _winner = Optional.of(_players.get(_currPlayer));
				 }
				 
				 getCurrentPlayer().getPieces().remove(playerPiece);
				 _board.setPiece(from.x, from.y, null);
				 
				 Piece captured = _board.getPieceAt(to);
				 if (captured != null) {
					 if (captured.isTPiece()) {
						 _players.remove(captured.getPlayer());
						 _playersAlive--;
						 _currPlayer = (_currPlayer == 0) ?  _playersAlive-1 : _currPlayer-1;
						 if (_players.size() == 1) {
							 _winner = Optional.of(_players.get(_currPlayer));
							 _isGameOver = true;
						 }
					 }
					 
					 captured.getPlayer().getPieces().remove(captured);
				 }
					 
 				 playerPiece.setPosn(to);
 				 getCurrentPlayer().getPieces().add(playerPiece);
				 _board.setPiece(to.x, to.y, playerPiece);
				 
				 changeTurn();
				 
				 return captured;
			 }
		}
	}
	
	/**
	 * Sets up pieces on the board
	 */
	protected void setUpPieces() {
		
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
		int numTerraces = _dimensions / 2;
		
		for (int i = 0; i < _dimensions; i++) {
			
			if (i == 0) {
				Piece tPiece = new Piece(0, true, new Posn(0, 0), _dimensions, p1);
				Piece p2Piece = new Piece(numTerraces - 1, false, new Posn(i, _dimensions - 1), _dimensions, p2);
				p1.addPiece(tPiece);
				p2.addPiece(p2Piece);
				
				_board.setPiece(i, 0, tPiece);
				_board.setPiece(i, _dimensions - 1, p2Piece);
			
			} else if (i == _dimensions - 1) {
				Piece tPiece = new Piece(0, true, new Posn(_dimensions - 1, _dimensions - 1), _dimensions, p2);
				Piece p1Piece = new Piece(i / 2, false, new Posn(i, 0), _dimensions, p1);
				p1.addPiece(p1Piece);
				p2.addPiece(tPiece);
				
				_board.setPiece(i, i, tPiece);
				_board.setPiece(i, 0, p1Piece);
			} else {
				Piece p1Piece = new Piece(i / 2, false, new Posn(i, 0), _dimensions, p1);
				Piece p2Piece = new Piece(numTerraces - (i / 2) - 1, false, new Posn(i, _dimensions - 1), _dimensions, p2);
				p1.addPiece(p1Piece);
				p2.addPiece(p2Piece);
				
				_board.setPiece(i, 0, p1Piece);
				_board.setPiece(i, _dimensions - 1, p2Piece);
			}
			
			Piece p1Piece = new Piece(numTerraces - (i / 2) - 1, false, new Posn(i, 1), _dimensions, p1);
			Piece p2Piece = new Piece(i / 2, false, new Posn(i, _dimensions  - 2), _dimensions, p2);
			p1.addPiece(p1Piece);
			p2.addPiece(p2Piece);
			
			_board.setPiece(i, 1, p1Piece);
			_board.setPiece(i, _dimensions - 2, p2Piece);
			
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
		int dimensions = _board.getWidth();
		int numTerraces = dimensions / 2;
		
		for (int i = 1; i < dimensions - 1; i++) {
			if (i == 1) {
				Piece t1Piece = new Piece(0, true, new Posn(1, 0), _dimensions, p1);
				Piece t2Piece = new Piece(0, true, new Posn(0, 1), _dimensions, p2);
				Piece p3Piece = new Piece(numTerraces - (i / 2) - 1, false, new Posn(i, dimensions -1), _dimensions, p3);
				Piece p4Piece = new Piece(numTerraces - (i / 2) - 1, false, new Posn(dimensions -1, i), _dimensions, p4);
				p1.addPiece(t1Piece);
				p2.addPiece(t2Piece);
				p3.addPiece(p3Piece);
				p4.addPiece(p4Piece);
				
				_board.setPiece(i, 0, t1Piece);
				_board.setPiece(i, dimensions - 1, p3Piece);
				
				_board.setPiece(0, i, t2Piece);
				_board.setPiece(dimensions - 1, i, p4Piece);
				
			} else if(i == dimensions - 2) {
				Piece p1Piece = new Piece(i / 2, false, new Posn(i, 0), _dimensions, p1);
				Piece p2Piece = new Piece(i / 2, false, new Posn(0, i), _dimensions, p2);
				Piece t3Piece = new Piece(0, true, new Posn(i, dimensions - 1), _dimensions, p3);
				Piece t4Piece = new Piece(0, true, new Posn(dimensions - 1, i), _dimensions, p4);
				p1.addPiece(p1Piece);
				p2.addPiece(p2Piece);
				p3.addPiece(t3Piece);
				p4.addPiece(t4Piece);
				
				_board.setPiece(i, 0, p1Piece);
				_board.setPiece(i, dimensions - 1, t3Piece);
				
				_board.setPiece(0, i, p2Piece);
				_board.setPiece(dimensions - 1, i, t4Piece);
			} else {
				Piece p1Piece = new Piece(i / 2, false, new Posn(i, 0), _dimensions, p1);
				Piece p2Piece = new Piece(i / 2, false, new Posn(0, i), _dimensions, p2);
				Piece p3Piece = new Piece(numTerraces - (i / 2) - 1, false, new Posn(i, dimensions -1), _dimensions, p3);
				Piece p4Piece = new Piece(numTerraces - (i / 2) - 1, false, new Posn(dimensions -1, i), _dimensions, p4);
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
