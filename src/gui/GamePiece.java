package gui;

import java.util.HashMap;

import javax.media.opengl.GL;
import javax.media.opengl.GL2;
import javax.media.opengl.glu.GLU;
import javax.media.opengl.glu.GLUquadric;
import javax.vecmath.*;

import terrace.DefaultBoard;
import terrace.Piece;
import terrace.Player;
import terrace.Posn;

public class GamePiece implements Drawable {
	boolean _selected;	/** Whether or not this object has been selected by the user **/
	double _elevation;
	private double _radius;
	private GLU glu;
	GUIBoard _board;
	Piece _piece;
	
	public GamePiece(GL2 gl, GUIBoard board, Piece piece, double d){

	 //   glu.gluQuadricNormals(_quadric, GL2.GL_SMOOTH);
	    
		glu = new GLU();
		_board = board;
	    _elevation = d;
	    _selected = false;
	    _radius = (piece.getSize() + 1) * .01;
	    _piece = piece;
	}
	
	/**
	 * Sets the new elevation of the game piece. Useful for drawing
	 * @param elevation - an integer representing the new elevation of the game piece
	 */
	public void setElevation(int elevation){
		_elevation = elevation;
	}
	
	@Override
	public void draw(GL2 gl){

		GLUquadric _quadric = glu.gluNewQuadric();
	    gl.glPushMatrix();			
		glu.gluQuadricNormals(_quadric, GL.GL_TRUE);
		gl.glLoadIdentity();
		Posn pos = _piece.getPosn();
		double shiftFactor = 1.0/_board.getDimensions()/2;
		double rowShift = 1.0/_board.getDimensions()*pos.x;
		double colShift = 1.0/_board.getDimensions()*pos.y;
		gl.glTranslated(.5 - shiftFactor - rowShift, _elevation + _radius, .5 - shiftFactor - colShift);

		double mult = (_piece.isTPiece()) ? .5 : 1;
		if (_selected) gl.glColor3d(mult*0.7f,mult*1,0.7f);
		else {
			Vector3d vec = _board._playerColors.get(_piece.getPlayer());
			gl.glColor3d(mult*vec.x, mult*vec.y, vec.z);
		}
		
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
