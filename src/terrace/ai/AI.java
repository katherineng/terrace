package terrace.ai;

import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;

import com.google.common.base.Optional;

import terrace.*;
import terrace.exception.IllegalMoveException;
import terrace.util.Posn;

public class AI extends Player {
	GameState _game;
	int lookahead = -1;

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

		private double getValue() {
			return _value;
		}

		private Move getMove() {
			return _move;
		}

		@Override
		public int compareTo(SearchNode o) {
			return Double.compare(_heuristic, o._heuristic);
		}
	}

	@Override
	public Optional<Move> getMove(int timeout) {
		SearchNode node;		
		try {
			assert(lookahead != -1);
			node = minimax(0, lookahead, _game.copy(), Double.POSITIVE_INFINITY);
			return Optional.of(node.getMove());
		} catch (IllegalMoveException e) {
			System.err.println(e.getMessage() + " ERROR: AI made invalid move. This shouldn't happen.");
		}

		assert false;
		return null;
	}
	
	
	private SearchNode minimax(int currDepth, int maxDepth, GameState gameState, double limit) throws IllegalMoveException{
		//assert(maxDepth % 2 == 0);
		boolean maximizing = gameState.getActivePlayer() == this;

		double nextLimit = (maximizing) ? Double.NEGATIVE_INFINITY : Double.POSITIVE_INFINITY;
		List<Move> possibleMoves = getPossibleMoves(gameState, gameState.getActivePlayer());

		PriorityQueue<SearchNode> bestNode = new PriorityQueue<>();
		double bestValue = nextLimit;
		assert(possibleMoves.size() > 0);
		for (Move m: possibleMoves){
			GameState g = getGameState(m, gameState);
			
			double newLimit = (bestNode.isEmpty()) ? nextLimit : bestNode.peek().getValue();
			
			SearchNode currNode = (currDepth == maxDepth || (g.getWinner() != null && g.getWinner().isPresent())) ? 
			new SearchNode(m, estimateValue(g, AI.this), heuristicValue(g)) : 
			new SearchNode(m, minimax(currDepth + 1, maxDepth, g, newLimit).getValue(), heuristicValue(g));

//			if ( !maximizing && currNode.getValue() <= limit)
//				return currNode;
			
			if (currNode.getValue() == bestValue && currNode.getValue() == bestValue){
				bestNode.add(currNode);
			} else if (maximizing && currNode.getValue() > bestValue || // trying to maximize
					!maximizing && currNode.getValue() < bestValue){ // trying to minimize
				bestNode.clear();
				bestValue = currNode.getValue();
				bestNode.add(currNode);
				limit = bestValue;
			}
		}
		assert(bestNode.peek() != null);
		return bestNode.poll();
	}
	
	private double heuristicValue(GameState g){
		List<Piece> tPieces = getOppTPiece(g);
		double toRet = 0;
		for (Piece piece: g.getBoard().getPlayerPieces(this)){
			for (Piece tPiece: tPieces){
				toRet += euclideanDistance(piece.getPosn(), tPiece.getPosn());
			}
		}
		return toRet;
	}
	
	private double euclideanDistance(Posn pos1, Posn pos2){
		return Math.sqrt(Math.pow(pos1.x - pos2.x, 2) + Math.pow(pos1.y - pos2.y, 2));
	}
	
	private List<Piece> getOppTPiece(GameState g){
		List<Piece> toRet =  new LinkedList<>();
		for (Player player: g.getPlayers()){
			if (player != AI.this){
				for (Piece piece: g.getBoard().getPlayerPieces((player))){
					if (piece.isTPiece()) toRet.add(piece);
				}
			}
		}
		return toRet;
		
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
	 * Gets a list of possibleMoves for a given player
	 * @param gameState - a Game
	 * @param player - a Player
	 * @return - the list of moves the <player> could make
	 */
	private List<Move> getPossibleMoves(GameState gameState, Player player) {
		List<Piece> pieces = gameState.getBoard().getPlayerPieces((player));
		LinkedList<Move> possibleMoves = new LinkedList<Move>();

		for (Piece piece : pieces)
			for (Move move : gameState.getBoard().getMoves(piece))
				possibleMoves.addLast(move);

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

		double currentPlayerValue = estimatePlayerValue(currPlayer, gameState);
		double othersAvgValue = 0;
		for (Player other: gameState.getPlayers())
			if (!other.equals(currPlayer))
				othersAvgValue += estimatePlayerValue(other, gameState);
		othersAvgValue /= (gameState.getPlayers().size() - 1.0);
		return  currentPlayerValue - othersAvgValue;
	}

	/**
	 * @param player - the Player whose value you're calculating
	 * @return - a double indicating the value of this player's current position in the game.
	 * Calculating the value of each piece by its size.
	 */
	private double estimatePlayerValue(Player player, GameState gameState) {
		double toRet = 0;
		List<Piece> pieceList = (gameState.getBoard().getPlayerPieces(player));
		for (Piece p : pieceList) {
			double pieceValue = (p.getSize() + 1);
			toRet += pieceValue;
		}
		return toRet;
	}

	public void updateGameState(GameState game) {
		if (lookahead == -1) lookahead = (game.getPlayers().size() == 2) ? 2 : 3;
		_game = game;
	}
}
