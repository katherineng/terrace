package terrace;

import terrace.util.Callback;

import com.google.common.base.Optional;

public class LocalGameServer extends GameServer {
	private boolean _closed = false;
	private final int _timeout;
	
	public LocalGameServer(
			int timeout,
			GameState state
	) {
		_game = state;
		_timeout = timeout;
	}
	
	/* (non-Javadoc)
	 * @see terrace.GameServer#run()
	 */
	@Override
	public void run(Runnable onReady) {
		if (onReady != null) onReady.run();
		
		while (!_closed) {
			Optional<Move> move = _game.getActivePlayer().getMove(_timeout);
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
	
	/* (non-Javadoc)
	 * @see terrace.GameServer#close()
	 */
	@Override
	public void close() {
		_closed = true;
	}
}