package gui;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.concurrent.atomic.AtomicInteger;

import javax.media.opengl.*;
import javax.media.opengl.glu.GLU;
import javax.vecmath.Vector4d;

import com.jogamp.common.nio.Buffers;

public class SelectionRecorder{

    IntBuffer m_selectionBuffer;
    int m_mouseX, m_mouseY;
    GL2 gl;
    
	public SelectionRecorder(GL2 gl){
		m_selectionBuffer = null;
		this.gl = gl;
	}
	
	public static class HitRecord{
		int numName;
		int minDepth;
		int maxDepth;
		int name;
		public HitRecord(int numName, int minDepth, int maxDepth, int name){
			this.numName = numName;
			this.minDepth = minDepth;
			this.maxDepth = maxDepth;
			this.name = name;
		}
	}

	void enterSelectionMode(int x, int y, int numObjects) {
		GLU glu = new GLU();
		if(m_selectionBuffer != null){
			assert(false);
			return;
		}

		// Get information about the current OpenGL state.
		int[] viewport = new int[4];
		double[] projectionMatrix = new double[16];
		gl.glGetIntegerv(GL2.GL_VIEWPORT, viewport,0);
		gl.glGetDoublev(GL2.GL_PROJECTION_MATRIX, projectionMatrix, 0);

		// Make the origin in the upper left.
		y = viewport[3] - y - 1;

		// Pre-multiply the projection matrix with a pick matrix, which expands
		// the viewport to fill the 1x1 square under the mouse pointer at (x, y).
		gl.glMatrixMode(GL2.GL_PROJECTION);
		gl.glPushMatrix();
		gl.glLoadIdentity();
		glu.gluPickMatrix((double)x, (double)y, 1, 1, viewport, 0);
		gl.glMultMatrixd(projectionMatrix, 0);
		gl.glMatrixMode(GL2.GL_MODELVIEW);


		// Enter selection mode with enough room for numObjects hit records.
		m_selectionBuffer = Buffers.newDirectIntBuffer(512);
		gl.glSelectBuffer(numObjects * 4, m_selectionBuffer);
		gl.glRenderMode(GL2.GL_SELECT);
		gl.glInitNames();
		gl.glPushName(0);

		// Remember the mouse position for reconstructing the hit point later.
		m_mouseX = x;
		m_mouseY = y;
	}

	void setObjectIndex(int index)
	{
		// Replace the current name on top of the name stack with index.
		// We only use a name stack of depth one, so it's like a variable.
		gl.glLoadName(index);
	}

	boolean exitSelectionMode(AtomicInteger index, Vector4d hitm) {
		GLU glu = new GLU();
		 // Put the user's projection matrix back the way it was.
	    gl.glMatrixMode(GL2.GL_PROJECTION);
	    gl.glPopMatrix();
	    gl.glMatrixMode(GL2.GL_MODELVIEW);

	    // Leave selection mode and parse the hits it generated.
	    boolean hasSelection = false;
	    int hits = gl.glRenderMode(GL2.GL_RENDER);
	    int minDepth = ~0;
	    for (int i = 0; i < hits; i++)
	    {
	        int hit = i*4;
	        if (m_selectionBuffer.get(hit+1) < minDepth)
	        {
	            index.set(m_selectionBuffer.get(hit + 3));
	            minDepth = m_selectionBuffer.get(hit+1);
	            hasSelection = true;
	        }
	    }

	    // We're done with the selection buffer.
	    m_selectionBuffer = null;
	    if (hasSelection) {
	        int[] viewport = new int[4];
	        float[] modelviewMatrix = new float[16];
	        FloatBuffer modelBuffer = FloatBuffer.wrap(modelviewMatrix);
	        float[] projectionMatrix  = new float[16];
	        FloatBuffer projectBuffer = FloatBuffer.wrap(projectionMatrix);
	        gl.glGetIntegerv(GL2.GL_VIEWPORT, viewport, 0);
	        gl.glGetFloatv(GL2.GL_MODELVIEW_MATRIX, modelviewMatrix, 0);
	        gl.glGetFloatv(GL2.GL_PROJECTION_MATRIX, projectionMatrix, 0);
	        float[] asdf = new float[]{(float) hitm.x, (float) hitm.y,(float)  hitm.z};
	        glu.gluUnProject((float)m_mouseX, (float)m_mouseY, (float)minDepth / (float)0xFFFFFFFF,
	        		modelBuffer, projectBuffer, IntBuffer.wrap(viewport), FloatBuffer.wrap(asdf));
	        hitm.x = asdf[0];
	        hitm.y = asdf[1];
	        hitm.z = asdf[2];
	        return true;
	    }

	    return false;
	}
}