package terrace.gui.game;

import javax.media.opengl.*;

import terrace.util.*;


/**
 * Draws rectangles because opengl doesn't have a fucking gluCube or
 * gluRectangle or anything WHAT THE FUCK
 * 
 * @author ww15
 *
 */
public class RectPrism implements Drawable {
	double _xscale, _yscale, _zscale; /** constants by which to scale the rectangle **/
	Vector3d _color;
	
	public RectPrism(
			double xscale, double yscale, double zscale, Vector3d color
	) {
		setup( xscale, yscale, zscale);
		_color = color;
	}
	
	public RectPrism(double xscale, double yscale, double zscale){
		_color = new Vector3d(0,0,0);
		setup(xscale, yscale, zscale);
	}
	
	private void setup(double xscale, double yscale, double zscale){
		this._xscale = xscale;
		this._yscale = yscale;
		this._zscale = zscale;
		
	}

	@Override
	public void draw(GL2 gl) {
		    gl.glBegin(GL2.GL_QUADS);
		    gl.glColor3d(_color.x,_color.y,_color.z);
		    for(int side = -1; side <= 1; side += 2){
		    	gl.glNormal3d(-1 * side, 0,0);
		    	gl.glVertex3d(-0.5*_xscale * side, -0.5*_yscale, -0.5*_zscale);
		    	gl.glVertex3d(-0.5 *_xscale* side, -0.5 *_yscale* side, 0.5 *_zscale* side);
		    	gl.glVertex3d(-0.5 *_xscale* side, 0.5*_yscale, 0.5*_zscale);
		    	gl.glVertex3d(-0.5 *_xscale* side, 0.5*_yscale * side, -0.5 *_zscale* side);
		    	
		    	gl.glNormal3d(0, -1 * side, 0);
		    	gl.glVertex3d(-0.5*_xscale, -0.5 *_yscale* side, -0.5*_zscale);
		    	gl.glVertex3d(0.5 *_xscale* side, -0.5 *_yscale* side, -0.5*_zscale * side);
		    	gl.glVertex3d(0.5*_xscale, -0.5 *_yscale* side, 0.5*_zscale);
		    	gl.glVertex3d(-0.5*_xscale * side, -0.5*_yscale * side, 0.5 *_zscale* side);
		    	
		    	gl.glNormal3d(0, 0, -1 * side);
		    	gl.glVertex3d(-0.5*_xscale, -0.5*_yscale, -0.5 * side*_zscale);
		    	gl.glVertex3d(-0.5 *_xscale* side, 0.5 *_yscale* side, -0.5 *_zscale* side);
		    	gl.glVertex3d(0.5*_xscale, 0.5*_yscale, -0.5 * side*_zscale);
		    	gl.glVertex3d(0.5 *_xscale* side, -0.5*_yscale * side, -0.5 *_zscale* side);
		    }
		    gl.glEnd();
	}
}
