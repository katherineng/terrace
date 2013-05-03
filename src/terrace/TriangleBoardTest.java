package terrace;

import static org.junit.Assert.*;

import org.junit.Test;

public class TriangleBoardTest {
	private TriangleBoard _board4x4;
	private TriangleBoard _board3x3;
	
	@Test
	public void setUp4x4Test() {
		_board4x4 = new TriangleBoard(4);
		_board4x4.setUp();
		
		String expected = "[ 6 4 2 0 ]\n" +
						  "[ 6 5 3 1 ]\n" +
						  "[ 5 4 2 2 ]\n" +
						  "[ 4 4 3 3 ]\n" +
						  "[ 3 3 4 4 ]\n" +
						  "[ 2 2 4 5 ]\n" +
						  "[ 1 3 5 6 ]\n" +
						  "[ 0 2 4 6 ]\n";

		
		String elevations = _board4x4.elevationsToString();
		assertEquals(expected, elevations);
	}

	@Test
	public void setUp3x3Test() {
		_board3x3 = new TriangleBoard(3);
		_board3x3.setUp();
		
		String expected = "[ 4 2 0 ]\n" +
						  "[ 4 3 1 ]\n" +
						  "[ 3 2 2 ]\n" +
						  "[ 2 2 3 ]\n" +
						  "[ 1 3 4 ]\n" +
						  "[ 0 2 4 ]\n";
		
		String elevations = _board3x3.elevationsToString();
		assertEquals(expected, elevations);
	}
}
