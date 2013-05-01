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
		SearchNode node;
		try {
			node = minimax(0, 0, _game.clone());
			System.out.println(node.getMove());
			_game.movePiece(node.getMove().getPiece().getPosn(), node.getMove().getTo());
		} catch (CloneNotSupportedException | IllegalMoveException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		return true;
	}
	
	
	//TODO: player's equals method shouldn't depend on pieces. make only dependent on color
	// minimax(0, 3, game.clone())
	private SearchNode minimax(int currDepth, int maxDepth, DefaultBoardGame gameState) throws CloneNotSupportedException, IllegalMoveException{
		assert(maxDepth % 2 == 0);
		boolean maximizing = currDepth % 2 == 0;

		//System.out.println(_game.getBoard().piecesToString());
		List<Move> possibleMoves = getPossibleMoves(gameState, gameState.getCurrentPlayer());

		SearchNode bestNode = null;
		double bestValue = (maximizing) ? Double.NEGATIVE_INFINITY : Double.POSITIVE_INFINITY;
		for (Move m: possibleMoves){
			DefaultBoardGame g = getGameState(m, gameState);
			SearchNode currNode = (currDepth == maxDepth || g.isGameOver()) ? new SearchNode(m, estimateValue(g, getPlayer(g))) : minimax(currDepth + 1, maxDepth, g);
			if (maximizing &&currNode.getValue() > bestValue || // trying to maximize
					!maximizing && currNode.getValue() < bestValue){ // trying to minimize
				bestValue = currNode.getValue();
				bestNode = currNode;
			}
		}
		return bestNode;

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
		copy.movePieceAI(m.getPiece().getPosn(), m.getTo());
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
	 * Estimates the value of this game for the current player, by taking the difference
	 * between the current player value and the opposing player's value
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
	 * Calculating the value of each piece by its size.
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
