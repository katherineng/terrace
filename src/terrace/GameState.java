package terrace;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import terrace.util.Callback;
import terrace.util.Copyable;

import com.google.common.base.Optional;

public class GameState implements Copyable<GameState> {
	private int _turnNumber;
	private final Board _board;
	private List<Player> _players;
	private int _active = 0;
	private Player _winner = null;
	
	public GameState(Board board, List<Player> players, int active, int turnNumber) {
		_turnNumber = turnNumber;
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
		_turnNumber++;
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
						
						if (_players.size() == 1) {
							_winner = _players.get(0);
							if (playerWon != null) playerWon.call(_players.get(0));
						} else if (playerLost != null) {
							playerLost.call(p.getPlayer());
						}
					}
				}
			} else {
				throw new IllegalMoveException("Bad move: " + m);
			}
		} finally {
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
		GameState copy = new GameState(_board.copy(), new ArrayList<>(_players), _active, _turnNumber + 1);
		copy._winner = _winner;
		return copy;
	}
	
	public void serialize(PrintWriter out) {
		out.println(_turnNumber);
		out.print(_active);
		out.print('/');
		out.println(_players.size());
		for (Player p : _players) {
			out.println(p.getName());
		}
		out.println();
		_board.serialize(out);
	}
}
