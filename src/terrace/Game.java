package terrace;

import java.util.*;
import terrace.ai.AI;
import terrace.exception.IllegalMoveException;
import com.google.common.base.*;

public class Game implements Cloneable {
	private DefaultBoard _board;
	private int _currPlayer;
	private List<Player> _players;
	private final int _numPlayers;
	private final int _numHuman; // need for cloning
	private final int _numAI;	// need for cloning
	private int _playersAlive;		/** Number of players that are currently still playing **/
	private boolean _isGameOver;
	private Optional<Player> _winner;
	// private Variant _variant;
	
	public Game(int numHuman, int numAI, int dimensions, Variant variant) throws IllegalMoveException {
		_numPlayers = numHuman + numAI;
		_numHuman = numHuman;
		_numAI = numAI;
		assert(_numPlayers == 2 || _numPlayers == 4);
		// _variant = variant;
		_board = new DefaultBoard(dimensions, variant);
		_board.setUp();
		_isGameOver = false;
		_winner = Optional.absent();
		
		_players = new ArrayList<Player>();
		for (int i = 0; i < numHuman; i++) 
			_players.add(new Player(PlayerColor.values()[i]));
		for (int i = numHuman; i < _numPlayers; i++)
			_players.add(new AI(PlayerColor.values()[i], this));
		
		_playersAlive = _numPlayers;
		_currPlayer = 0;
		
		setUpPieces();
		_players.get(_currPlayer).makeMove();
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
	public List<Piece> getPiecesOf(Player p) {
		return p.getPieces();
	}
	
	public boolean isGameOver() {
		return _isGameOver;
	}
	
	public Player getWinner() {
		return _winner.orNull();
	}
	
	private void changeTurn() throws IllegalMoveException {
		if (_currPlayer < _playersAlive - 1) _currPlayer++;
		else _currPlayer = 0;
		if (!_isGameOver) {
//			if (noPossibleMoves()){
//				 _players.remove(_players.get(_currPlayer));
//				 _playersAlive--;
//				 _currPlayer = (_currPlayer == 0) ?  _playersAlive-1 : _currPlayer-1;
//				 if (_players.size() == 1) setWinner(_players.get(_currPlayer));
//			}
			_players.get(_currPlayer).makeMove();
		}
	}
	
	protected boolean noPossibleMoves(){
		return true;
	}
	
	
	/**
	 * Attempts to make a move from a given position to a given position and returns the captured piece, if any
	 * @param from The position from which to move
	 * @param to The destination position
	 * @return The captured piece, if any; otherwise null
	 * @throws IllegalMoveException When a move from the given position to the given destination position is not legal
	 */
	public Piece movePiece(Posn from, Posn to) throws IllegalMoveException {
		Player current = getCurrentPlayer();
		Piece playerPiece = _board.getPieceAt(from);
		if (playerPiece == null || !current.getPieces().contains(playerPiece)) {
			throw new IllegalMoveException("ERROR: " + _currPlayer + "\'s piece not found at " + from.toString());
		} else {
			 List<Move> possibleMoves = _board.getMoves(playerPiece);
			 
			 if (!possibleMoves.contains(new Move(playerPiece, to))) {
				 throw new IllegalMoveException("ERROR: Piece at " + from.toString() + " can't be moved to " + to.toString());
			 } else {
				 Posn goal = playerPiece.getGoalPosn().orNull();
				 if (playerPiece.isTPiece()){
					 System.out.println(goal);
					 System.out.println(to);
				 }
				 if (playerPiece.isTPiece() && goal != null && goal.equals(to))
					 setWinner(current);
				 

				 current.getPieces().remove(playerPiece);
				 _board.setPiece(from.x, from.y, null);
				 
				 Piece captured = _board.getPieceAt(to);
				 if (captured != null) {
					 if (captured.isTPiece()) {
						 _players.remove(captured.getPlayer());
						 _playersAlive--;
						 if (_players.size() == 1) 
							 setWinner(current);
						 else 
							 removePlayerPieces(captured.getPlayer());
					 } else  captured.getPlayer().getPieces().remove(captured);
				 }
				
				 if (!(captured != null && captured.isTPiece() && captured.getPlayer().equals(playerPiece.getPlayer()))){
					 playerPiece.setPosn(to);
					 current.getPieces().add(playerPiece);
					 _board.setPiece(to.x, to.y, playerPiece);
				 }
				 
				 if (captured != null && captured.isTPiece() && _currPlayer < getPlayerNumber(captured.getPlayer())) 
					 _currPlayer--;
				 changeTurn();
				 
				 return captured;
			 }
		}
	}
	
	/**
	 * Gets the number associated with a plyaer
	 * @param player - a Player
	 * @return - <player>'s index in _players
	 */
	protected int getPlayerNumber(Player player){
		for (int i = 0; i < _players.size(); i++)
			if (_players.get(i).equals(player)) return i;
		return -1;
	}
	
	/**
	 * Removes all the pieces of a certain player
	 * @param p - a Player whose pieces you want to remove
	 */
	protected void removePlayerPieces(Player p){
		getBoard().removePlayer(p);
		p.getPieces().clear();
	}
	
	/**
	 * Sets the winner	
	 * @param winner - the player that is the winner
	 */
	protected void setWinner(Player winner){
		 _winner = Optional.of(winner);
		 _isGameOver = true;		
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
	
	public int getDimensions(){
		return _board.getDimensions();
	}
	
	/**
	 * Places the pieces on the board for a 2 player game
	 * @param p1 Player 1
	 * @param p2 Player 2
	 */
	private void setUp2Player(Player p1, Player p2) {
		int dim = getDimensions();
		int numTerraces = dim / 2;
		
		for (int i = 0; i < dim; i++) {
			
			if (i == 0) {
				Piece tPiece = new Piece(0, true, new Posn(0, 0), dim, p1);
				Piece p2Piece = new Piece(numTerraces - 1, false, new Posn(i, dim - 1), dim, p2);
				p1.addPiece(tPiece);
				p2.addPiece(p2Piece);
				
				_board.setPiece(i, 0, tPiece);
				_board.setPiece(i, dim - 1, p2Piece);
			
			} else if (i == dim - 1) {
				Piece tPiece = new Piece(0, true, new Posn(dim - 1, dim - 1), dim, p2);
				Piece p1Piece = new Piece(i / 2, false, new Posn(i, 0), dim, p1);
				p1.addPiece(p1Piece);
				p2.addPiece(tPiece);
				
				_board.setPiece(i, i, tPiece);
				_board.setPiece(i, 0, p1Piece);
			} else {
				Piece p1Piece = new Piece(i / 2, false, new Posn(i, 0),dim, p1);
				Piece p2Piece = new Piece(numTerraces - (i / 2) - 1, false, new Posn(i, dim - 1), dim, p2);
				p1.addPiece(p1Piece);
				p2.addPiece(p2Piece);
				
				_board.setPiece(i, 0, p1Piece);
				_board.setPiece(i, dim - 1, p2Piece);
			}
			
			Piece p1Piece = new Piece(numTerraces - (i / 2) - 1, false, new Posn(i, 1), dim, p1);
			Piece p2Piece = new Piece(i / 2, false, new Posn(i, dim  - 2), dim, p2);
			p1.addPiece(p1Piece);
			p2.addPiece(p2Piece);
			
			_board.setPiece(i, 1, p1Piece);
			_board.setPiece(i, dim - 2, p2Piece);
			
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
		int dim = _board.getDimensions();
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
				
				_board.setPiece(i, 0, t1Piece);
				_board.setPiece(i, dim - 1, p3Piece);
				
				_board.setPiece(0, i, t2Piece);
				_board.setPiece(dim - 1, i, p4Piece);
				
			} else if(i == dim - 2) {
				Piece p1Piece = new Piece(i / 2, false, new Posn(i, 0), dim, p1);
				Piece p2Piece = new Piece(i / 2, false, new Posn(0, i), dim, p2);
				Piece t3Piece = new Piece(0, true, new Posn(i, dim - 1), dim, p3);
				Piece t4Piece = new Piece(0, true, new Posn(dim - 1, i), dim, p4);
				p1.addPiece(p1Piece);
				p2.addPiece(p2Piece);
				p3.addPiece(t3Piece);
				p4.addPiece(t4Piece);
				
				_board.setPiece(i, 0, p1Piece);
				_board.setPiece(i, dim - 1, t3Piece);
				
				_board.setPiece(0, i, p2Piece);
				_board.setPiece(dim - 1, i, t4Piece);
			} else {
				Piece p1Piece = new Piece(i / 2, false, new Posn(i, 0), dim, p1);
				Piece p2Piece = new Piece(i / 2, false, new Posn(0, i), dim, p2);
				Piece p3Piece = new Piece(numTerraces - (i / 2) - 1, false, new Posn(i, dim -1), dim, p3);
				Piece p4Piece = new Piece(numTerraces - (i / 2) - 1, false, new Posn(dim -1, i), dim, p4);
				p1.addPiece(p1Piece);
				p2.addPiece(p2Piece);
				p3.addPiece(p3Piece);
				p4.addPiece(p4Piece);
				
				_board.setPiece(i, 0, p1Piece);
				_board.setPiece(i, dim - 1, p3Piece);
			
				_board.setPiece(0, i, p2Piece);
				_board.setPiece(dim - 1, i, p4Piece);
			}
		}
	}
	
	/* ===================
	 * AI UTILITIES
	 * ====================*/

	
	public Game clone() throws CloneNotSupportedException{
		Game toRet = (Game) super.clone();
		toRet._board = _board.clone();
		toRet._players = new LinkedList<Player>();
		
		// make new players
		for (int i = 0; i < _numHuman; i++) 
			toRet._players.add(new Player(PlayerColor.values()[i]));
		for (int i = 0; i < _numAI; i++)
			toRet._players.add(new AI(PlayerColor.values()[i], toRet));
		
		// set up player's pieces
		for (int i = 0; i < _players.size(); i++){
			Player newPlayer =  toRet._players.get(i);
			for(Piece piece: _players.get(i).getPieces())
				newPlayer.addPiece(piece.clone());
		}
		
		return toRet;
	}
	
	/**
	 * Estimates the value of this game for the current player
	 * @return the value for this game for the current player
	 */
	public double estimateValue(){
		Player currPlayer = _players.get(_currPlayer);
		if (_isGameOver){
			if(_winner.equals(currPlayer)) 
				return Double.POSITIVE_INFINITY;
			else return Double.NEGATIVE_INFINITY;
		}
		
		double currentPlayerValue = estimatePlayerValue(currPlayer);
		double othersAvgValue = 0;
		for (Player other: _players)
			if (!other.equals(currPlayer))
				othersAvgValue += estimatePlayerValue(other);
		othersAvgValue /= (_players.size() - 1);
		
		return currentPlayerValue - othersAvgValue;
	}

	/**
	 * @param player - the Player whose value you're calculating
	 * @return - a double indicating the value of this player's current position in the game.
	 * Calculating the value of each piece by its size and its elevation.
	 * The bigger the size/the higher the elevation, the better.
	 * 
	 * TODO: 
	 * 
	 * currently adding the two. maybe rethink that? Have 2 AIs play each other, one adding,
	 * one not later on.
	 * 
	 * NEED TO REWRITE!
	 * PROBLEM: big pieces in the beginning are encouraged to stay where they are if they are
	 * at a higher elevation. In general, also need to take into account proximity to other pieces
	 * + attack positions. But this might already be taken account for? Might have to search too deep
	 * to get to this point though. grasd;flkjasd;lfkjasd;lfkj;
	 */
	private double estimatePlayerValue(Player player){
		double toRet = 0;
		for (Piece p : player.getPieces()){
			double pieceValue = (p.getSize() + 1) + getBoard().getElevation(p.getPosn());
			List<Move> possibleMoves = _board.getMoves(p);
			for (Move move : possibleMoves){
				
			}
			toRet += pieceValue;
		}
		return toRet;
	}
}
