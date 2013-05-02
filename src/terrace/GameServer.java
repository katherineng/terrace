package terrace;

import java.util.HashMap;
import java.util.List;

import terrace.exception.IllegalMoveException;
import terrace.util.Callback;

import com.google.common.base.Optional;

public class GameServer {
	private final List<Player> _players;
	private GameState _game;
	
	public GameServer(List<Player> players, Board board) {
		_players = players;
		HashMap<PlayerColor, Player> map = new HashMap<PlayerColor, Player>();
		for (Player p: _players)
			map.put(p.getColor(), p);
		_game = new GameState(board, players, 0, map);
	}
	public void run() {
		while (true) {
			Optional<Move> move = _game.getActivePlayer().getMove(30);
			
			if (move.isPresent()) {
				try {
					_game.makeMove(
							move.get(),
							new Callback<Player>() {
								@Override
								public void call(Player loser) {
									// TODO: Notify losers.
								}
							},
							new Callback<Player>() {
								@Override
								public void call(Player winner) {
									for (Player player : _players) {
										player.notifyWinner(winner);
									}
								}
							}
					);
				} catch (IllegalMoveException e) {
					// Skip turn.
				}
			}
			_game.endTurn();
			
			for (Player player : _players) {
				player.updateState(_game);
			}
			
			if (_game.getWinner().isPresent()) return;
		}
	}
}
