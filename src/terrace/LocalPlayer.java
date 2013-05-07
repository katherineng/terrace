package terrace;

public abstract class LocalPlayer extends Player {
	public LocalPlayer(PlayerColor color) {
		super(color);
	}
	
	public abstract void sendMove(Move m, int turnNumber);
}
