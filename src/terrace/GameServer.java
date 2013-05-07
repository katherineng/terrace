package terrace;

import java.io.Closeable;
import java.util.LinkedList;
import java.util.List;

import terrace.util.Callback;

public abstract class GameServer implements Closeable {
	protected final List<Callback<GameState>> _updateStateCBs = new LinkedList<>();
	protected final List<Callback<Player>> _notifyWinnerCBs = new LinkedList<>();
	protected final List<Callback<Player>> _notifyLoserCBs = new LinkedList<>();
	
	protected GameState _game;
	
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
	
	public abstract void run();
}
