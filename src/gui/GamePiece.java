package gui;

import java.util.HashMap;

import javax.media.opengl.GL;
import javax.media.opengl.GL2;
import javax.media.opengl.glu.GLU;
import javax.media.opengl.glu.GLUquadric;
import javax.vecmath.*;

import terrace.Piece;
import terrace.Player;

public class GamePiece implements Drawable {
	GLUquadric _quadric;
	Vector2d _position;	/** The position of this object in 3d space **/
	boolean _selected;	/** Whether or not this object has been selected by the user **/
	double _elevation;
	private double _radius;
	private GLU glu;
	private HashMap<Player, Vector3d> _playerColors;
	Piece _piece;
	
	public GamePiece(GL2 gl, Piece piece, double d, Vector2d pos, GLUquadric quadric, HashMap<Player, Vector3d> playerColors){
		glu = new GLU();
		this._quadric = quadric;

	 //   glu.gluQuadricNormals(_quadric, GL2.GL_SMOOTH);
	    
	    _position = pos;
	    _elevation = d;
	    _selected = false;
	    _radius = (piece.getSize() + 1) * .01;
	    _playerColors = playerColors;
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

	    gl.glPushMatrix();			
		glu.gluQuadricNormals(_quadric, GL.GL_TRUE);
		gl.glLoadIdentity();
		gl.glTranslated(_position.x, _elevation + _radius, _position.y);

		if (_selected) gl.glColor3f(0.7f,1,0.7f);
		else {
			Vector3d vec = _playerColors.get(_piece.getPlayer());
			gl.glColor3d(vec.x, vec.y, vec.z);
		}
		
		glu.gluSphere(_quadric, _radius, 90, 90);
	    gl.glPopMatrix();
	}
	
	/**
	 * Changes whether or not this object is deeemd to be selected
	 */
	public void changeSelection(){
		_selected = !_selected;
	}

}
