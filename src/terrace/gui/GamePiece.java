package terrace.gui;


import javax.media.opengl.GL;
import javax.media.opengl.GL2;
import javax.media.opengl.glu.GLU;
import javax.media.opengl.glu.GLUquadric;
import javax.vecmath.*;

import terrace.Piece;
import terrace.Posn;

public class GamePiece implements Drawable {
	boolean _selected;	/** Whether or not this object has been selected by the user **/
	private double _radius;
	private GLU glu;
	GUIBoard _board;
	Piece _piece;
	GLUquadric _quadric;
	
	public GamePiece(GL2 gl, GUIBoard board, Piece piece){
		glu = new GLU();
		_quadric = glu.gluNewQuadric();
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

	@Override
	public void draw(GL2 gl){
	    gl.glPushMatrix();			
		glu.gluQuadricNormals(_quadric, GL.GL_TRUE);
		gl.glLoadIdentity();
		Posn pos = _piece.getPosn();
		double shiftFactor = 1.0/_board.getDimensions()/2;
		double rowShift = 1.0/_board.getDimensions()*pos.y;
		double colShift = 1.0/_board.getDimensions()*pos.x;
		gl.glTranslated(.5 - shiftFactor - rowShift, _board.getElevation(pos) + _radius, .5 - shiftFactor - colShift);

		double mult = (_piece.isTPiece()) ? .5 : 1;
		Vector3d vec = _board._playerColors.get(_piece.getPlayer()); // set color
		if (_selected) gl.glColor3d(.2*vec.x, .2*vec.y, .2*vec.z);
		else gl.glColor3d(mult*vec.x, mult*vec.y, vec.z);
		
		glu.gluSphere(_quadric, _radius, 25, 25);
	    gl.glPopMatrix();
	}
	
	/**
	 * Changes whether or not this object is deeemd to be selected
	 */
	public void changeSelection(){
		_selected = !_selected;
	}

}