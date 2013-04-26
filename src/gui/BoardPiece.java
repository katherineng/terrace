package gui;

import javax.media.opengl.GL2;
import javax.vecmath.Vector2d;
import javax.vecmath.Vector3d;

import terrace.Posn;

/**
 * The squares that lay atop the board w/ different elevations
 * @author ww15
 *
 */
public class BoardPiece implements Drawable {
	
	private double _height;
	private Posn _pos;
	boolean selected;
	GUIBoard _board;

	public BoardPiece(GUIBoard board, double height, Posn pos){
		_board = board;
		_height = height;
		_pos = pos;
		selected = false;
	}
	
	
	@Override
	public void draw(GL2 gl) {	
		double dim = 1.0/_board.getDimensions();
		double shiftFactor = 1.0/_board.getDimensions()/2;

		double rowShift = 1.0/_board.getDimensions()*_pos.x;
		double colShift = 1.0/_board.getDimensions()*_pos.y;
		Vector2d pos = new Vector2d(.5 - shiftFactor - rowShift, .5 - shiftFactor - colShift);
		gl.glPushMatrix();
		gl.glTranslated(pos.x, _height/2, pos.y);
		gl.glColor3f(1,1,1);
		gl.glBegin(GL2.GL_QUADS);
		for(int side = -1; side <= 1; side += 2){
			gl.glNormal3d(-1 * side*dim, 0,0);
			gl.glVertex3d(-0.5*dim * side, -0.5*_height, -0.5*dim);
			gl.glVertex3d(-0.5 *dim* side, -0.5 *_height* side, 0.5 *dim* side);
			gl.glVertex3d(-0.5 *dim* side, 0.5*_height, 0.5*dim);
			gl.glVertex3d(-0.5 *dim* side, 0.5*_height * side, -0.5 *dim* side);

			// top & bottom
			Vector3d select = (side == -1 && selected) ? new Vector3d(1,.5,.5) : new Vector3d(1,1,1);

			gl.glColor3d(select.x,select.y,select.z);
			gl.glNormal3d(0, -1 * side*_height, 0);
			gl.glVertex3d(-0.5*dim, -0.5 *_height* side, -0.5*dim);
			gl.glVertex3d(0.5 *dim* side, -0.5 *_height* side, -0.5*dim * side);
			gl.glVertex3d(0.5*dim, -0.5 *_height* side, 0.5*dim);
			gl.glVertex3d(-0.5*dim * side, -0.5*_height * side, 0.5 *dim* side);


			gl.glColor3f(1,1,1);
			gl.glNormal3d(0, 0, -1 * side*dim);
			gl.glVertex3d(-0.5*dim, -0.5*_height, -0.5 * side*dim);
			gl.glVertex3d(-0.5 *dim* side, 0.5 *_height* side, -0.5 *dim* side);
			gl.glVertex3d(0.5*dim, 0.5*_height, -0.5 * side*dim);
			gl.glVertex3d(0.5 *dim* side, -0.5*_height * side, -0.5 *dim* side);
		}
		gl.glEnd();
		
		// draw outlines for board
		gl.glColor3f(0,0,0);
		double side = -1*1.01;
		gl.glBegin(GL2.GL_LINE_STRIP);
		gl.glVertex3d(-0.5*dim, -0.5 *_height* side, -0.5*dim);
		gl.glVertex3d(0.5 *dim* side, -0.5 *_height* side, -0.5*dim * side);
		gl.glVertex3d(0.5*dim, -0.5 *_height* side, 0.5*dim);
		gl.glVertex3d(-0.5*dim * side, -0.5*_height * side, 0.5 *dim* side);
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
