package gui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.media.opengl.*;
import javax.media.opengl.glu.*;
import javax.vecmath.*;


import terrace.DefaultBoard;
import terrace.Game;
import terrace.Player;

public class GUIBoard extends Board {

	private RectPrism _foundation;
	private BoardPiece[][] _boardPieces;
	private ArrayList<GamePiece> _gamePieces;
	protected DefaultBoard _board;
	protected int _dimension;
	private GLUquadric _gluQuadric;
	HashMap<Player, Vector3d> _playerColors;
	private Game _game;

	public GUIBoard(GL2 gl, Game game){
		_game = game;
		GLU glu = new GLU();
		_foundation = new RectPrism(1.,.01,1.);
		_board = _game.getBoard();
		_dimension = _board.getDimensions();
		_boardPieces = new BoardPiece[_dimension][_dimension];
		_gamePieces = new ArrayList<GamePiece>();//[_dimension][_dimension];
		_gluQuadric = glu.gluNewQuadric();
		setUpColors();
		
		setUpBoard(gl);
	}
	
	public int getDimensions(){
		return _board.getDimensions();
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

	private void setUpBoard(GL2 gl){
		
		//needed because translation is relative to center of shape, not the corner
		double shiftFactor = 1.0/_board.getDimensions()/2;

		for (int row = 0; row < _dimension; row++){
			double rowShift = 1.0/_dimension*row;
			for (int col = 0; col < _dimension; col++){
				double colShift = 1.0/_dimension*col;

				double height =  _board.getElevation(row, col)/60.0;
				// set up _boardPiece
				BoardPiece piece = new BoardPiece(1.0/_dimension, height, 
						new Vector2d(.5 - shiftFactor - rowShift, .5 - shiftFactor - colShift));
				_boardPieces[row][col] = piece;

				// set up _gamePiece
				if (_board.getPiece(row,  col) != null) 
					_gamePieces.add(new GamePiece(gl, this, _board.getPiece(row,  col), height));
			}
		}
	}

	public ArrayList<GamePiece> getGamePieces(){
		return _gamePieces;
	}

	@Override
	public void draw(GL2 gl) {
		_foundation.draw(gl);	
		for (BoardPiece[] pieceArray: _boardPieces)
			for (BoardPiece piece: pieceArray)
				piece.draw(gl);

		for (GamePiece piece: _gamePieces)
			piece.draw(gl);
	}
}
