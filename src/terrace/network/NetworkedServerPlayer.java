package terrace.network;

import java.io.IOException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import terrace.Move;
import terrace.Player;
import terrace.PlayerColor;
import terrace.message.Channel;
import terrace.message.Port;

import com.google.common.base.Optional;

public class NetworkedServerPlayer extends Player {
	private final Port<NetworkMessage> _in;
	private final Channel<NetworkMessage> _out;
	
	public NetworkedServerPlayer(
			PlayerColor color,
			Port<NetworkMessage> in,
			Channel<NetworkMessage> out
	) {
		super(color);
		_in = in;
		_out = out;
	}
	
	public NetworkedServerPlayer(ClientConnection conn, String name, PlayerColor color) {
		this(color, conn.getPort(name), conn.getChannel(name));
	}
	
	@Override
	public Optional<Move> getMove(int timeout) {
		try {
			throw _in.get(timeout, TimeUnit.SECONDS);
		} catch (IOException | InterruptedException e) {
			System.err.println("LOG: " + e.getLocalizedMessage());
			return Optional.absent();
		} catch (TimeoutException e) {
			return Optional.absent();
		} catch (NetworkMessage e) {
			System.err.println("LOG: Client sent invalid command.");
			return Optional.absent();
		}
	}
}
