package terrace.gui;

import javax.media.opengl.*;

/**
 * Draws rectangles because opengl doesn't have a fucking gluCube or
 * gluRectangle or anything WHAT THE FUCK
 * 
 * @author ww15
 *
 */
public class RectPrism implements Drawable{
	double _x, _y, _z; /** constants by which to scale the rectangle **/

	public RectPrism(double xscale, double yscale, double zscale){
		this._x = xscale;
		this._y = yscale;
		this._z = zscale;
	}

	//TODO: BUG! it changes color though it shouldn't. I think something isn't being cleared.
	@Override
	public void draw(GL2 gl) {

		    gl.glPushMatrix();			
		    gl.glColor3f(0,0,0);
		    gl.glBegin(GL2.GL_QUADS);
		    for(int side = -1; side <= 1; side += 2){
		    	gl.glNormal3d(-1 * side*_x, 0,0);
		    	gl.glVertex3d(-0.5*_x * side, -0.5*_y, -0.5*_z);
		    	gl.glVertex3d(-0.5 *_x* side, -0.5 *_y* side, 0.5 *_z* side);
		    	gl.glVertex3d(-0.5 *_x* side, 0.5*_y, 0.5*_z);
		    	gl.glVertex3d(-0.5 *_x* side, 0.5*_y * side, -0.5 *_z* side);

		    	gl.glNormal3d(0, -1 * side*_y, 0);
		    	gl.glVertex3d(-0.5*_x, -0.5 *_y* side, -0.5*_z);
		    	gl.glVertex3d(0.5 *_x* side, -0.5 *_y* side, -0.5*_z * side);
		    	gl.glVertex3d(0.5*_x, -0.5 *_y* side, 0.5*_z);
		    	gl.glVertex3d(-0.5*_x * side, -0.5*_y * side, 0.5 *_z* side);

		    	gl.glNormal3d(0, 0, -1 * side*_z);
		    	gl.glVertex3d(-0.5*_x, -0.5*_y, -0.5 * side*_z);
		    	gl.glVertex3d(-0.5 *_x* side, 0.5 *_y* side, -0.5 *_z* side);
		    	gl.glVertex3d(0.5*_x, 0.5*_y, -0.5 * side*_z);
		    	gl.glVertex3d(0.5 *_x* side, -0.5*_y * side, -0.5 *_z* side);
		    }
		    gl.glEnd();
		    gl.glPopMatrix();
		
	}

}
