package terrace;

import java.io.Closeable;
import java.util.LinkedList;
import java.util.List;

import terrace.exception.IllegalMoveException;
import terrace.util.Callback;

import com.google.common.base.Optional;

public class GameServer implements Closeable {
	private final List<Callback<GameState>> _updateStateCBs = new LinkedList<>();
	private final List<Callback<Player>> _notifyWinnerCBs = new LinkedList<>();
	private final List<Callback<Player>> _notifyLoserCBs = new LinkedList<>();
	
	private GameState _game;
	private boolean _closed = false;
	
	public GameServer(
			GameState state
	) {
		_game = state;
	}
	
	public GameState getState() {
		return _game;
	}
	
	public void addUpdateStateCB(Callback<GameState> cb) {
		_updateStateCBs.add(cb);
	}
	
	public void addWinnerCB(Callback<Player> cb) {
		_notifyWinnerCBs.add(cb);
	}
	
	public void addLoserCB(Callback<Player> cb) {
		_notifyLoserCBs.add(cb);
	}
	
	public void run() {
		while (!_closed) {
			Optional<Move> move = _game.getActivePlayer().getMove(45);
			if (move.isPresent()) {
				try {
					_game.makeMove(
							move.get(),
							new Callback<Player>() {
								@Override
								public void call(Player loser) {
									for (Callback<Player> cb : _notifyLoserCBs) {
										cb.call(loser);
									}
								}
							},
							new Callback<Player>() {
								@Override
								public void call(Player winner) {
									for (Callback<Player> cb : _notifyWinnerCBs) {
										cb.call(winner);
									}
								}
							}
					);
				} catch (IllegalMoveException e) {
					// Skip turn.
				}
			} else {
				_game.endTurn();
			}
			
			for (Callback<GameState> cb : _updateStateCBs) {
				cb.call(_game);
			}
			
			if (_game.getWinner().isPresent()) return;
		}
	}
	
	@Override
	public void close() {
		_closed = true;
	}
}