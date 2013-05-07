package terrace.network;

import terrace.util.Posn;

public class MoveMessage extends NetworkMessage {
	private static final long serialVersionUID = 3170726720229780892L;
	
	private final int _turnNumber;
	private final Posn _from;
	private final Posn _to;
	
	public MoveMessage(int turnNumber, Posn from, Posn to) {
		_turnNumber = turnNumber;
		_from = from;
		_to = to;
	}
	
	public int getTurnNumber() {
		return _turnNumber;
	}
	
	public Posn getFrom() {
		return _from;
	}
	
	public Posn getTo() {
		return _to;
	}
}
