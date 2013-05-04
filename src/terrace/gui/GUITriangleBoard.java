package terrace.gui;

import java.util.*;

import terrace.Board;
import terrace.util.Posn;

public class GUITriangleBoard extends GUIBoard {

	public GUITriangleBoard(GamePanel panel){
		_panel = panel;
		_foundation = new RectPrism(0.0, -0.01, 0.0, 1.0, 0.01, 1.0);
		assert _panel._game.getBoard().getHeight() > _panel._game.getBoard().getWidth();
		_boardTiles = new BoardTile[_panel._game.getBoard().getWidth()][_panel._game.getBoard().getHeight()];
		_gamePieces = new ArrayList<GamePiece>();
		setUpColors();
		
		setUpBoard();
	}
	
	private boolean orientedDown(Posn pos){
		if (pos.y % 2 == 0) return true;
		return false;
	}

	@Override
	double getRowShift(Posn p){
		double toRet = 1.0/getWidth()*p.x;;
		if (!orientedDown(p)) toRet +=  1.0/getWidth();
		return toRet;
	}

	@Override
	double getColShift(Posn p){
		double toRet = 1.0/getWidth()/2*p.y;
		if (!orientedDown(p)) toRet +=  1.0/getWidth();
		return toRet;
	}
	
	@Override
	public double getShiftFactor() {
		return 0;
	}

	@Override
	protected void setUpBoard() {
		//needed because translation is relative to center of shape, not the corner
		Board gameBoard = _panel._game.getBoard();
		for (int y = 0; y < gameBoard.getHeight(); y++){
			for (int x = 0; x < gameBoard.getWidth(); x++){
				Posn pos = new Posn(x, y);
				double height = gameBoard.getElevation(pos)/30.;
				// set up _boardPiece
				BoardTile tile = new TriangleTile(
						this,
						height,
						pos,
						gameBoard.getElevation(pos)
				);
				_boardTiles[x][y] = tile;
			}
		}
		
		resetPieces();
		
	}
}
