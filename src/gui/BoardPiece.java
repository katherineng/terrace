package gui;

import javax.media.opengl.GL2;
import javax.vecmath.Vector2d;

/**
 * The squares that lay atop the board w/ different elevations
 * @author ww15
 *
 */
public class BoardPiece implements Drawable {
	
	private double _dim;
	private double _height;
	private Vector2d _pos;

	public BoardPiece(double dim, double height, Vector2d pos){
		_dim = dim;
		_height = height;
		_pos = pos;		
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
			gl.glNormal3d(0, -1 * side*_height, 0);
			gl.glVertex3d(-0.5*_dim, -0.5 *_height* side, -0.5*_dim);
			gl.glVertex3d(0.5 *_dim* side, -0.5 *_height* side, -0.5*_dim * side);
			gl.glVertex3d(0.5*_dim, -0.5 *_height* side, 0.5*_dim);
			gl.glVertex3d(-0.5*_dim * side, -0.5*_height * side, 0.5 *_dim* side);

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

}
