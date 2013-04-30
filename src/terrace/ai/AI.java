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
		return true;
	}
	
	/**
	 * @param size - integer
	 * @return a number between [0, size)
	 */
	private int getRandom(int size){
		return (int)(Math.random() * ((size-1) + 1));
	}
}