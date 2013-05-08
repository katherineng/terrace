package terrace.gui.game;


import javax.media.opengl.GL;
import javax.media.opengl.GL2;
import javax.media.opengl.glu.GLU;
import javax.media.opengl.glu.GLUquadric;

import terrace.Piece;
import terrace.TPiece;
import terrace.util.Posn;
import terrace.util.Vector3d;

public class TriangleGamePiece extends GamePiece implements Drawable {
	public TriangleGamePiece(GUIBoard board, Piece piece){
		super(board, piece);
	}
	
	boolean tileOrientedUp(Posn pos) {
		return pos.y % 2 == 0;
	}
	
	@Override
	public void draw(GL2 gl) {
		GLU glu = new GLU();
		GLUquadric quadric = glu.gluNewQuadric();
		
	    gl.glPushMatrix();			
		glu.gluQuadricNormals(quadric, GL.GL_TRUE);
		gl.glLoadIdentity();
		Posn pos = _piece.getPosn();
		
		double widthFactor = 1.0 / _board.getWidth();
		double heightFactor = 1.0 / _board.getHeight();
		double rowShift = tileOrientedUp(pos) ? (pos.y * heightFactor) + widthFactor / 3. - .01 : ((pos.y - 1) * heightFactor) + widthFactor * 2./3. + .01;
		double colShift = tileOrientedUp(pos) ? (pos.x * widthFactor) + widthFactor / 3. - .01 : (pos.x * widthFactor) + widthFactor * 2./3. + .01;
		gl.glTranslated(.5 - rowShift, _board.getElevation(pos) * (1.0 / _board.getWidth()) + _radius, .5 - colShift);
		
		double mult = (_piece instanceof TPiece) ? .5 : 1;
		Vector3d vec = _board._playerColors.get(_piece.getColor()); // set color
		if (_selected) {
			gl.glColor3d(.2*vec.x, .2*vec.y, .2*vec.z);
		} else {
			gl.glColor3d(mult*vec.x, mult*vec.y, vec.z);
		}
		
		if (_piece instanceof TPiece)
			(new RectPrism(_radius*1.75, _radius*1.75, _radius*1.75, new Vector3d(vec.x,  vec.y, vec.z))).draw(gl);
		else
			glu.gluSphere(quadric, _radius, 25, 25);
	    gl.glPopMatrix();
	}
}
