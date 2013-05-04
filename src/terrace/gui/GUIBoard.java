package terrace.gui;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.media.opengl.GL2;
import javax.vecmath.Vector3d;

import terrace.Piece;
import terrace.Player;
import terrace.PlayerColor;
import terrace.util.Posn;

public abstract class GUIBoard implements Drawable {
	protected RectPrism _foundation;				/** The foundation of the board **/
	protected BoardTile[][] _boardTiles;			/** A 2d Array of the Board tiles **/
	protected List<GamePiece> _gamePieces;			/** An array of the game pieces **/
	protected Map<PlayerColor, Vector3d> _playerColors;	/** Maps players to their colors **/
	protected GamePanel _panel;							/** a Game instance **/
	
	public double getElevation(Posn pos) {
		BoardTile b = _boardTiles[pos.x][pos.y];
		return b.getElevation();
	}
	
	public double getElevation(int col, int row) {
		return  _panel._game.getBoard().getElevation(new Posn(col, row))/60.;		
	}
	
	public abstract double getShiftFactor();
	abstract double getRowShift(Posn p);
	abstract double getColShift(Posn p);

	public double getHeight() {
		return _panel._game.getBoard().getHeight();
	}
	
	public double getWidth() {
		return _panel._game.getBoard().getWidth();
	}

	public List<GamePiece> getGamePieces() {
		return _gamePieces;
	}
	
	public BoardTile posToTile(Posn pos) {
		return _boardTiles[pos.getX()][pos.getY()];
	}
	
	public Vector3d getPlayerColors(PlayerColor playerColor) {
		return _playerColors.get(playerColor);
	}
	
	public List<BoardTile> getBoardPieces() {
		LinkedList<BoardTile> toRet = new LinkedList<BoardTile>();
		for (int i = 0; i < _boardTiles.length; i++)
			for (int j = 0; j < _boardTiles[0].length; j++)
				toRet.addLast(_boardTiles[i][j]);
		return toRet;
	}
	
	public void resetPieces() {	
		_gamePieces.clear();
		//needed because translation is relative to center of shape, not the corner
		for (int row = 0; row < _panel._game.getBoard().getWidth(); row++)
			for (int col = 0; col < _panel._game.getBoard().getHeight(); col++){
				// set up _gamePiece
				Piece piece = _panel._game.getBoard().getPieceAt(new Posn(row,  col));
				if (piece != null) 
					_gamePieces.add(new GamePiece(this, piece));
			}
	}
	
	/**
	 * Sets up colors for players
	 */
	protected void setUpColors() {
		_playerColors = new HashMap<PlayerColor, Vector3d>();
		
		List<Player> players =  _panel._game.getPlayers();
		for (int i = 0; i < players.size(); i++){
			switch(i){
			case 0:
				_playerColors.put(players.get(i).getColor(), new Vector3d(51/255., 255/255., 204/255.));
				break;
			case 1:
				_playerColors.put(players.get(i).getColor(), new Vector3d(255/255., 51/255., 102/255.));
				break;
			case 2:
				_playerColors.put(players.get(i).getColor(), new Vector3d(245/255.,184/255.,0/255.));
				break;
			case 3:
				_playerColors.put(players.get(i).getColor(), new Vector3d(38/255.,153/255., 0/255.));
				break;
			default: //shouldn't get here
				assert(false);
			}
		}
	}
	
	abstract protected void setUpBoard();
	
	@Override
	public void draw(GL2 gl) {
		_foundation.draw(gl);	
		for (BoardTile[] tileArray : _boardTiles)
			for (BoardTile tile : tileArray) tile.draw(gl);
		
		for (GamePiece piece : _gamePieces) piece.draw(gl);
	}
}
