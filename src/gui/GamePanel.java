package gui;
import java.awt.*;
import java.awt.event.*;


import javax.media.opengl.GL;
import javax.media.opengl.GL2;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLCapabilities;
import javax.media.opengl.GLEventListener;
import javax.media.opengl.GLProfile;
import javax.media.opengl.awt.GLCanvas;
import javax.media.opengl.glu.GLU;
import javax.swing.*;

import com.jogamp.common.nio.Buffers;
import com.jogamp.opengl.util.Animator;

import java.nio.IntBuffer;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

import javax.vecmath.*;


public class GamePanel extends GLCanvas implements MouseWheelListener{

	Camera m_camera;
	GamePanel this_gui;
	Vector2d m_prevMousePos;
	java.util.ArrayList<GamePiece> m_pieces;
	GamePiece m_selection; //todo
	Vector4d m_hit;
	boolean selection_draw;
	Vector2d selection_mouse;
	
	public GamePanel(){
		super(new GLCapabilities(GLProfile.getDefault()));
		setSize(600,600);
		
		GraphicListener listener=new GraphicListener();
		setVisible(true);
	    addGLEventListener(listener);
	    
	    Animator animator = new Animator(this);
	    animator.start();
	    this_gui = this;
	    m_hit = new Vector4d(0,0,0,0);
	    m_pieces = new ArrayList<GamePiece>();
	    m_selection = null;
	    selection_draw = false;
	    
	    
	    
	    this.addMouseListener(new MouseListener(){
			@Override
			public void mousePressed(MouseEvent e) {
				m_prevMousePos = new Vector2d(e.getX(), e.getY());
			}

			@Override
			public void mouseClicked(MouseEvent e) {
				selection_mouse = new Vector2d(e.getX(), e.getY());
				selection_draw = true;
				repaint();
			}
			@Override
			public void mouseReleased(MouseEvent e) {}
			@Override
			public void mouseEntered(MouseEvent e) {}
			@Override
			public void mouseExited(MouseEvent e) {}
	    });
	    
	    this.addMouseMotionListener(new MouseMotionListener(){
			@Override
			public void mouseDragged(MouseEvent e) {
				if ((e.getModifiers() & InputEvent.BUTTON3_MASK) != 0) {
					Vector2d pos = new Vector2d(e.getX(), e.getY());
					m_camera.mouseMove(new Vector2d(pos.x - m_prevMousePos.x, pos.y - m_prevMousePos.y));
					m_prevMousePos = pos;
				}
			}
			@Override
			public void mouseMoved(MouseEvent e) {}
	    	
	    });
	    //set up camera;
	    Vector3d center = new Vector3d(0., 0., 0.);
	    Vector3d up = new Vector3d(0.f, 1.f, 0.f);
	    double theta = Math.PI * 1.5f, phi = 0.2, fovy = 60., zoom = 3.5;
	    m_camera = new Camera(center, up, theta, phi, fovy, zoom);
	    setVisible(true);

	}
	private static float deltaZ=0;
	private static float deltaZ2=0;
	

	public class GraphicListener implements GLEventListener{

		
		private void applyCameraPerspective(GL2 gl){
			GLU glu = new GLU();
			//set up camera stuff
			double ratio = ((double) this_gui.getWidth())/ this_gui.getHeight();
			Vector3d dir = m_camera.fromAngles();
			dir = new Vector3d(dir.x * m_camera.zoom, dir.y * m_camera.zoom, dir.z * m_camera.zoom);
			Vector3d eye = new Vector3d(m_camera.center.x - dir.x, m_camera.center.y - dir.y, m_camera.center.z - dir.z);

			gl.glMatrixMode(GL2.GL_PROJECTION);
			gl.glLoadIdentity();
			glu.gluPerspective(m_camera.fovy, ratio, 0.1f, 21000.f);
			glu.gluLookAt(eye.x, eye.y, eye.z,
					eye.x + dir.x, eye.y + dir.y, eye.z + dir.z,
					m_camera.up.x, m_camera.up.y, m_camera.up.z);
			gl.glMatrixMode(gl.GL_MODELVIEW);
			gl.glLoadIdentity();
		}
		
