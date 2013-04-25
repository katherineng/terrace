package gui;

import javax.media.opengl.GL;
import javax.media.opengl.GL2;
import javax.media.opengl.GLContext;
import javax.media.opengl.glu.GLU;
import javax.media.opengl.glu.GLUquadric;
import javax.vecmath.*;

public class GamePiece {
	GLUquadric quadric;
	Vector3d position;
	boolean selected;
	
	public GamePiece(GL2 gl){
		GLU glu = new GLU();
		this.quadric = glu.gluNewQuadric();

	    glu.gluQuadricNormals(quadric, gl.GL_SMOOTH);
	    
	    position = new Vector3d(Math.random(), Math.random(), Math.random());
	    selected = false;
	}
	
	public void draw(GL2 gl){

		GLU glu = new GLU();
		
		gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);

		gl.glEnable(GL.GL_DEPTH_TEST);  
		gl.glCullFace(GL.GL_FRONT);
		gl.glEnable(GL.GL_CULL_FACE); 
		gl.glFrontFace(GL.GL_CW); 

		glu.gluQuadricNormals(quadric, GL.GL_TRUE);

		gl.glLoadIdentity();
		gl.glTranslated(position.x, position.y, position.z);

		if (selected) gl.glColor3f(0.7f,1,0.7f);
		else gl.glColor3f(0f,0,0.7f);
		glu.gluSphere(quadric, 0.5f, 90, 90);
	}
	
	public void changeSelection(){
		selected = !selected;
	}

}
