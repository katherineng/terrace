package terrace.gui;

import javax.media.opengl.*;
import javax.vecmath.*;

import terrace.Posn;

/**
 * The squares that lay atop the board w/ different elevations
 * @author ww15
 *
 */
public class BoardTile implements Drawable {

	private static final Vector3d HOVER_COLOR = new Vector3d(.2,.2,.2);
	private static final Vector3d MOVES_COLOR = new Vector3d(1,.5,.5);
	private double _height;
	private int _level;
	private Posn _pos;
	boolean selected;
	GUIBoard _board;
	Vector3d _moveColor;
	private boolean _correct;
	int incorrectTimes;

	public BoardTile(GUIBoard board, double height, Posn pos, int level){
		_board = board;
		_height = height;
		_level = level;
		_pos = pos;
		selected = false;
		_correct = true;
	}
	
	public double getElevation(){
		return _height;
	}
	public Posn getPosn(){
		return _pos;
	}
	@Override
	public void draw(GL2 gl) {	
		double dim = 1.0/_board.getDimensions();
		double shiftFactor = 1.0/_board.getDimensions()/2;
		double rowShift = 1.0/_board.getDimensions()*_pos.y;
		double colShift = 1.0/_board.getDimensions()*_pos.x;
		Vector2d pos = new Vector2d(.5 - shiftFactor - rowShift, .5 - shiftFactor - colShift);
		gl.glPushMatrix();
		gl.glTranslated(pos.x, _height/2, pos.y);
		double mult = (_level % 2 == 0) ? .7 : 1; 
		gl.glBegin(GL2.GL_QUADS);
		for(int side = -1; side <= 1; side += 2){
			gl.glNormal3d(-1 * side*dim, 0,0);
			gl.glVertex3d(-0.5*dim * side, -0.5*_height, -0.5*dim);
			gl.glVertex3d(-0.5 *dim* side, -0.5 *_height* side, 0.5 *dim* side);
			gl.glVertex3d(-0.5 *dim* side, 0.5*_height, 0.5*dim);
			gl.glVertex3d(-0.5 *dim* side, 0.5*_height * side, -0.5 *dim* side);

			// top & bottom
			Vector3d select = (side == -1 && selected) ? HOVER_COLOR : new Vector3d(mult*.8,mult*.8,mult*.8);


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
		gl.glPushAttrib(GL2.GL_HINT_BIT | GL2.GL_CURRENT_BIT | GL2.GL_LINE_BIT | GL2.GL_COLOR_BUFFER_BIT);

		gl.glBlendFunc(GL.GL_SRC_ALPHA, GL.GL_ONE_MINUS_SRC_ALPHA);
		gl.glEnable(GL.GL_BLEND);
		gl.glHint(GL.GL_FASTEST, GL.GL_LINE_SMOOTH_HINT);
		gl.glEnable(GL.GL_LINE_SMOOTH);
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
	
	public void setMoveColor(Vector3d move){
		_moveColor = move;
	}
	
	public void incorrect(){
		_correct = !_correct;
	}

}