		public void display(GLAutoDrawable arg0) {
			GL2 gl=arg0.getGL().getGL2();
			GLU glu = new GLU();
			if (selection_draw){
				//doPick(gl);
				setSelection(arg0);
				m_selection.changeSelection();
				selection_draw = false;
			} else {
				applyCameraPerspective(gl);		
				gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);

				gl.glEnable(GL.GL_DEPTH_TEST);  
				gl.glCullFace(GL.GL_FRONT);
				gl.glEnable(GL.GL_CULL_FACE); 
				gl.glFrontFace(GL.GL_CW); 
				for (GamePiece piece: m_pieces)
					piece.draw(gl);
			}
		}


	
		void setSelection(GLAutoDrawable arg0){
			GL2 gl=arg0.getGL().getGL2();
		    SelectionRecorder recorder = new SelectionRecorder(gl);

		    // See if the (x, y) mouse position hits any primitives.
		    recorder.enterSelectionMode((int) selection_mouse.x, (int) selection_mouse.y, m_pieces.size());
		    for (int i = 0; i < m_pieces.size(); i++){
		        recorder.setObjectIndex(i);
		        m_pieces.get(i).draw(gl);
		    }

		    // Set or clear the selection, and set m_hit to be the intersection point.
		    AtomicInteger index = new AtomicInteger(0);
		    m_selection = recorder.exitSelectionMode(index, m_hit) ? m_pieces.get(index.get()) : null;
		    m_hit.w = 1;
		}

		public void displayChanged(GLAutoDrawable arg0, boolean arg1, boolean arg2) {
			
		}

		public void init(GLAutoDrawable arg0) {

			GL2 gl=arg0.getGL().getGL2();
			GLU glu = new GLU();
			
			// set up lighting
			gl.glEnable(GL2.GL_LIGHTING);

			float ambient[]= {0.2f,0.2f,0.2f,1};
			gl.glLightModelfv(GL2.GL_LIGHT_MODEL_AMBIENT , ambient,0);
			
			gl.glEnable(GL2.GL_LIGHT0);
			float position[]= {-0.4f,0.5f,0.7f,1};
			gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_POSITION, position, 0);
			float intensity[]= {1,1,1,1};
			gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_DIFFUSE, intensity, 0);
			
			gl.glEnable(GL2.GL_LIGHT1);
			float position2[]= {0,-0.8f,0.3f,1};
			gl.glLightfv(GL2.GL_LIGHT1, GL2.GL_POSITION, position2, 0);
			float intensity2[]= {1,0,0,0};
			gl.glLightfv(GL2.GL_LIGHT1, GL2.GL_DIFFUSE, intensity2, 0);
			float specIntensity2[]= {1,1,1,1};
			gl.glLightfv(GL2.GL_LIGHT1, GL2.GL_SPECULAR, specIntensity2, 0);
			
			
			gl.glEnable(GL2.GL_COLOR_MATERIAL);
			gl.glColorMaterial(GL.GL_FRONT_AND_BACK, GL2.GL_AMBIENT_AND_DIFFUSE);
			float specColor[]= {1,1,1,1};
			gl.glMaterialfv(GL.GL_FRONT_AND_BACK,GL2.GL_SPECULAR, specColor,0);
			gl.glMaterialf(GL.GL_FRONT_AND_BACK,GL2.GL_SHININESS, 80);

			gl.glEnable(GL2.GL_FOG);
			gl.glFogf(GL2.GL_FOG_START,0.3f);
			gl.glFogf(GL2.GL_FOG_END,1);
			gl.glFogi(GL2.GL_FOG_MODE,GL.GL_LINEAR);
			float fogColor[]={1,0.8f,0.8f,1};
			gl.glFogfv(GL2.GL_FOG_COLOR,fogColor,0);
			gl.glClearColor(fogColor[0],fogColor[1],fogColor[2],fogColor[3]);
			
			GamePiece test1= new GamePiece(gl);
			test1.position = new Vector3d(.2, 0, 0);
			GamePiece test2= new GamePiece(gl);
			test2.position = new Vector3d(.2, .9, 0);
		    m_pieces.add(test1);
		    m_pieces.add(test2);
		}

		public void reshape(GLAutoDrawable arg0, int arg1, int arg2, int arg3, int arg4) {
			
		}

		@Override
		public void dispose(GLAutoDrawable arg0) {
			// TODO Auto-generated method stub
			
		}
	}
	


	static final long serialVersionUID=100;
	


	@Override
	public void mouseWheelMoved(MouseWheelEvent e) {
        m_camera.mouseWheel(-e.getWheelRotation()*10);
	}

	
	public static void main(String[] args) {
		JFrame frame = new JFrame();
		frame.add(new GamePanel());
		frame.setSize(600, 600);
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
}