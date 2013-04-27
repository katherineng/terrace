package terrace.gui;

import terrace.Variant;
import terrace.exception.IllegalMoveException;

public class Main {
	public static void main(String[] args) {
		try {
			TerraceFrame frame = new TerraceFrame(1, 1, 8, Variant.STANDARD);
			frame.setVisible(true);
		} catch (IllegalMoveException e){
			System.out.println(e.getMessage());
		}
	}
}
