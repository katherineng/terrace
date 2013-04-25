package gui;

import javax.media.opengl.GL2;
import javax.vecmath.Vector2d;
import javax.vecmath.Vector3d;

/**
 * The squares that lay atop the board w/ different elevations
 * @author ww15
 *
 */
public class BoardPiece implements Drawable {
	
	private double _dim;
	private double _height;
	private Vector2d _pos;
	boolean selected;

	public BoardPiece(double dim, double height, Vector2d pos){
		_dim = dim;
		_height = height;
		_pos = pos;
		selected = false;
	}
	
	
	@Override
	public void draw(GL2 gl) {	
		gl.glPushMatrix();
		gl.glTranslated(_pos.x, _height/2, _pos.y);
		gl.glColor3f(1,1,1);
		gl.glBegin(GL2.GL_QUADS);
		for(int side = -1; side <= 1; side += 2){
			gl.glNormal3d(-1 * side*_dim, 0,0);
			gl.glVertex3d(-0.5*_dim * side, -0.5*_height, -0.5*_dim);
			gl.glVertex3d(-0.5 *_dim* side, -0.5 *_height* side, 0.5 *_dim* side);
			gl.glVertex3d(-0.5 *_dim* side, 0.5*_height, 0.5*_dim);
			gl.glVertex3d(-0.5 *_dim* side, 0.5*_height * side, -0.5 *_dim* side);

			// top & bottom
			Vector3d select = (side == -1 && selected) ? new Vector3d(1,.5,.5) : new Vector3d(1,1,1);

			gl.glColor3d(select.x,select.y,select.z);
			gl.glNormal3d(0, -1 * side*_height, 0);
			gl.glVertex3d(-0.5*_dim, -0.5 *_height* side, -0.5*_dim);
			gl.glVertex3d(0.5 *_dim* side, -0.5 *_height* side, -0.5*_dim * side);
			gl.glVertex3d(0.5*_dim, -0.5 *_height* side, 0.5*_dim);
			gl.glVertex3d(-0.5*_dim * side, -0.5*_height * side, 0.5 *_dim* side);


			gl.glColor3f(1,1,1);
			gl.glNormal3d(0, 0, -1 * side*_dim);
			gl.glVertex3d(-0.5*_dim, -0.5*_height, -0.5 * side*_dim);
			gl.glVertex3d(-0.5 *_dim* side, 0.5 *_height* side, -0.5 *_dim* side);
			gl.glVertex3d(0.5*_dim, 0.5*_height, -0.5 * side*_dim);
			gl.glVertex3d(0.5 *_dim* side, -0.5*_height * side, -0.5 *_dim* side);
		}
		gl.glEnd();
		
		// draw outlines for board
		gl.glColor3f(0,0,0);
		double side = -1*1.01;
		gl.glBegin(GL2.GL_LINE_STRIP);
		gl.glVertex3d(-0.5*_dim, -0.5 *_height* side, -0.5*_dim);
		gl.glVertex3d(0.5 *_dim* side, -0.5 *_height* side, -0.5*_dim * side);
		gl.glVertex3d(0.5*_dim, -0.5 *_height* side, 0.5*_dim);
		gl.glVertex3d(-0.5*_dim * side, -0.5*_height * side, 0.5 *_dim* side);
		gl.glEnd();
		gl.glPopMatrix();
	}
	
	public void changeSelection(){
		selected = !selected;
	}
	
	public boolean isSelected(){
		return selected;
	}

}
