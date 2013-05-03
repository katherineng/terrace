package terrace.gui;

import terrace.DefaultBoard;

public class GUIBoardFactory {
	public static GUIBoard create(GamePanel panel) {
		if (panel._game.getBoard() instanceof DefaultBoard) {
			return new GUISquareBoard(panel);
		} else {
			return new GUITriangleBoard(panel);
		}
	}
}
