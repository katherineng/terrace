package terrace;

import static org.junit.Assert.*;
import java.util.*;
import org.junit.Test;


public class BoardFactoryTest {

	@Test
	public void triangleBoardTest() {
		
		List<Player> players = new ArrayList<>();
		players.add(new LocalPlayer(PlayerColor.PINK));
		players.add(new LocalPlayer(PlayerColor.BLUE));
		Board board = BoardFactory.create(players, 4, Variant.TRIANGLE);
		
		assertTrue(board instanceof TriangleBoard);
		
		String b = "[ (BLUE, 3)	(BLUE, 2)	(BLUE, 1)	(BLUE, 0)	]\n" +
				   "[ (BLUE, 3)	(BLUE, 2)	(BLUE, 1)	(BLUE, T)	]\n" +
				   "[ (..........)	(..........)	(..........)	(..........)	]\n" +
				   "[ (..........)	(..........)	(..........)	(..........)	]\n" +
				   "[ (..........)	(..........)	(..........)	(..........)	]\n" +
				   "[ (..........)	(..........)	(..........)	(..........)	]\n" +
				   "[ (PINK, 0)	(PINK, 1)	(PINK, 2)	(PINK, 3)	]\n" +
				   "[ (PINK, T)	(PINK, 1)	(PINK, 2)	(PINK, 3)	]\n";
		
		assertTrue(board.piecesToString().equals(b));
	}
	
	@Test
	public void defaultBoard8x8() {
		List<Player> players = new ArrayList<>();
		players.add(new LocalPlayer(PlayerColor.PINK));
		players.add(new LocalPlayer(PlayerColor.BLUE));
		Board board = BoardFactory.create(players, 8, Variant.STANDARD);
		
		assertTrue(board instanceof DefaultBoard);
		
		String b = "[ (BLUE, 3)	(BLUE, 3)	(BLUE, 2)	(BLUE, 2)	(BLUE, 1)	(BLUE, 1)	(BLUE, 0)	(BLUE, T)	]\n" +
				   "[ (BLUE, 0)	(BLUE, 0)	(BLUE, 1)	(BLUE, 1)	(BLUE, 2)	(BLUE, 2)	(BLUE, 3)	(BLUE, 3)	]\n" +
				   "[ (..........)	(..........)	(..........)	(..........)	(..........)	(..........)	(..........)	(..........)	]\n" +
				   "[ (..........)	(..........)	(..........)	(..........)	(..........)	(..........)	(..........)	(..........)	]\n" +
				   "[ (..........)	(..........)	(..........)	(..........)	(..........)	(..........)	(..........)	(..........)	]\n" +
				   "[ (..........)	(..........)	(..........)	(..........)	(..........)	(..........)	(..........)	(..........)	]\n" +
				   "[ (PINK, 3)	(PINK, 3)	(PINK, 2)	(PINK, 2)	(PINK, 1)	(PINK, 1)	(PINK, 0)	(PINK, 0)	]\n" +
				   "[ (PINK, T)	(PINK, 0)	(PINK, 1)	(PINK, 1)	(PINK, 2)	(PINK, 2)	(PINK, 3)	(PINK, 3)	]\n";
		
		assertTrue(board.piecesToString().equals(b));
	}
	
