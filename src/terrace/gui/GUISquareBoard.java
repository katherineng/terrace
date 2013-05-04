package terrace.gui;

import java.util.ArrayList;

import terrace.Piece;
import terrace.util.Posn;

public class GUISquareBoard extends GUIBoard {
	public GUISquareBoard(GamePanel panel) {
		_panel = panel;
		_foundation = new RectPrism(0.0, -0.01, 0.0, 1.0, 0.01, 1.0);
		int dimension = _panel._game.getBoard().getWidth();
		_boardTiles = new BoardTile[dimension][dimension];
		_gamePieces = new ArrayList<GamePiece>();
		setUpColors();
		
		setUpBoard();
	}

	@Override
	public double getShiftFactor() {
		return 1.0/_panel._game.getBoard().getWidth()/2;
	}
	
	@Override
	protected void setUpBoard() {
		//needed because translation is relative to center of shape, not the corner
		for (int row = 0; row < _panel._game.getBoard().getWidth(); row++){
			for (int col = 0; col < _panel._game.getBoard().getHeight(); col++){
				double height = getElevation(row, col);
				// set up _boardPiece
				Posn pos = new Posn(row, col);
				BoardTile tile = new SquareTile(
						this,
						height,
						pos,
						_panel._game.getBoard().getElevation(pos)
				);
				_boardTiles[row][col] = tile;
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
					_gamePieces.add(new SquareGamePiece(this, piece));
				}
			}
	}
}
