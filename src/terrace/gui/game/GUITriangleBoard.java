package terrace.gui.game;

import java.util.*;

import terrace.Board;
import terrace.Piece;
import terrace.util.Posn;

public class GUITriangleBoard extends GUIBoard {

	public GUITriangleBoard(GamePanel panel){
		_panel = panel;
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
				double height = gameBoard.getElevation(pos)/10.;
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
		synchronized (_gamePieces) {
			_gamePieces.clear();
			
			//needed because translation is relative to center of shape, not the corner
			for (int row = 0; row < _panel._game.getBoard().getWidth(); row++) {
				for (int col = 0; col < _panel._game.getBoard().getHeight(); col++){
					// set up _gamePiece
					Piece piece = _panel._game.getBoard().getPieceAt(new Posn(row,  col));
					if (piece != null) {
						_gamePieces.add(new TriangleGamePiece(this, piece));
					}
				}
			}
		}
	}
}
