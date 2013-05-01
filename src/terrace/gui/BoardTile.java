package terrace.gui;

import javax.media.opengl.*;
import javax.vecmath.*;

import terrace.util.Posn;

/**
 * The squares that lay atop the board w/ different elevations
 * @author ww15
 *
 */
public class BoardTile implements Drawable {

	private static final Vector3d HOVER_COLOR = new Vector3d(.2,.2,.2);
	private int _incorrect_timing = 0;
	private double _height;
	private int _level;
	private Posn _pos;
	GUISquareBoard _board;
	private boolean _selected;	/** Whether this tile is being hovered over **/
	private boolean _correct;	/** Whether this is a valid move when the user clicks on it **/
	Vector3d _moveColor;		/** The color of the tile if the move is possible. Corresponds to the current user **/

	public BoardTile(GUISquareBoard board, double height, Posn pos, int level){
		_height = height;
		_level = level;
		_pos = pos;
		_board = board;
		_selected = false;
		_correct = true;
		_moveColor = null;
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
		double mult = (_level % 2 == 0) ? .7 : 1; 
		
		gl.glPushMatrix();
		gl.glTranslated(pos.x, _height/2, pos.y);
		gl.glBegin(GL2.GL_QUADS);
		for(int side = -1; side <= 1; side += 2){
			gl.glNormal3d(-1 * side*dim, 0,0);
			gl.glVertex3d(-0.5*dim * side, -0.5*_height, -0.5*dim);
			gl.glVertex3d(-0.5 *dim* side, -0.5 *_height* side, 0.5 *dim* side);
			gl.glVertex3d(-0.5 *dim* side, 0.5*_height, 0.5*dim);
			gl.glVertex3d(-0.5 *dim* side, 0.5*_height * side, -0.5 *dim* side);

			/* ===== SPECIAL RENDERING FOR THE TOP OF THE BOARD =======*/
			
			// hover colors
			Vector3d select = (side == -1 && _selected) ? HOVER_COLOR : new Vector3d(mult*.8,mult*.8,mult*.8);
			
			// invalid move colors
			if (side == -1 && _selected && !_correct){
				if (_incorrect_timing < 20){
					select = new Vector3d(1.0, select.y, select.z);
					_incorrect_timing++;
				} else {
					_incorrect_timing = 0;
					_correct = true;
				}
			} 
			
			// possible move colors
			if (_moveColor != null) select = new Vector3d(_moveColor.x *.5, _moveColor.y * .5, _moveColor.z * .5);				

			// ACTUAL DRAWING
			gl.glColor3d(select.x,select.y,select.z);
			gl.glNormal3d(0, -1 * side*_height, 0);
			gl.glVertex3d(-0.5*dim, -0.5 *_height* side, -0.5*dim);
			gl.glVertex3d(0.5 *dim* side, -0.5 *_height* side, -0.5*dim * side);
			gl.glVertex3d(0.5*dim, -0.5 *_height* side, 0.5*dim);
			gl.glVertex3d(-0.5*dim * side, -0.5*_height * side, 0.5 *dim* side);
			
			/*============================================================*/

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
		double side = -1;
		
		// anti-aliasing for the lines
		gl.glPushAttrib(GL2.GL_HINT_BIT | GL2.GL_CURRENT_BIT | GL2.GL_LINE_BIT | GL2.GL_COLOR_BUFFER_BIT);
		gl.glBlendFunc(GL.GL_SRC_ALPHA, GL.GL_ONE_MINUS_SRC_ALPHA);
		gl.glEnable(GL.GL_BLEND);
		gl.glHint(GL.GL_LINE_SMOOTH_HINT, GL.GL_FASTEST);
		gl.glEnable(GL.GL_LINE_SMOOTH);
		
		// draw outlines for the squares
		gl.glBegin(GL2.GL_LINE_STRIP);
		gl.glVertex3d(-0.5*dim, -0.5 *_height* side, -0.5*dim);
		gl.glVertex3d(0.5 *dim* side, -0.5 *_height* side, -0.5*dim * side);
		gl.glVertex3d(0.5*dim, -0.5 *_height* side, 0.5*dim);
		gl.glVertex3d(-0.5*dim * side, -0.5*_height * side, 0.5 *dim* side);
		gl.glEnd();
		
		gl.glPopMatrix();
	}
	
	public void changeSelection(){
		_selected = !_selected;
	}
	
	public boolean isSelected(){
		return _selected;
	}
	
	public void setMoveColor(Vector3d move){
		_moveColor = move;
	}
	
	public void incorrect(){
		_correct = !_correct;
	}

}
