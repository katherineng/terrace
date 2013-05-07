package terrace.network;

import com.google.common.base.Optional;

import terrace.Move;
import terrace.Player;
import terrace.PlayerColor;

public class NetworkedClientPlayer extends Player {
	public NetworkedClientPlayer(PlayerColor color) {
		super(color);
	}
	
	@Override
	public Optional<Move> getMove(int timeout) {
		// We should never ask a networked player for its moves.
		// The moves just get updated through the game state.
		assert false;
		return Optional.absent();
	}
}