	@Test
	public void defaultBoard8x84P() {
		List<Player> players = new ArrayList<>();
		players.add(new LocalPlayer(PlayerColor.PINK));
		players.add(new LocalPlayer(PlayerColor.BLUE));
		players.add(new LocalPlayer(PlayerColor.GREEN));
		players.add(new LocalPlayer(PlayerColor.YELLOW));
		Board board = BoardFactory.create(players, 8, Variant.STANDARD);
		
		assertTrue(board instanceof DefaultBoard);
		
		String b = "[ (..........)	(GREEN, 3)	(GREEN, 2)	(GREEN, 2)	(GREEN, 1)	(GREEN, 1)	(GREEN, T)	(..........)	]\n" +
				   "[ (BLUE, 3)	(..........)	(..........)	(..........)	(..........)	(..........)	(..........)	(YELLOW, T)	]\n" +
				   "[ (BLUE, 2)	(..........)	(..........)	(..........)	(..........)	(..........)	(..........)	(YELLOW, 1)	]\n" +
				   "[ (BLUE, 2)	(..........)	(..........)	(..........)	(..........)	(..........)	(..........)	(YELLOW, 1)	]\n" +
				   "[ (BLUE, 1)	(..........)	(..........)	(..........)	(..........)	(..........)	(..........)	(YELLOW, 2)	]\n" +
				   "[ (BLUE, 1)	(..........)	(..........)	(..........)	(..........)	(..........)	(..........)	(YELLOW, 2)	]\n" +
				   "[ (BLUE, T)	(..........)	(..........)	(..........)	(..........)	(..........)	(..........)	(YELLOW, 3)	]\n" +
				   "[ (..........)	(PINK, T)	(PINK, 1)	(PINK, 1)	(PINK, 2)	(PINK, 2)	(PINK, 3)	(..........)	]\n";
		
		assertTrue(board.piecesToString().equals(b));
	}
	
	@Test
	public void defaultBoard6x6() {
		List<Player> players = new ArrayList<>();
		players.add(new LocalPlayer(PlayerColor.PINK));
		players.add(new LocalPlayer(PlayerColor.BLUE));
		Board board = BoardFactory.create(players, 6, Variant.STANDARD);
		
		assertTrue(board instanceof DefaultBoard);
		
		String b = "[ (BLUE, 2)	(BLUE, 2)	(BLUE, 1)	(BLUE, 1)	(BLUE, 0)	(BLUE, T)	]\n" +
				   "[ (BLUE, 0)	(BLUE, 0)	(BLUE, 1)	(BLUE, 1)	(BLUE, 2)	(BLUE, 2)	]\n" +
				   "[ (..........)	(..........)	(..........)	(..........)	(..........)	(..........)	]\n" +
				   "[ (..........)	(..........)	(..........)	(..........)	(..........)	(..........)	]\n" +
				   "[ (PINK, 2)	(PINK, 2)	(PINK, 1)	(PINK, 1)	(PINK, 0)	(PINK, 0)	]\n" +
				   "[ (PINK, T)	(PINK, 0)	(PINK, 1)	(PINK, 1)	(PINK, 2)	(PINK, 2)	]\n";
		assertTrue(board.piecesToString().equals(b));
	}
	
	@Test
	public void defaultBoard6x64P() {
		List<Player> players = new ArrayList<>();
		players.add(new LocalPlayer(PlayerColor.PINK));
		players.add(new LocalPlayer(PlayerColor.BLUE));
		players.add(new LocalPlayer(PlayerColor.GREEN));
		players.add(new LocalPlayer(PlayerColor.YELLOW));
		Board board = BoardFactory.create(players, 6, Variant.STANDARD);
		
		assertTrue(board instanceof DefaultBoard);
		
		String b = "[ (..........)	(GREEN, 2)	(GREEN, 1)	(GREEN, 1)	(GREEN, T)	(..........)	]\n" +
				   "[ (BLUE, 2)	(..........)	(..........)	(..........)	(..........)	(YELLOW, T)	]\n" +
				   "[ (BLUE, 1)	(..........)	(..........)	(..........)	(..........)	(YELLOW, 1)	]\n" +
				   "[ (BLUE, 1)	(..........)	(..........)	(..........)	(..........)	(YELLOW, 1)	]\n" +
				   "[ (BLUE, T)	(..........)	(..........)	(..........)	(..........)	(YELLOW, 2)	]\n" +
				   "[ (..........)	(PINK, T)	(PINK, 1)	(PINK, 1)	(PINK, 2)	(..........)	]\n";
		assertTrue(board.piecesToString().equals(b));
	}
	
	@Test
	public void size0() {
		List<Player> players = new ArrayList<>();
		players.add(new LocalPlayer(PlayerColor.PINK));
		players.add(new LocalPlayer(PlayerColor.BLUE));
		Board board = BoardFactory.create(players, 0, Variant.STANDARD);
		
		assertTrue(board instanceof DefaultBoard);
	}

}
