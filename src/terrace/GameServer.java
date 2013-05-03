package terrace;

import java.util.LinkedList;
import java.util.List;

import terrace.exception.IllegalMoveException;
import terrace.util.Callback;

import com.google.common.base.Optional;

public class GameServer {
	private final List<Callback<GameState>> _updateStateCBs = new LinkedList<>();
	private final List<Callback<Player>> _notifyWinnerCBs = new LinkedList<>();
	private final List<Callback<Player>> _notifyLoserCBs = new LinkedList<>();
	private GameState _game;
	
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
		while (true) {
			Optional<Move> move = _game.getActivePlayer().getMove(30);
			if (move.isPresent()) {
				System.out.println("move present");
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
}