package terrace.network;

import terrace.LocalPlayer;
import terrace.Move;
import terrace.PlayerColor;

import com.google.common.base.Optional;

public class ClientLocalPlayer extends LocalPlayer {
	private final ClientGameServer _server;
	
	public ClientLocalPlayer(PlayerColor color, ClientGameServer server) {
		super(color);
		_server = server;
	}
	
	@Override
	public void sendMove(Move m, int turnNumber) {
		_server.sendMove(m, turnNumber);
	}
	
	@Override
	public Optional<Move> getMove(int timeout) {
		// We should never ask a client player for its moves.
		// The moves just get updated through the game state.
		assert false;
		return Optional.absent();
	}
}
