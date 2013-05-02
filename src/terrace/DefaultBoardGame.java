package terrace;

import java.util.*;
import terrace.ai.AI;
import terrace.exception.IllegalMoveException;
import terrace.gui.LocalPlayer;
import terrace.util.Posn;

import com.google.common.base.*;

public class DefaultBoardGame {
	private GameState _game;
	private int _currPlayer;
	private List<Player> _players;
	private final int _numPlayers;
	private int _playersAlive;		/** Number of players that are currently still playing **/
	private boolean _isGameOver;
	private Optional<Player> _winner;
	private Variant _variant;

	public DefaultBoardGame(int numHuman, int numAI, int dimensions, Variant variant) throws IllegalMoveException {
		_numPlayers = numHuman + numAI;
		assert(_numPlayers == 2 || _numPlayers == 4);
		_variant = variant;
		_isGameOver = false;
		_winner = Optional.absent();

		_players = new ArrayList<Player>();
		HashMap<PlayerColor, Player> colorToPlayer = new HashMap<PlayerColor, Player>();
		for (int i = 0; i < numHuman; i++){
			PlayerColor color  = PlayerColor.values()[i];
			Player newPlayer = new LocalPlayer(color);
			_players.add(newPlayer);
			colorToPlayer.put(color,  newPlayer);
			
		}
			
		for (int i = numHuman; i < _numPlayers; i++)
			_players.add(new AI(PlayerColor.values()[i]));
		
		_game = new GameState(
				BoardFactory.create(_players, dimensions, _variant),
				_players,
				0, colorToPlayer
		);
		
		for (Player p : _players) p.updateState(_game);
		
		_playersAlive = _numPlayers;
		_currPlayer = 0;
		
		_players.get(_currPlayer).makeMove();
	}

	
	public Player getPlayer(PlayerColor color){
		return _game.getPlayer(color);
	}
	public Board getBoard() {
		return _game.getBoard();
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
		if (!_isGameOver) 
			_players.get(_currPlayer).makeMove();
	}

	protected boolean noPossibleMoves() {
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
		if (!isGameOver()){
			System.out.println("default board game move");
			Player current = getCurrentPlayer();
			Piece playerPiece = _game.getBoard().getPieceAt(from);
			if (playerPiece == null || !current.getPieces().contains(playerPiece)) {
				throw new IllegalMoveException("ERROR: " + _currPlayer + "\'s piece not found at " + from.toString());
			} else {
				List<Move> possibleMoves = _game.getBoard().getMoves(playerPiece);
	
				if (!possibleMoves.contains(new Move(playerPiece, to))) {
					throw new IllegalMoveException("ERROR: Piece at " + from.toString() + " can't be moved to " + to.toString());
				} else {
					checkWinner(playerPiece, to);
					
					current.getPieces().remove(playerPiece);
					_game.getBoard().setPieceAt(from, null);
					
					Piece captured = _game.getBoard().getPieceAt(to);
					if (captured != null) {
						if (captured instanceof TPiece) {
							_players.remove(getPlayer(captured.getColor()));
							_playersAlive--;
							if (_players.size() == 1) 
								setWinner(current);
							else 
								removePlayerPieces(_game.getPlayer(captured.getColor()));
						} else  _game.getPlayer(captured.getColor()).getPieces().remove(captured);
					}
					
					//if (!(captured != null && captured instanceof TPiece && captured.getColor().equals(playerPiece.getColor()))) {
					playerPiece.setPosn(to);
					current.getPieces().add(playerPiece);
					_game.getBoard().setPieceAt(to, playerPiece);
					//}
					
					if (captured != null && captured instanceof TPiece && _currPlayer < getPlayerNumber(_game.getPlayer(captured.getColor()))) 
						_currPlayer--;
					if (_currPlayer < _playersAlive - 1) _currPlayer++;
					else _currPlayer = 0;
					changeTurn();
	
					return captured;
				}
			}
		}
		return null;
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
		System.out.println(p.getColor());
		getBoard().removePlayer(p);
		p.getPieces().clear();
	}

	/**
	 * Sets the winner	
	 * @param winner - the player that is the winner
	 */
	protected void setWinner(Player winner) {
		_winner = Optional.of(winner);
		System.out.println("GAME OVER WINNER IS " + _winner.get().getColor());
		System.out.println(getBoard().piecesToString());
		_isGameOver = true;		
	}

	/* ===================
	 * AI UTILITIES
	 * ====================*/
	private void checkWinner(Piece piece, Posn to) {
		if (piece instanceof TPiece && ((TPiece)piece).isAtGoal()) {
			setWinner(_game.getPlayer(piece.getColor()));
		}
	}
}
