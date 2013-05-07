package terrace.gui.game;

import terrace.Piece;
import terrace.util.Posn;

public abstract class GamePiece implements Drawable {
	boolean _selected;	// Whether or not this object has been selected by the user
	protected double _radius;
	GUIBoard _board;
	Piece _piece;
	
	public GamePiece(GUIBoard board, Piece piece) {
		_board = board;
	    _selected = false;
	    _radius = (piece.getSize() + 1) * .01;
	    _piece = piece;

	}
	
	public Posn getPosn() {
		return _piece.getPosn();
	}
	
	public boolean isSelected() {
		return _selected;
	}
	
	public Piece getPiece() {
		return _piece;
	}
	
	/**
	 * Changes whether or not this object is deeemd to be selected
	 */
	public void changeSelection() {
		_selected = !_selected;
	}
}
