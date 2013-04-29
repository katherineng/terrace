package terrace;

import java.util.*;

import org.junit.Test;

import terrace.exception.IllegalMoveException;

public class GameTest {

	@Test
	public void test() throws IllegalMoveException {
		Game twoPlayer = new Game(2, 0, 8, Variant.STANDARD);
		twoPlayer.setUpPieces();
		
		Game fourPlayer = new Game(4, 0, 8, Variant.STANDARD);
		fourPlayer.setUpPieces();
		
//		System.out.println(twoPlayer.getBoard().piecesToString());
//		System.out.println(fourPlayer.getBoard().piecesToString());
		
//		DefaultBoard twoPlayerBoard = twoPlayer.getBoard();
//		Set<Posn> moves = twoPlayerBoard.getMoves(twoPlayerBoard.getPiece(0, 1));
//		for (Posn p : moves) {
//			System.out.println(p);
//		}
		
		
//		twoPlayer.movePiece(new Posn(0, 1), new Posn(0, 2));
//		System.out.println(twoPlayer.getBoard().getPiece(0, 2));
//		
//		Set<Piece> pieces = twoPlayer.getPlayers().get(0).getPieces();
//		for (Piece p : pieces) {
//			System.out.println(p);
//		}
		
		Game downhill = new Game(2, 0, 8, Variant.DOWNHILL);
		downhill.setUpPieces();
		
		List<Move> moves = downhill.getBoard().getMoves(downhill.getBoard().getPiece(7,  1));
		for (Move m : moves) {
			System.out.println(m);
		}
		
	}

}
