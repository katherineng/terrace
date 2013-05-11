package terrace;

import java.io.BufferedReader;
import java.io.EOFException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

import terrace.util.Callback;
import terrace.util.Copyable;

import com.google.common.base.Optional;

public class GameState implements Copyable<GameState> {
	private static final Pattern numberPattern = Pattern.compile("[0-9]+");
	
	private int _turnNumber;
	private final Board _board;
	private List<Player> _players;
	private int _active = 0;
	private Player _winner = null;
	
	public GameState(Board board, List<Player> players, int active, int turnNumber) {
		_turnNumber = turnNumber;
		_board = board;
		_players = players;
		_active = active;
		
		if (players.size() == 1) _winner = players.get(0);
	}
	
	public Board getBoard() {
		return _board;
	}
	
	public Optional<Player> getWinner() {
		return Optional.fromNullable(_winner);
	}
	
	public List<Player> getPlayers() {
		return _players;
	}
	
	public Player getActivePlayer() {
		return _players.get(_active);
	}
	
	public void endTurn() {
		_active = (_active + 1) % _players.size();
		_turnNumber++;
	}
	
	public void forfeitGame(Player player) {
		if (_players.indexOf(player) < _active) _active--;
		_players.remove(player);
		_board.removePlayer(player);
		
		if (_players.size() == 1) _winner = _players.get(0);
	}
	
	public void makeMove(
			Move m,
			Callback<Player> playerLost,
			Callback<Player> playerWon
	) throws IllegalMoveException {
		try {
			if(isValid(m, getActivePlayer())) {
				_board.makeMove(m);
				
				Piece piece = m.getPiece();
				if (piece instanceof TPiece && ((TPiece)piece).isAtGoal()) {
					_winner = piece.getPlayer();
					if (playerWon != null) playerWon.call(_winner);
					System.out.println("GameState: GAME!");
					return;
				}
				
				Optional<Piece> captured = m.getCapturedPiece();
				if (captured.isPresent()) {
					Piece p = captured.get();
					
					if (p instanceof TPiece) {
						if (_players.indexOf(p.getPlayer()) < _active) _active--;
						_players.remove(p.getPlayer());
						_board.removePlayer(p.getPlayer());
						
						if (_players.size() == 1) {
							_winner = _players.get(0);
							if (playerWon != null) playerWon.call(_players.get(0));
						} else if (playerLost != null) {
							playerLost.call(p.getPlayer());
						}
					}
				}
			} else {
				throw new IllegalMoveException("Bad move: " + m);
			}
		} finally {
			endTurn();
		}
	}
	
	public boolean isValid(Move m, Player activePlayer) {
		if (!m.getPiece().getPlayer().equals(activePlayer)) return false;
		List<Move> allowed = _board.getMoves(m.getPiece());
		return allowed.contains(m);
	}
	
	@Override
	public GameState copy() {
		GameState copy = new GameState(_board.copy(), new ArrayList<>(_players), _active, _turnNumber + 1);
		copy._winner = _winner;
		return copy;
	}
	
	public void serialize(PrintWriter out) {
		out.println(_turnNumber);
		out.println(getActivePlayer().getColor().ordinal());
		_board.serialize(out);
	}
	
	public int getTurnNumber() {
		return _turnNumber;
	}
	
	public static GameState read(BufferedReader in, List<Player> players) throws IOException {
		int turnNum = readIntLine(in);
		int active = readIntLine(in);
		
		if (active >= players.size()) throw new IOException("Server sent bad active player");
		
		Board b = BoardFactory.read(in, players);
		
		Set<Player> activePlayers = new HashSet<>();
		for (Piece p : b.getPieces()) activePlayers.add(p.getPlayer());
		
		List<Player> activePlayerList = new LinkedList<>();
		
		for (Player p : players) {
			if (activePlayers.contains(p)) activePlayerList.add(p);
		}
		
		return new GameState(b, activePlayerList, active, turnNum);
	}
	
	private static int readIntLine(BufferedReader in) throws IOException {
		String line = in.readLine();
		
		if (line == null) throw new EOFException("Server connection closed");
		if (!numberPattern.matcher(line).matches()) {
			throw new IOException("Server sent bad number");
		}
		return Integer.parseInt(line);
	}
}
