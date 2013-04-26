package terrace.gui;

import java.util.*;

import javax.media.opengl.*;
import javax.vecmath.*;


import terrace.DefaultBoard;
import terrace.Game;
import terrace.Player;
import terrace.Posn;

public class GUIBoard extends Board {

	private RectPrism _foundation;
	private BoardPiece[][] _boardPieces;
	private ArrayList<GamePiece> _gamePieces;
	protected DefaultBoard _board;
	protected int _dimension;
	HashMap<Player, Vector3d> _playerColors;
	private Game _game;

	public GUIBoard(GL2 gl, Game game){
		_game = game;
		_foundation = new RectPrism(1.,.01,1.);
		_board = _game.getBoard();
		_dimension = _board.getDimensions();
		_boardPieces = new BoardPiece[_dimension][_dimension];
		_gamePieces = new ArrayList<GamePiece>();
		setUpColors();
		
		setUpBoard(gl);
	}
	
	public int getDimensions(){
		return _board.getDimensions();
	}
	
	public List<BoardPiece> getBoardPieces(){
		LinkedList<BoardPiece> toRet = new LinkedList<BoardPiece>();
		for (int i = 0; i < _boardPieces.length; i++)
			for (int j = 0; j < _boardPieces[0].length; j++)
				toRet.addLast(_boardPieces[i][j]);
		return toRet;
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
		return  _board.getElevation(col, row)/60.0;		
	}
	
	private void setUpBoard(GL2 gl){
		
		//needed because translation is relative to center of shape, not the corner
		for (int row = 0; row < _dimension; row++){
			for (int col = 0; col < _dimension; col++){
				double height = getElevation(col, row);
				// set up _boardPiece
				BoardPiece piece = new BoardPiece(this, height, new Posn(col, row));
				_boardPieces[col][row] = piece;

				// set up _gamePiece
				if (_board.getPiece(col,  row) != null) 
					_gamePieces.add(new GamePiece(gl, this, _board.getPiece(col,  row), height));
			}
		}
		
		System.out.println(_board.elevationsToString());
		System.out.println("===================");
		System.out.println(_board.piecesToString());
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
