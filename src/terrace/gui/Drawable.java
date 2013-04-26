package terrace.gui;

import javax.media.opengl.GL2;

/**
 * Interface for any object that can be rendered on the screen
 * @author ww15
 *
 */
public interface Drawable {
	public void draw(GL2 gl);
}
