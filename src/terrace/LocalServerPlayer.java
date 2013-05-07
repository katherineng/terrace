package terrace;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import terrace.message.Channel;
import terrace.message.Port;

import com.google.common.base.Optional;

public class LocalServerPlayer extends LocalPlayer {
	private final Port<Move> moves = new Port<>();
	private final Channel<Move> send = moves.newChannel();
	
	public LocalServerPlayer(PlayerColor color) {
		super(color);
	}
	
	@Override
	public Optional<Move> getMove(int timeout) {
		try {
			return moves.tryGet(timeout, TimeUnit.SECONDS);
		} catch (IOException e) {
			System.err.println("LOG: " + e.getLocalizedMessage());
			return Optional.absent();
		}
	}
	
	@Override
	public void sendMove(Move move, int turnNumber) {
		try {
			send.send(move);
		} catch (IOException | InterruptedException e) {
			System.err.println("LOG: " + e.getLocalizedMessage());
		}
	}
}
