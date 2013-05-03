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
	
	public void makeMove(
			Move m,
			Callback<Player> playerLost,
			Callback<Player> playerWon
	) throws IllegalMoveException {
		System.out.println("making move");
		try {
			if(isValid(m, getActivePlayer())) {
				_board.makeMove(m);
				
				Piece piece = m.getPiece();
				if (piece instanceof TPiece && ((TPiece)piece).isAtGoal()) {
					_winner = piece.getPlayer();
					if (playerWon != null) playerWon.call(_winner);
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
							playerWon.call(piece.getPlayer());
						} else if (playerLost != null) {
							playerLost.call(p.getPlayer());
						}
					}
				}
			} else {
				throw new IllegalMoveException("Bad move");
			}
		} finally {
			System.out.println("ending");
			endTurn();
		}
	}
	
	public boolean isValid(Move m, Player activePlayer) {
		if (!m.getPiece().getPlayer().equals(activePlayer)) return false;
		
		List<Move> allowed = _board.getMoves(m.getPiece());
		
		return allowed.contains(m);
	}
	
	@Override
	public GameState copy() {
		return new GameState(
				_board.copyBoard(),
				new ArrayList<>(_players),
				_active
		);
	}
}