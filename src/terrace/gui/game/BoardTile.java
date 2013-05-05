package terrace.gui.game;

import javax.media.opengl.*;
import javax.vecmath.*;

import terrace.util.Posn;

/**
 * The squares that lay atop the board w/ different elevations
 * @author ww15
 *
 */
public abstract class BoardTile implements Drawable {

	protected static final Vector3d HOVER_COLOR = new Vector3d(.2,.2,.2);
	protected int _incorrect_timing = 0;
	protected double _height;
	protected int _level;
	protected Posn _pos;
	GUIBoard _board;
	protected boolean _selected;	/** Whether this tile is being hovered over **/
	protected boolean _correct;	/** Whether this is a valid move when the user clicks on it **/
	Vector3d _moveColor;		/** The color of the tile if the move is possible. Corresponds to the current user **/

	public BoardTile(GUIBoard board, double height, Posn pos, int level){
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
	abstract public void draw(GL2 gl);
	
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
	
	@Override
	public String toString() {
		return _pos.toString() + " " + _moveColor;
	}

}
