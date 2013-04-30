package terrace.ai;

import java.util.LinkedList;
import java.util.List;

import terrace.*;
import terrace.exception.IllegalMoveException;

public class AI extends Player {
	Game _game;
		
	public AI(PlayerColor color, Game game) {
		super(color);
		_game = game;
	}

	
	@Override
	public boolean makeMove() {
		List<Piece> pieces = getPieces();
		assert(pieces.size() > 0);
		LinkedList<Move> possibleMoves = new LinkedList<Move>();
		
		for (Piece piece: pieces) {
			for (Move move : _game.getBoard().getMoves(piece)){
				if (!possibleMoves.contains(move))
						possibleMoves.addLast(move);
			}
		}
		
		Move toMove = possibleMoves.get(getRandom(possibleMoves.size()));
		try {
			_game.movePiece(toMove.getPiece().getPosn(), toMove.getTo());
		} catch (IllegalMoveException e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
		
		
		
		
//		SearchNode node;
//		System.out.println(_game.getBoard().piecesToString());
//		try {
//			node = minimax(0, 3, _game);
//			_game.movePiece(node.getMove().getPiece().getPosn(), node.getMove().getTo());
//		} catch (CloneNotSupportedException | IllegalMoveException e1) {
//			// TODO Auto-generated catch block
//			e1.printStackTrace();
//		}
		
		
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
	private SearchNode minimax(int currDepth, int maxDepth, Game gameState) throws CloneNotSupportedException, IllegalMoveException{
		assert(maxDepth % 2 == 0);
		boolean maximizing = currDepth % 2 == 0;

		System.out.println(_game.getBoard().piecesToString());
		if (gameState.isGameOver()){
			return new SearchNode(null, gameState.estimateValue(this));
		} else {
			List<Move> possibleMoves = getPossibleMoves(gameState, gameState.getCurrentPlayer());
			
			SearchNode bestNode = null;
			double bestValue = (maximizing) ? Double.NEGATIVE_INFINITY : Double.POSITIVE_INFINITY;
			for (Move m: possibleMoves){
				Game g = getGameState(m, gameState);
				SearchNode currNode = (currDepth == maxDepth) ? new SearchNode(m, g.estimateValue(this)) : minimax(currDepth + 1, maxDepth, g);
				if (maximizing &&currNode.getValue() > bestValue || // trying to maximize
				!maximizing && currNode.getValue() < bestValue){ // trying to minimize
					bestValue = currNode.getValue();
					bestNode = currNode;
				}
			}
			return bestNode;
		}
		
	}

	private Game getGameState(Move m, Game gameState) throws CloneNotSupportedException, IllegalMoveException{
		Game copy = gameState.clone();
		copy.movePiece(m.getPiece().getPosn(), m.getTo());
		return copy;
	}
	
	
	private List<Move> getPossibleMoves(Game gameState, Player player){
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
	
	private static class SearchNode{
		private Move _move;
		private double _value;
		
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
	
}
