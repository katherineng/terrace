package terrace.gui;

import java.util.*;

import terrace.Board;
import terrace.util.Posn;

public class GUITriangleBoard extends GUIBoard {

	public GUITriangleBoard(GamePanel panel){
		_panel = panel;
		_foundation = new RectPrism(0.0, -0.01, 0.0, 1.0, 0.01, 1.0);
		_boardTiles = new BoardTile[_panel._game.getBoard().getHeight()][_panel._game.getBoard().getWidth()];
		_gamePieces = new ArrayList<GamePiece>();
		setUpColors();
		
		setUpBoard();
	}

	@Override
	public double getShiftFactor() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	protected void setUpBoard() {
		//needed because translation is relative to center of shape, not the corner
		Board gameBoard = _panel._game.getBoard();
		for (int col = 0; col < gameBoard.getHeight(); col++){
			for (int row = 0; row < gameBoard.getWidth(); row++){
				double height = getElevation(col, row);
				// set up _boardPiece
				Posn pos = gameBoard.new Posn(col, row);
				BoardTile tile = new TriangleTile(
						this,
						height,
						pos,
						gameBoard.getElevation(new Posn(col, row))
				);
				_boardTiles[col][row] = tile;
			}
		}
		
		resetPieces();
		
	}
}
