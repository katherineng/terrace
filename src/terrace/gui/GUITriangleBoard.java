package terrace.gui;

import java.util.*;

import terrace.Board;
import terrace.Piece;
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

	@Override
	public double getShiftFactor() {
		return 0;
	}

	@Override
	protected void setUpBoard() {
		//needed because translation is relative to center of shape, not the corner
		Board gameBoard = _panel._game.getBoard();
//		System.out.println("Elevations");
//		System.out.println(gameBoard.elevationsToString());
//		System.out.println("=====================================");
		for (int y = 0; y < gameBoard.getHeight(); y++){
			for (int x = 0; x < gameBoard.getWidth(); x++){
				Posn pos = new Posn(x, y);
				double height = gameBoard.getElevation(pos)/10.;
				System.out.println(gameBoard.elevationsToString());
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
	
	@Override
	public void resetPieces() {	
		_gamePieces.clear();
		//needed because translation is relative to center of shape, not the corner
		for (int row = 0; row < _panel._game.getBoard().getWidth(); row++)
			for (int col = 0; col < _panel._game.getBoard().getHeight(); col++){
				// set up _gamePiece
				Piece piece = _panel._game.getBoard().getPieceAt(new Posn(row,  col));
				if (piece != null) {
					_gamePieces.add(new TriangleGamePiece(this, piece));
				}
			}
	}
}
