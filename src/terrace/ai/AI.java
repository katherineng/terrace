package terrace.ai;

import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;

import com.google.common.base.Optional;

import terrace.*;
import terrace.exception.IllegalMoveException;

public class AI extends Player {
	GameState _game;
		
	public AI(PlayerColor color) {
		super(color);
	}
	
	/**
	 * Represents a node in the search tree
	 * @author ww15
	 *
	 */
	private static class SearchNode implements Comparable<SearchNode> {
		private Move _move;		/** The move made **/
		private double _value;	/** the value associated a certain game state **/
		private double _heuristic; /** maybe store some value that makes AI more aggressive? **/
		
		private SearchNode(Move move, double value, double heuristic) {
			_move = move;
			_value = value;
			_heuristic = heuristic;
		}
		
		private double getHeuristic() {
			return _heuristic;
		}
		
		private double getValue() {
			return _value;
		}
		
		private Move getMove() {
			return _move;
		}

		@Override
		public int compareTo(SearchNode o) {
			if (_heuristic > o.getHeuristic()) return 1;
			if (_heuristic == o.getHeuristic()) return 0;
			return -1;
		}
	}
	
	@Override
	public Optional<Move> getMove(int timeout) {
		SearchNode node;
	//	try {
		//	node = minimax(0, 0, _game.copy());
			System.out.println("HI");
			System.out.println(_game.getBoard().piecesToString());
//			return  Optional.of(node.getMove());
			return Optional.of(naiveMakeMove());
//		} catch (IllegalMoveException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		assert false;
//		return null;
	}
	
	/**
	 * The AI naively makes a move. For testing purposes only
	 * @return
	 */
	private Move naiveMakeMove() {
		List<Piece> pieces = getPlayerPieces(this);
		
		assert(pieces.size() > 0);
		LinkedList<Move> possibleMoves = new LinkedList<Move>();
		
		for (Piece piece: pieces) {
			for (Move move : _game.getBoard().getMoves(piece)){
				if (!possibleMoves.contains(move))
						possibleMoves.addLast(move);
			}
		}
		
		Move toMove = possibleMoves.get(getRandom(possibleMoves.size()));

		return toMove;
	}	
	
	/**
	 * @param size - integer
	 * @return a number between [0, size)
	 */
	private int getRandom(int size){
		return (int)(Math.random() * ((size-1) + 1));
	}
	
	
	// minimax
	private SearchNode minimax(int currDepth, int maxDepth, GameState gameState) throws IllegalMoveException{
		assert(maxDepth % 2 == 0);
		boolean maximizing = currDepth % 2 == 0;
		List<Move> possibleMoves = getPossibleMoves(gameState, gameState.getActivePlayer());
		
		PriorityQueue<SearchNode> bestNode = new PriorityQueue<SearchNode>();
		double bestValue = (maximizing) ? Double.NEGATIVE_INFINITY : Double.POSITIVE_INFINITY;
		
		for (Move m: possibleMoves) {
			GameState g = getGameState(m, gameState);
			SearchNode currNode = (currDepth == maxDepth || g.getWinner().isPresent()) ? 
					new SearchNode(m, estimateValue(g, getPlayer(g)), 0) : 
					new SearchNode(m, minimax(currDepth + 1, maxDepth, g).getValue(), 0);
			if (currNode.getValue() == bestValue) 
				bestNode.add(currNode);
			else if (maximizing && currNode.getValue() > bestValue || // trying to maximize
				!maximizing && currNode.getValue() < bestValue){ // trying to minimize
				bestNode.clear();
				bestValue = currNode.getValue();
				bestNode.add(currNode);
			}
		}
		assert(bestNode.peek() != null);
		System.out.println("returns");
		return bestNode.poll();

	}

	/**
	 * Gets a new, resulting Game instance given a move and a game
	 * @param m - a Move
	 * @param gameState - a Game
	 * @return - a clone of <gameState> after <m> has been made
	 * @throws CloneNotSupportedException
	 * @throws IllegalMoveException
	 */
	private GameState getGameState(Move m, GameState gameState) throws IllegalMoveException {
		GameState copy = gameState.copy();
		copy.makeMove(m, null, null);
		return copy;
	}
	
	/**
	 * Gets the player that corresponds to this AI given a Game
	 * @param gameState - a Game
	 * @return - the Player associated with this AI
	 */
	private Player getPlayer(GameState gameState) {
		for (Player p : gameState.getPlayers()) {
			if (p.getColor().equals(this.getColor()))
				return p;
		}
		return null;
	}
	
	/**
	 * Gets a list of possibleMoves for a given player
	 * @param gameState - a Game
	 * @param player - a Player
	 * @return - the list of moves the <player> could make
	 */
	private List<Move> getPossibleMoves(GameState gameState, Player player) {
		List<Piece> pieces = getPlayerPieces(player);
		
		System.out.println(gameState.getBoard().piecesToString());
		LinkedList<Move> possibleMoves = new LinkedList<Move>();
		
		for (Piece piece : pieces) {
			for (Move move : gameState.getBoard().getMoves(piece)) {
				if (!possibleMoves.contains(move))
						possibleMoves.addLast(move);
			}
		}
		return possibleMoves;
	}
	

	/**
	 * Estimates the value of this game for the current player, by taking the difference
	 * between the current player value and the opposing player's value
	 * @return the value for this game for the current player
	 */
	public double estimateValue(GameState gameState, Player currPlayer) {
		if (gameState.getWinner().isPresent()) {
			if(gameState.getWinner().get().equals(currPlayer)) return Double.POSITIVE_INFINITY;
			else return Double.NEGATIVE_INFINITY;
		}
		
		double currentPlayerValue = estimatePlayerValue(currPlayer);
		double othersAvgValue = 0;
		for (Player other: gameState.getPlayers())
			if (!other.equals(currPlayer))
				othersAvgValue += estimatePlayerValue(other);
		othersAvgValue /= (gameState.getPlayers().size() - 1.0);
		return  currentPlayerValue - othersAvgValue;
	}

	/**
	 * @param player - the Player whose value you're calculating
	 * @return - a double indicating the value of this player's current position in the game.
	 * Calculating the value of each piece by its size.
	 */
	private double estimatePlayerValue(Player player) {
		double toRet = 0;
		for (Piece p : getPlayerPieces(player)) {
			double pieceValue = (p.getSize() + 1);
			toRet += pieceValue;
		}
		return toRet;
	}
	
	public void updateGameState(GameState game) {
		_game = game;
	}
	
	public List<Piece> getPlayerPieces(Player p) {
		return _game.getBoard().getPlayerPieces(p);
	}
}
