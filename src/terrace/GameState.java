package terrace;

import java.util.*;

import com.google.common.base.Optional;

import terrace.exception.IllegalMoveException;
import terrace.util.Callback;
import terrace.util.Copyable;

public class GameState implements Cloneable {
	private final Board _board;
	private List<Player> _players;
	private int _active = 0;
	private Player _winner = null;
	private Map<PlayerColor, Player> _colorToPlayer;
	
	public GameState(Board board, List<Player> players, int active, Map<PlayerColor, Player> map) {
		_board = board;
		_players = players;
		_active = active;
		_colorToPlayer = map;
	}
	
	public Board getBoard() {
		return _board;
	}
	
	public Player getPlayer(PlayerColor color){
		return _colorToPlayer.get(color);
	}
	
	public Optional<Player> getWinner() {
		return Optional.fromNullable(_winner);
	}
	
	public List<Player> getPlayers() {
		return _players;
	}
	
	public Player getActivePlayer() {
		return _players.get(_active);
	}
	
	public void endTurn() {
		_active = (_active + 1) % _players.size();
	}
	
	public void makeMove(
			Move m,
			Callback<Player> playerLost,
			Callback<Player> playerWon
	) throws IllegalMoveException {
		try {
			if(isValid(m, getActivePlayer())) {
				_board.makeMove(m);
				
				Piece piece = m.getPiece();
				if (piece instanceof TPiece && ((TPiece)piece).isAtGoal()) {
					_winner = _colorToPlayer.get(piece.getColor());
					if (playerWon != null) playerWon.call(_winner);
					return;
				}
				
				Optional<Piece> captured = m.getCapturedPiece();
				if (captured != null && captured.isPresent()) {
					Piece p = captured.get();
					
					if (p instanceof TPiece) {
						if (_players.indexOf(p.getColor()) >= _active) _active--;
						_players.remove(p.getColor());
						_board.removePlayer(_colorToPlayer.get(p.getColor()));
						
						if (_players.size() == 1 && playerWon != null) {
							playerWon.call(_colorToPlayer.get(p.getColor()));
						} else if (playerLost != null) {
							playerLost.call(_colorToPlayer.get(p.getColor()));
						}
					}
				}
			} else {
				throw new IllegalMoveException("Bad move");
			}
		} finally {
			endTurn();
		}
	}
	
	private boolean isValid(Move m, Player activePlayer) {
		// TODO Auto-generated method stub
		return true;
	}
	
	@Override
	public GameState clone() throws CloneNotSupportedException {
		Board newBoard = _board.copyBoard();
		
		LinkedList<Player> newPlayers = new LinkedList<Player>();
		for (Player p: _players)
			newPlayers.addLast(p.clone());
		
		HashMap<PlayerColor, Player> newMap = new HashMap<PlayerColor, Player>();
		for (Player p: newPlayers)
			newMap.put(p.getColor(), p);
		
		return new GameState(newBoard, newPlayers, _active, newMap);
	}
}
