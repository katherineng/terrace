package terrace.gui;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import message.Channel;
import message.Port;

import com.google.common.base.Optional;

import terrace.*;

public class LocalPlayer extends Player {
	private final Port<Move> moves = new Port<>();
	private final Channel<Move> send = moves.newChannel();
	
	public LocalPlayer(PlayerColor color) {
		super(color);
	}
	
	@Override
	public Optional<Move> getMove(int timeout) {
		return moves.tryGet(timeout, TimeUnit.SECONDS);
	}
	
	public void sendMove(Move move) {
		try {
			send.send(move);
		} catch (IOException | InterruptedException e) {
			System.err.println("LOG: " + e.getLocalizedMessage());
		}
	}
}
