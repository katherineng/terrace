package terrace.gui;

import java.util.ArrayList;

public class GUISquareBoard extends GUIBoard {
	public GUISquareBoard(GamePanel panel) {
		_panel = panel;
		_foundation = new RectPrism(0.0, -0.01, 0.0, 1.0, 0.01, 1.0);
		int dimension = _panel._game.getBoard().getWidth();
		_boardPieces = new BoardTile[dimension][dimension];
		_gamePieces = new ArrayList<GamePiece>();
		setUpColors();
		
		setUpBoard();
	}
}
