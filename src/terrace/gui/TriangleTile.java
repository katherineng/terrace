package terrace.gui;

import javax.media.opengl.GL;
import javax.media.opengl.GL2;
import javax.vecmath.Vector2d;
import javax.vecmath.Vector3d;

import terrace.util.Posn;

public class TriangleTile extends BoardTile{

	public TriangleTile(GUIBoard board, double height, Posn pos, int level) {
		super(board, height, pos, level);
		// TODO Auto-generated constructor stub
	}

	private boolean orientedDown(){
		if (_pos.y % 2 == 0) return true;
		return false;
	}

	@Override
	public void draw(GL2 gl) {
		double sideLength =  1.0/_board.getWidth();
		double colShift = 1.0/_board.getWidth()/2*_pos.y;
		double rowShift = 1.0/_board.getWidth()*_pos.x;
		if (!orientedDown()){
			colShift += sideLength;
			rowShift += sideLength;
		}
		Vector2d pos = new Vector2d(.5  - rowShift, .5  - colShift);
		if (!orientedDown()) pos.y += sideLength/2;
		double mult = (_level % 2 == 0) ? .7 : 1; 

		gl.glPushMatrix();

		double elevation = _height;
		double rotation = orientedDown() ? 90 : -90;
		gl.glTranslated(pos.x, 0, pos.y);
		gl.glRotated(rotation, 0., 1., 0.);
		gl.glBegin(GL2.GL_QUADS);
		gl.glNormal3d(0f, 0f, 1);
		gl.glVertex3d(0f, 0f, 0f);
		gl.glVertex3d(sideLength, 0f, 0f);
		gl.glVertex3d(sideLength, sideLength*elevation, 0f);
		gl.glVertex3d(0f, sideLength*elevation, 0f);
		gl.glEnd();


		gl.glBegin(GL2.GL_QUADS);
		gl.glNormal3d(-1, 0f, 0f);
		gl.glVertex3d(0f, 0f, 0f);
		gl.glVertex3d(0f, sideLength*elevation, 0f);
		gl.glVertex3d(0f, sideLength*elevation, -sideLength);
		gl.glVertex3d(0f, 0f, -sideLength);
		gl.glEnd();


		gl.glBegin(GL2.GL_QUADS);
		gl.glNormal3d(1, 0f, -.25f);
		gl.glVertex3d(sideLength, 0f, 0f);
		gl.glVertex3d(0f, 0f, -sideLength);
		gl.glVertex3d(0f, sideLength*elevation, -sideLength);
		gl.glVertex3d(sideLength, sideLength*elevation, 0f);
		gl.glEnd();

		//top of triangle
		gl.glBegin(GL2.GL_TRIANGLES);
		Vector3d select = new Vector3d(mult*.8,mult*.8,mult*.8);
		gl.glColor3d(select.x, select.y, select.z);
		gl.glNormal3d(0f, 1, 0f);
		gl.glVertex3d(0f, sideLength*elevation, 0f);
		gl.glVertex3d(sideLength, sideLength*elevation, 0f);
		gl.glVertex3d(0f, sideLength*elevation, -sideLength);
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
		gl.glVertex3d(0f, sideLength*elevation, 0f);
		gl.glVertex3d(sideLength, sideLength*elevation, 0f);
		gl.glVertex3d(0f, sideLength*elevation, -sideLength);
		gl.glEnd();

		gl.glPopMatrix();
	}

}
