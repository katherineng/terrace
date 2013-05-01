package terrace;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import terrace.util.Posn;

public class DefaultBoardTest {
	private DefaultBoard _board;
	
	@Before
	public void setUp() {
		_board = new DefaultBoard(8, Variant.STANDARD);
		_board.setUp();
	}

	@Test
	public void elevations8x8() {
		String expected = "[ 7 6 5 4 3 2 1 0 ]\n" +
						  "[ 6 6 5 4 3 2 1 1 ]\n" +
						  "[ 5 5 5 4 3 2 2 2 ]\n" +
						  "[ 4 4 4 4 3 3 3 3 ]\n" +
						  "[ 3 3 3 3 4 4 4 4 ]\n" +
						  "[ 2 2 2 3 4 5 5 5 ]\n" +
						  "[ 1 1 2 3 4 5 6 6 ]\n" +
						  "[ 0 1 2 3 4 5 6 7 ]\n";
		
		assertTrue(_board.elevationsToString().equals(expected));
	}
	
	@Test
	public void elevations2x2() {
		DefaultBoard board2x2 = new DefaultBoard(2, Variant.STANDARD);
		board2x2.setUp();
		
		String expected = "[ 1 0 ]\n[ 0 1 ]\n";
		
		assertTrue(board2x2.elevationsToString().equals(expected));
	}
	
	@Test
	public void terraceCorner() {
		List<Posn> terrace = _board.getTerracePosns(new Posn(0, 0));
		
		assertTrue(terrace.size() == 1);
		assertTrue(terrace.get(0).equals(new Posn(0, 0)));
	}
	
	@Test
	public void bottomLeftQuad() {
		List<Posn> terrace = _board.getTerracePosns(new Posn(3, 1));
		
		assertTrue(terrace.size() == 7);
		
		List<Posn> expected = new ArrayList<>();
		expected.add(new Posn(0, 3));
		expected.add(new Posn(1, 3));
		expected.add(new Posn(2, 3));
		expected.add(new Posn(3, 3));
		expected.add(new Posn(3, 2));
		expected.add(new Posn(3, 1));
		expected.add(new Posn(3, 0));
		
		for (int i = 0; i < 7; i++) {
			assertTrue(terrace.get(i).equals(expected.get(i)));
		}
	}
	
	@Test
	public void bottomRightQuad() {
		List<Posn> terrace = _board.getTerracePosns(new Posn(0, 6));
		
		assertTrue(terrace.size() == 3);
		
		List<Posn> expected = new ArrayList<>();
		expected.add(new Posn(0, 6));
		expected.add(new Posn(1, 6));
		expected.add(new Posn(1, 7));
		
		for (int i = 0; i < 3; i++) {
			assertTrue(terrace.get(i).equals(expected.get(i)));
		}
	}
	
	@Test
	public void topLeftQuad() {
		List<Posn> terrace = _board.getTerracePosns(new Posn(5, 2));
		
		assertTrue(terrace.size() == 5);
		
		List<Posn> expected = new ArrayList<>();
		expected.add(new Posn(5, 0));
		expected.add(new Posn(5, 1));
		expected.add(new Posn(5, 2));
		expected.add(new Posn(6, 2));
		expected.add(new Posn(7, 2));
		
		for (int i = 0; i < 5; i++) {
			assertTrue(terrace.get(i).equals(expected.get(i)));
		}
	}
	
	@Test
	public void topRightQuad() {
		List<Posn> terrace = _board.getTerracePosns(new Posn(4, 4));
		
		assertTrue(terrace.size() == 7);
		
		List<Posn> expected = new ArrayList<>();
		expected.add(new Posn(4, 7));
		expected.add(new Posn(4, 6));
		expected.add(new Posn(4, 5));
		expected.add(new Posn(4, 4));
		expected.add(new Posn(5, 4));
		expected.add(new Posn(6, 4));
		expected.add(new Posn(7, 4));
		
		for (int i = 0; i < 7; i++) {
			assertTrue(terrace.get(i).equals(expected.get(i)));
		}
	}

}
