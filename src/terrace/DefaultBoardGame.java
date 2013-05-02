package terrace;

import java.util.*;
import terrace.ai.AI;
import terrace.exception.IllegalMoveException;
import terrace.gui.LocalPlayer;
import terrace.util.Posn;

import com.google.common.base.*;

public class DefaultBoardGame implements Cloneable {
	private Board _board;
	private int _currPlayer;
	private List<Player> _players;
	private final int _numPlayers;
	private final int _numHuman; // need for cloning
	private final int _numAI;	// need for cloning
	private int _playersAlive;		/** Number of players that are currently still playing **/
	private boolean _isGameOver;
	private Optional<Player> _winner;
	private Variant _variant;

	public DefaultBoardGame(int numHuman, int numAI, int dimensions, Variant variant) throws IllegalMoveException {
		_numPlayers = numHuman + numAI;
		_numHuman = numHuman;
		_numAI = numAI;
		assert(_numPlayers == 2 || _numPlayers == 4);
		_variant = variant;
		_isGameOver = false;
		_winner = Optional.absent();

		_players = new ArrayList<Player>();
		for (int i = 0; i < numHuman; i++) 
			_players.add(new LocalPlayer(PlayerColor.values()[i]));
		for (int i = numHuman; i < _numPlayers; i++)
			_players.add(new AI(PlayerColor.values()[i], this));

		_playersAlive = _numPlayers;
		_currPlayer = 0;

		_board = BoardFactory.create(_players, dimensions, _variant);
		
		_players.get(_currPlayer).makeMove();
	}

	public Board getBoard() {
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
				checkWinner(playerPiece, to);
				
				current.getPieces().remove(playerPiece);
				_board.setPieceAt(from, null);
				
				Piece captured = _board.getPieceAt(to);
				if (captured != null) {
					if (captured instanceof TPiece) {
						_players.remove(captured.getPlayer());
						_playersAlive--;
						if (_players.size() == 1) 
							setWinner(current);
						else 
							removePlayerPieces(captured.getPlayer());
					} else  captured.getPlayer().getPieces().remove(captured);
				}
				
				if (!(captured != null && captured instanceof TPiece && captured.getPlayer().equals(playerPiece.getPlayer()))){
					playerPiece.setPosn(to);
					current.getPieces().add(playerPiece);
					_board.setPieceAt(to, playerPiece);
				}
				
				if (captured != null && captured instanceof TPiece && _currPlayer < getPlayerNumber(captured.getPlayer())) 
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
	protected int getPlayerNumber(Player player) {
		for (int i = 0; i < _players.size(); i++)
			if (_players.get(i).equals(player)) return i;
		return -1;
	}

	/**
	 * Removes all the pieces of a certain player
	 * @param p - a Player whose pieces you want to remove
	 */
	protected void removePlayerPieces(Player p) {
		getBoard().removePlayer(p);
		p.getPieces().clear();
	}

	/**
	 * Sets the winner	
	 * @param winner - the player that is the winner
	 */
	protected void setWinner(Player winner) {
		_winner = Optional.of(winner);
		_isGameOver = true;		
	}

	/* ===================
	 * AI UTILITIES
	 * ====================*/


	public DefaultBoardGame clone() throws CloneNotSupportedException{
		DefaultBoardGame toRet = (DefaultBoardGame) super.clone();
		toRet._board = new DefaultBoard(_board.getWidth(), _variant);
		toRet._players = new LinkedList<Player>();

		// make new players
		for (int i = 0; i < _numHuman; i++) 
			toRet._players.add(new LocalPlayer(PlayerColor.values()[i]));
		for (int i = _numHuman; i < _numPlayers; i++)
			toRet._players.add(new AI(PlayerColor.values()[i], toRet));

		// set up player's pieces
		for (int i = 0; i < _players.size(); i++) {
			Player newPlayer =  toRet._players.get(i);
			for(Piece piece : _players.get(i).getPieces()) {
				if (piece instanceof TPiece) {
					
				}
				Piece newPiece = piece.copy();
				
				newPlayer.addPiece(newPiece);
				toRet._board.setPieceAt(newPiece.getPosn(), newPiece);
			}
		}

		return toRet;
	}
	
	//TODO delete later
	/**
	 * Attempts to make a move from a given position to a given position and returns the captured piece, if any
	 * @param from The position from which to move
	 * @param to The destination position
	 * @return The captured piece, if any; otherwise null
	 * @throws IllegalMoveException When a move from the given position to the given destination position is not legal
	 */
	public Piece movePieceAI(Posn from, Posn to) throws IllegalMoveException {
		Player current = getCurrentPlayer();
		Piece playerPiece = _board.getPieceAt(from);
		if (playerPiece == null || !current.getPieces().contains(playerPiece)) {
			throw new IllegalMoveException("ERROR: " + _currPlayer + "\'s piece not found at " + from.toString());
		} else {
			List<Move> possibleMoves = _board.getMoves(playerPiece);

			if (!possibleMoves.contains(new Move(playerPiece, to))) {
				throw new IllegalMoveException("ERROR: Piece at " + from.toString() + " can't be moved to " + to.toString());
			} else {
				checkWinner(playerPiece, to);
				
				current.getPieces().remove(playerPiece);
				_board.setPieceAt(from, null);

				Piece captured = _board.getPieceAt(to);
				if (captured != null) {
					if (captured instanceof TPiece) {
						_players.remove(captured.getPlayer());
						_playersAlive--;
						if (_players.size() == 1) 
							setWinner(current);
						else 
							removePlayerPieces(captured.getPlayer());
					} else  captured.getPlayer().getPieces().remove(captured);
				}
				
				if (!(captured != null && captured instanceof TPiece && captured.getPlayer().equals(playerPiece.getPlayer()))){
					playerPiece.setPosn(to);
					current.getPieces().add(playerPiece);
					_board.setPieceAt(to, playerPiece);
				}
				
				if (captured != null && captured instanceof TPiece && _currPlayer < getPlayerNumber(captured.getPlayer())) 
					_currPlayer--;
				if (_currPlayer < _playersAlive - 1) _currPlayer++;
				else _currPlayer = 0;
				return captured;
			}
		}
	}
	private void checkWinner(Piece piece, Posn to) {
		if (piece instanceof TPiece && ((TPiece)piece).getGoal().equals(to)) {
			setWinner(piece.getPlayer());
		}
	}
}
