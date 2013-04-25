package terrace;

import static org.junit.Assert.*;

import org.junit.Test;

public class GameTest {

	@Test
	public void test() {
		Game twoPlayer = new Game(2, 8, Variant.STANDARD);
		twoPlayer.setUpPieces();
		
		Game fourPlayer = new Game(4, 8, Variant.STANDARD);
		fourPlayer.setUpPieces();
		
		System.out.println(twoPlayer.getBoard().piecesToString());
		System.out.println(fourPlayer.getBoard().piecesToString());
	}

}
