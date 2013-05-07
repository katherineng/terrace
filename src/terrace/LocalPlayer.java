package terrace;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import terrace.message.Channel;
import terrace.message.Port;

import com.google.common.base.Optional;

public class LocalPlayer extends Player {
	private final Port<Move> moves = new Port<>();
	private final Channel<Move> send = moves.newChannel();
	
	public LocalPlayer(PlayerColor color) {
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
	
	public void sendMove(Move move) {
		try {
			send.send(move);
		} catch (IOException | InterruptedException e) {
			System.err.println("LOG: " + e.getLocalizedMessage());
		}
	}
	
	@Override
	public boolean isLocalHuman() {
		return true;
	}
}
