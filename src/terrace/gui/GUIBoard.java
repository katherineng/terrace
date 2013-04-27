package terrace.gui;

import java.util.*;

import javax.media.opengl.*;
import javax.vecmath.*;

import terrace.Game;
import terrace.Player;
import terrace.Posn;

public class GUIBoard extends Board {

	private RectPrism _foundation;				/** The foundation of the board **/
	private BoardTile[][] _boardPieces;			/** A 2d Array of the Board tiles **/
	private ArrayList<GamePiece> _gamePieces;	/** An array of the game pieces **/
	HashMap<Player, Vector3d> _playerColors;	/** Maps players to their colors **/
	HashMap<Posn, BoardTile> _posnToTile;		/** Maps positions to BoardTiles **/
	private Game _game;							/** a Game instance **/
	GL2 gl;

	public GUIBoard(GL2 gl, Game game){
		_game = game;
		_foundation = new RectPrism(1.,.01,1.);
		int dimension = _game.getBoard().getDimensions();
		_boardPieces = new BoardTile[dimension][dimension];
		_gamePieces = new ArrayList<GamePiece>();
		setUpColors();
		this.gl = gl;
		
		_posnToTile = new HashMap<Posn, BoardTile>();
		setUpBoard();
	}
	
	public int getDimensions(){
		return _game.getBoard().getDimensions();
	}
	
	public BoardTile posToTile(Posn x){
		assert(_posnToTile.containsKey(x));
		return _posnToTile.get(x);
	}
	
	public List<BoardTile> getBoardPieces(){
		LinkedList<BoardTile> toRet = new LinkedList<BoardTile>();
		for (int i = 0; i < _boardPieces.length; i++)
			for (int j = 0; j < _boardPieces[0].length; j++)
				toRet.addLast(_boardPieces[i][j]);
		return toRet;
	}

	public void resetPieces() {	
		int dimension = _game.getBoard().getDimensions();
		_gamePieces.clear();
		//needed because translation is relative to center of shape, not the corner
		for (int row = 0; row < dimension; row++)
			for (int col = 0; col < dimension; col++)
				// set up _gamePiece
				if (_game.getBoard().getPiece(col,  row) != null) 
					_gamePieces.add(new GamePiece(gl, this, _game.getBoard().getPiece(col,  row)));
	}
	
	/**
	 * Sets up colors for players
	 */
	private void setUpColors(){
		_playerColors = new HashMap<Player, Vector3d>();
		
		List<Player> players =  _game.getPlayers();
		for (int i = 0; i < players.size(); i++){
			switch(i){
			case 0:
				_playerColors.put(players.get(i), new Vector3d(51/255., 255/255., 204/255.));
				break;
			case 1:
				_playerColors.put(players.get(i), new Vector3d(255/255., 51/255., 102/255.));
				break;
			case 2:
				_playerColors.put(players.get(i), new Vector3d(245/255.,184/255.,0/255.));
				break;
			case 3:
				_playerColors.put(players.get(i), new Vector3d(38/255.,153/255., 0/255.));
				break;
			default: //shouldn't get here
				assert(false);
			}
		}
	}

	public double getElevation(int col, int row){
		return  _game.getBoard().getElevation(col, row)/60.;		
	}
	
	private void setUpBoard(){
		
		int dimension = _game.getBoard().getDimensions();
		//needed because translation is relative to center of shape, not the corner
		for (int row = 0; row < dimension; row++){
			for (int col = 0; col < dimension; col++){
				double height = getElevation(col, row);
				// set up _boardPiece
				Posn pos = new Posn(col, row);
				BoardTile piece = new BoardTile(this, height, pos, _game.getBoard().getElevation(col, row));
				_boardPieces[col][row] = piece;
				_posnToTile.put(pos, piece);
			}
		}
		
		resetPieces();
	}

	public ArrayList<GamePiece> getGamePieces(){
		return _gamePieces;
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

	public double getElevation(Posn pos) {
		BoardTile b = _boardPieces[pos.y][pos.x];
		return b.getElevation();
	}
}
