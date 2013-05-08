package terrace.gui.game;

import javax.media.opengl.GL;
import javax.media.opengl.GL2;
import javax.media.opengl.glu.GLU;
import javax.media.opengl.glu.GLUquadric;

import terrace.Piece;
import terrace.TPiece;
import terrace.util.Posn;
import terrace.util.Vector3d;

public class SquareGamePiece extends GamePiece implements Drawable {
	public SquareGamePiece(GUIBoard board, Piece piece){
		super(board, piece);
	}
	
	@Override
	public void draw(GL2 gl) {
		GLU glu = new GLU();
		GLUquadric quadric = glu.gluNewQuadric();
		
	    gl.glPushMatrix();			
		glu.gluQuadricNormals(quadric, GL.GL_TRUE);
		gl.glLoadIdentity();
		Posn pos = _piece.getPosn();
		double shiftFactor = _board.getShiftFactor();
		double rowShift = 1.0/_board.getHeight()*pos.y;
		double colShift = 1.0/_board.getWidth()*pos.x;
		gl.glTranslated(.5 - shiftFactor - rowShift, _board.getElevation(pos) + _radius, .5 - shiftFactor - colShift);
		
		//double mult = (_piece instanceof TPiece) ? .5 : 1;
		Vector3d vec = _board._playerColors.get(_piece.getColor()); // set color
		
		if (_selected) {
			gl.glColor3d(.2*vec.x, .2*vec.y, .2*vec.z);
		} else {
			gl.glColor3d(vec.x, vec.y, vec.z);
		}
		
		if (_piece instanceof TPiece)
			(new RectPrism(_radius*1.75, _radius*1.75, _radius*1.75, new Vector3d(vec.x,  vec.y, vec.z))).draw(gl);
		else
			glu.gluSphere(quadric, _radius, 25, 25);
	    gl.glPopMatrix();
	}
}
