package terrace.ai;

import terrace.Variant;
import terrace.exception.IllegalMoveException;
import terrace.gui.TerraceFrame;

public class TestMain {

	public static void main(String[] args) throws IllegalMoveException {
		TerraceFrame frame = new TerraceFrame(1, 1, 8, Variant.STANDARD);
		frame.setVisible(true);
	}
}
