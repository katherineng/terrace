package terrace;

import java.util.ArrayList;
import java.util.List;

import com.google.common.base.Optional;

import terrace.exception.IllegalMoveException;
import terrace.util.Callback;
import terrace.util.Copyable;

public class GameState implements Copyable<GameState> {
	private final Board _board;
	private List<Player> _players;
	private int _active = 0;
	private Player _winner = null;
	
	public GameState(Board board, List<Player> players, int active) {
		_board = board;
		_players = players;
		_active = active;
	}
	
	public Board getBoard() {
		return _board;
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
	
	//TODO
	public void forfeitGame(Player player) {
		if (_players.indexOf(player) < _active) _active--;
		_players.remove(player);
		_board.removePlayer(player);
	
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
					_winner = piece.getPlayer();
					if (playerWon != null) playerWon.call(_winner);
					System.out.println("GameState: GAME!");
					return;
				}
				
				Optional<Piece> captured = m.getCapturedPiece();
				if (captured.isPresent()) {
					Piece p = captured.get();
					
					if (p instanceof TPiece) {
						if (_players.indexOf(p.getPlayer()) < _active) _active--;
						_players.remove(p.getPlayer());
						_board.removePlayer(p.getPlayer());
						
						if (_players.size() == 1 && playerWon != null) {
							playerWon.call(_players.get(0));
						} else if (playerLost != null) {
							playerLost.call(p.getPlayer());
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
	
	public boolean isValid(Move m, Player activePlayer) {
		if (!m.getPiece().getPlayer().equals(activePlayer)) return false;
		//System.out.println(m.getPiece().getPosn().x + " " + m.getPiece().getPosn().y + " => " + m.getTo().x + " " + m.getTo().x);
		//System.out.println(_board.piecesToString());
		List<Move> allowed = _board.getMoves(m.getPiece());
		return allowed.contains(m);
	}
	
	@Override
	public GameState copy() {
		GameState copy = new GameState(_board.copy(), new ArrayList<>(_players), _active);
		copy._winner = _winner;
		return copy;
	}
}