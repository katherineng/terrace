package terrace.ai;

import java.util.LinkedList;
import java.util.List;

import terrace.*;
import terrace.exception.IllegalMoveException;

public class AI extends Player {
	DefaultBoardGame _game;
		
	public AI(PlayerColor color, DefaultBoardGame game) {
		super(color);
		_game = game;
	}

	/**
	 * Represents a node in the search tree
	 * @author ww15
	 *
	 */
	private static class SearchNode{
		private Move _move;		/** The move made **/
		private double _value;	/** the value associated a certain game state **/
		
		private SearchNode(Move move, double value){
			_move = move;
			_value = value;
		}
		
		private Move getMove(){
			return _move;
		}
		
		private double getValue(){
			return _value;
		}
	}
	
	@Override
	public boolean makeMove() {
//		List<Piece> pieces = getPieces();
//		assert(pieces.size() > 0);
//		LinkedList<Move> possibleMoves = new LinkedList<Move>();
//		
//		for (Piece piece: pieces) {
//			for (Move move : _game.getBoard().getMoves(piece)){
//				if (!possibleMoves.contains(move))
//						possibleMoves.addLast(move);
//			}
//		}
//		
//		Move toMove = possibleMoves.get(getRandom(possibleMoves.size()));
//		try {
//			_game.movePiece(toMove.getPiece().getPosn(), toMove.getTo());
//		} catch (IllegalMoveException e) {
//			System.out.println(e.getMessage());
//			e.printStackTrace();
//		}
		
		
		
		SearchNode node;
		try {
			node = minimax(0, 0, _game.clone());
			if (node.getMove() != null)
				_game.movePiece(node.getMove().getPiece().getPosn(), node.getMove().getTo());
		} catch (CloneNotSupportedException | IllegalMoveException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		return true;
	}
	
	/**
	 * @param size - integer
	 * @return a number between [0, size)
	 */
	private int getRandom(int size){
		return (int)(Math.random() * ((size-1) + 1));
	}
	
	//TODO: player's equals method shouldn't depend on pieces. make only dependent on color
	// minimax(0, 3, game.clone())
	private SearchNode minimax(int currDepth, int maxDepth, DefaultBoardGame gameState) throws CloneNotSupportedException, IllegalMoveException{
		assert(maxDepth % 2 == 0);
		boolean maximizing = currDepth % 2 == 0;

		System.out.println(maximizing);
		//System.out.println(_game.getBoard().piecesToString());
		if (gameState.isGameOver()){
			return new SearchNode(null, estimateValue(gameState, this));
		} else {
			List<Move> possibleMoves = getPossibleMoves(gameState, gameState.getCurrentPlayer());

			SearchNode bestNode = null;
			double bestValue = (maximizing) ? Double.NEGATIVE_INFINITY : Double.POSITIVE_INFINITY;
			for (Move m: possibleMoves){
				DefaultBoardGame g = getGameState(m, gameState);
				SearchNode currNode = (currDepth == maxDepth) ? new SearchNode(m, estimateValue(g, getPlayer(g))) : minimax(currDepth + 1, maxDepth, g);
				if (maximizing &&currNode.getValue() > bestValue || // trying to maximize
				!maximizing && currNode.getValue() < bestValue){ // trying to minimize
					bestValue = currNode.getValue();
					bestNode = currNode;
				}
			}
			return bestNode;
		}
		
	}

	/**
	 * Gets a new, resulting Game instance given a move and a game
	 * @param m - a Move
	 * @param gameState - a Game
	 * @return - a clone of <gameState> after <m> has been made
	 * @throws CloneNotSupportedException
	 * @throws IllegalMoveException
	 */
	private DefaultBoardGame getGameState(Move m, DefaultBoardGame gameState) throws CloneNotSupportedException, IllegalMoveException{
		DefaultBoardGame copy = gameState.clone();
		copy.movePiece(m.getPiece().getPosn(), m.getTo());
		return copy;
	}
	
	/**
	 * Gets the player that corresponds to this AI given a Game
	 * @param gameState - a Game
	 * @return - the Player associated with this AI
	 */
	private Player getPlayer(DefaultBoardGame gameState){
		for (Player p: gameState.getPlayers()){
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
	private List<Move> getPossibleMoves(DefaultBoardGame gameState, Player player){
		List<Piece> pieces = player.getPieces();

		LinkedList<Move> possibleMoves = new LinkedList<Move>();
		
		for (Piece piece: pieces) {
			for (Move move : gameState.getBoard().getMoves(piece)){
				if (!possibleMoves.contains(move))
						possibleMoves.addLast(move);
			}
		}
		return possibleMoves;
	}
	

	/**
	 * Estimates the value of this game for the current player
	 * @return the value for this game for the current player
	 */
	public double estimateValue(DefaultBoardGame gameState, Player currPlayer){
		if (gameState.isGameOver()){
			if(gameState.getWinner().equals(currPlayer)) 
				return Double.POSITIVE_INFINITY;
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
	 * Calculating the value of each piece by its size and its elevation.
	 * The bigger the size/the higher the elevation, the better.
	 * 
	 * TODO: 
	 * 
	 * currently adding the two. maybe rethink that? Have 2 AIs play each other, one adding,
	 * one not later on.
	 * 
	 * NEED TO REWRITE!
	 * PROBLEM: big pieces in the beginning are encouraged to stay where they are if they are
	 * at a higher elevation. In general, also need to take into account proximity to other pieces
	 * + attack positions. But this might already be taken account for? Might have to search too deep
	 * to get to this point though. grasd;flkjasd;lfkjasd;lfkj;
	 */
	private double estimatePlayerValue(Player player){
		double toRet = 0;
		for (Piece p : player.getPieces()){
			double pieceValue = (p.getSize() + 1);
			toRet += pieceValue;
		}
		return toRet;
	}
	
}
