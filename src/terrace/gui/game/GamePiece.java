package terrace.gui.game;


import javax.media.opengl.GL;
import javax.media.opengl.GL2;
import javax.media.opengl.glu.GLU;
import javax.media.opengl.glu.GLUquadric;

import terrace.Piece;
import terrace.TPiece;
import terrace.util.Posn;

public abstract class GamePiece implements Drawable {
	boolean _selected;	/** Whether or not this object has been selected by the user **/
	protected double _radius;
	GUIBoard _board;
	Piece _piece;
	
	public GamePiece(GUIBoard board, Piece piece){
		_board = board;
	    _selected = false;
	    _radius = (piece.getSize() + 1) * .01;
	    _piece = piece;

	}
	
	public Posn getPosn(){
		return _piece.getPosn();
	}
	public boolean isSelected(){
		return _selected;
	}
	public Piece getPiece(){
		return _piece;
	}
	
	/**
	 * Changes whether or not this object is deeemd to be selected
	 */
	public void changeSelection(){
		_selected = !_selected;
	}

	
	public abstract void drawPiece(GL2 gl);
	
	@Override
	public void draw(GL2 gl) {
		// TODO Auto-generated method stub
		drawPiece(gl);
	}

}
