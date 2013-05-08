package terrace.network;

import java.io.IOException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import terrace.Move;
import terrace.Piece;
import terrace.Player;
import terrace.PlayerColor;
import terrace.message.Port;

import com.google.common.base.Optional;

public class NetworkedServerPlayer extends Player {
	private final ClientConnection _conn;
	private final Port<NetworkMessage> _in;
	
	public NetworkedServerPlayer(ClientConnection conn, String name, PlayerColor color) {
		super(color);
		_conn = conn;
		_in = conn.getPort(name);
	}
	
	@Override
	public Optional<Move> getMove(int timeout) {
		for (int i = 0; i < timeout; i++) {
			if (wantToForfeit()) return Optional.absent();
			
			try {
				System.err.println("DEBUG: Asking " + getName() + " for move.");
				throw _in.get(1, TimeUnit.SECONDS);
			} catch (IOException | InterruptedException e) {
				System.err.println("LOG: " + e.getLocalizedMessage());
				return Optional.absent();
			} catch (TimeoutException e) {
				continue;
			} catch (MoveMessage m) {
				System.err.println("DEBUG: Got move message.");
				
				if (m.getTurnNumber() != _conn.getState().getTurnNumber()) continue;
				
				if (
						!_conn.getState().getBoard().inBounds(m.getFrom()) ||
						!_conn.getState().getBoard().inBounds(m.getTo())
				) {
					return invalid("out of bounds");
				}
				
				Piece p = _conn.getState().getBoard().getPieceAt(m.getFrom());
				
				if (p == null) return invalid("no such piece");
				
				if (p.getPlayer() != _conn.getState().getActivePlayer()) return invalid("wrong player");
				
				Move move = new Move(p, m.getTo(), _conn.getState().getBoard().getPieceAt(m.getTo()));
				
				if (!_conn.getState().getBoard().getMoves(p).contains(move)) {
					return invalid("illegal move");
				} else {
					return Optional.of(move);
				}
			} catch (NetworkMessage e) {
				System.err.println("LOG: Client sent invalid command.");
				return Optional.absent();
			}
		}
		System.out.println("LOG: Client sent no valid moves.");
		return Optional.absent();
	}

	private Optional<Move> invalid(String reason) {
		System.out.println("LOG: Client made bad move. Reason: " + reason);
		return Optional.absent();
	}
	
	@Override
	public boolean wantToForfeit() {
		return _conn.isClosed();
	}
}
