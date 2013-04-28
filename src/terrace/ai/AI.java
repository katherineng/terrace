package terrace.ai;

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
		
		Piece pieceToMove = pieces.get(getRandom(pieces.size()));
		List<Posn> moves = _game.getBoard().getMoves(pieceToMove);
		
		while(moves.isEmpty()){
			pieceToMove = pieces.get(getRandom(pieces.size())); 
			moves = _game.getBoard().getMoves(pieceToMove);			
		}
		
		Posn posnToMove = moves.get(getRandom(moves.size()));
		try {
			_game.movePiece(pieceToMove.getPosn(), posnToMove);
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