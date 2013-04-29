package terrace.gui;

import terrace.Variant;
import terrace.exception.IllegalMoveException;

public class Main {
	public static void main(String[] args) {
		try {
			int numHuman = 1;
			int numAI = 1;
			TerraceFrame frame = new TerraceFrame(numHuman, numAI, 8, Variant.STANDARD);
			frame.setVisible(true);
		} catch (IllegalMoveException e){
			System.out.println(e.getMessage());
		}
	}
}
