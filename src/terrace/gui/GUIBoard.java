package terrace.gui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.media.opengl.GL2;
import javax.vecmath.Vector3d;

import terrace.DefaultBoardGame;
import terrace.Player;
import terrace.PlayerColor;
import terrace.util.Posn;

public abstract class GUIBoard implements Drawable {
	protected RectPrism _foundation;				/** The foundation of the board **/
	protected BoardTile[][] _boardPieces;			/** A 2d Array of the Board tiles **/
	protected List<GamePiece> _gamePieces;	/** An array of the game pieces **/
	protected Map<PlayerColor, Vector3d> _playerColors;	/** Maps players to their colors **/
	protected GamePanel _panel;							/** a Game instance **/
	
	public double getElevation(Posn pos) {
		BoardTile b = _boardPieces[pos.x][pos.y];
		return b.getElevation();
	}
	
	public double getElevation(int col, int row) {
		return  _panel._game.getBoard().getElevation(new Posn(col, row))/60.;		
	}
	
	public int getDimensions() {
		return _panel._game.getBoard().getWidth();
	}
	
	public List<GamePiece> getGamePieces() {
		return _gamePieces;
	}
	
	public BoardTile posToTile(Posn pos) {
		return _boardPieces[pos.getX()][pos.getY()];
	}
	
	public Vector3d getPlayerColors(PlayerColor playerColor) {
		return _playerColors.get(playerColor);
	}
	
	public List<BoardTile> getBoardPieces() {
		LinkedList<BoardTile> toRet = new LinkedList<BoardTile>();
		for (int i = 0; i < _boardPieces.length; i++)
			for (int j = 0; j < _boardPieces[0].length; j++)
				toRet.addLast(_boardPieces[i][j]);
		return toRet;
	}
	
	public void resetPieces() {	
		int dimension = _panel._game.getBoard().getWidth();
		_gamePieces.clear();
		//needed because translation is relative to center of shape, not the corner
		for (int row = 0; row < dimension; row++)
			for (int col = 0; col < dimension; col++)
				// set up _gamePiece
				if (_panel._game.getBoard().getPieceAt(new Posn(col,  row)) != null) 
					_gamePieces.add(new GamePiece(this, _panel._game.getBoard().getPieceAt(new Posn(col,  row))));
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
	
	protected void setUpBoard() {
		int dimension = _panel._game.getBoard().getWidth();
		//needed because translation is relative to center of shape, not the corner
		for (int row = 0; row < dimension; row++){
			for (int col = 0; col < dimension; col++){
				double height = getElevation(col, row);
				// set up _boardPiece
				Posn pos = new Posn(col, row);
				BoardTile piece = new BoardTile(
						this,
						height,
						pos,
						_panel._game.getBoard().getElevation(new Posn(col, row))
				);
				_boardPieces[col][row] = piece;
			}
		}
		
		resetPieces();
	}
	
	@Override
	public void draw(GL2 gl) {
		_foundation.draw(gl);	
		for (BoardTile[] pieceArray: _boardPieces)
			for (BoardTile piece: pieceArray)
				piece.draw(gl);
		
		for (GamePiece piece: _gamePieces)
			piece.draw(gl);
	}
}
