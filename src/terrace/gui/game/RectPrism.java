package terrace.gui.game;

import javax.media.opengl.*;


/**
 * Draws rectangles because opengl doesn't have a fucking gluCube or
 * gluRectangle or anything WHAT THE FUCK
 * 
 * @author ww15
 *
 */
public class RectPrism implements Drawable {
	double _x, _y, _z;
	double _xscale, _yscale, _zscale; /** constants by which to scale the rectangle **/
	
	public RectPrism(
			double x, double y, double z,
			double xscale, double yscale, double zscale
	) {
		this._x = x;
		this._y = y;
		this._z = z;
		this._xscale = xscale;
		this._yscale = yscale;
		this._zscale = zscale;
	}

	@Override
	public void draw(GL2 gl) {
		    gl.glPushMatrix();
		    gl.glTranslated(_x, _y, _z);
		    gl.glColor3f(0,0,0);
		    gl.glBegin(GL2.GL_QUADS);
		    for(int side = -1; side <= 1; side += 2){
		    	gl.glNormal3d(-1 * side*_xscale, 0,0);
		    	gl.glVertex3d(-0.5*_xscale * side, -0.5*_yscale, -0.5*_zscale);
		    	gl.glVertex3d(-0.5 *_xscale* side, -0.5 *_yscale* side, 0.5 *_zscale* side);
		    	gl.glVertex3d(-0.5 *_xscale* side, 0.5*_yscale, 0.5*_zscale);
		    	gl.glVertex3d(-0.5 *_xscale* side, 0.5*_yscale * side, -0.5 *_zscale* side);
		    	
		    	gl.glNormal3d(0, -1 * side*_yscale, 0);
		    	gl.glVertex3d(-0.5*_xscale, -0.5 *_yscale* side, -0.5*_zscale);
		    	gl.glVertex3d(0.5 *_xscale* side, -0.5 *_yscale* side, -0.5*_zscale * side);
		    	gl.glVertex3d(0.5*_xscale, -0.5 *_yscale* side, 0.5*_zscale);
		    	gl.glVertex3d(-0.5*_xscale * side, -0.5*_yscale * side, 0.5 *_zscale* side);
		    	
		    	gl.glNormal3d(0, 0, -1 * side*_zscale);
		    	gl.glVertex3d(-0.5*_xscale, -0.5*_yscale, -0.5 * side*_zscale);
		    	gl.glVertex3d(-0.5 *_xscale* side, 0.5 *_yscale* side, -0.5 *_zscale* side);
		    	gl.glVertex3d(0.5*_xscale, 0.5*_yscale, -0.5 * side*_zscale);
		    	gl.glVertex3d(0.5 *_xscale* side, -0.5*_yscale * side, -0.5 *_zscale* side);
		    }
		    gl.glEnd();
		    gl.glPopMatrix();
	}
}
