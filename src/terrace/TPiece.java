package terrace;

import terrace.util.Posn;

public class TPiece extends Piece {
	private final Posn _goal;
	
	public TPiece(Posn posn, Player player, Posn goal) {
		super(0, posn, player);
		_goal = goal;
	}
	
	public boolean isAtGoal() {
		return _goal.equals(_posn);
	}
	
	@Override
	public String toString() {
		return "(" + getColor().toString() + ", T)";
	}
	
	@Override
	public Piece copy() {
		return new TPiece(_posn, _player, _goal);
	}
}
