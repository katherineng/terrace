package gui;
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

import com.jogamp.opengl.util.Animator;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

import javax.vecmath.*;

import terrace.Game;
import terrace.Variant;


public class GamePanel extends GLCanvas implements MouseWheelListener, MouseListener, MouseMotionListener{
	
	/** Determines current drawing mode **/
	private enum Mode {NORMAL, SELECTION, HOVER};

	/*==== General Drawing ====*/
	Camera _camera;
	GamePanel this_gui;
	Vector2d _prevMousePos;
	
	/*==== For Gameplay ====*/
	GUIBoard _board;
	Game _game;
	
	/*==== For Selection/Hoover ====*/
	GamePiece _selection; 		/** The GamePiece that has currently been selected **/
	BoardPiece _hover;
	Vector4d _hit;
	Vector2d _selection_mouse;
	Vector2d _hover_mouse;
	Mode _mode;
	
	
	public GamePanel(){

		/*==== General Drawing ====*/
		super(new GLCapabilities(GLProfile.getDefault()));
		setSize(600,600);
		GraphicListener listener=new GraphicListener();
		setVisible(true);
	    addGLEventListener(listener);
	    Animator animator = new Animator(this);
	    animator.start();
	    this_gui = this;
	    _mode = Mode.NORMAL;
	    
	    /*==== Camera setup ====*/
	    Vector3d center = new Vector3d(0., 0., 0.);
	    Vector3d up = new Vector3d(0.f, 1.f, 0.f);
	    double theta = Math.PI * 1.5f, phi = -.6, fovy = 60., zoom = 1;
	    _camera = new Camera(center, up, theta, phi, fovy, zoom);
	    
	    
	    /*==== Selection ===*/
	    _hit = new Vector4d(0,0,0,0);
	    _selection = null;
	    _hover = null;
	    _hover_mouse = new Vector2d(0,0);
	    
	    
	    /*==== Listeners ====*/
	    this.addMouseWheelListener(this);
	    this.addMouseListener(this);
	    
	    this.addMouseMotionListener(this);
	    
	    setVisible(true);

	}
	

	public class GraphicListener implements GLEventListener{

		/**
		 * Sets up stuff for the camera
		 * @param gl
		 */
		private void applyCameraPerspective(GL2 gl){
			GLU glu = new GLU();
			//set up camera stuff
			double ratio = ((double) this_gui.getWidth())/ this_gui.getHeight();
			Vector3d dir = _camera.fromAngles();
			dir = new Vector3d(dir.x * _camera.zoom, dir.y * _camera.zoom, dir.z * _camera.zoom);
			Vector3d eye = new Vector3d(_camera.center.x - dir.x, _camera.center.y - dir.y, _camera.center.z - dir.z);

			gl.glMatrixMode(GL2.GL_PROJECTION);
			gl.glLoadIdentity();
			glu.gluPerspective(_camera.fovy, ratio, 0.1f, 21000.f);
			glu.gluLookAt(eye.x, eye.y, eye.z,
					eye.x + dir.x, eye.y + dir.y, eye.z + dir.z,
					_camera.up.x, _camera.up.y, _camera.up.z);
			gl.glMatrixMode(GL2.GL_MODELVIEW);
			gl.glLoadIdentity();
		}
		
		/**
		 * Does the actual drawing
		 */
		public void display(GLAutoDrawable arg0) {
			GL2 gl=arg0.getGL().getGL2();
			switch (_mode){
			case SELECTION: // if the user has selected something
				setPieceSelection(gl);
				_mode = (_selection == null) ? Mode.NORMAL : Mode.HOVER;
				break;
			case HOVER:
				setBoardSelection(gl);
				applyCameraPerspective(gl);		
				gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);

				gl.glEnable(GL.GL_DEPTH_TEST);  
				gl.glCullFace(GL.GL_FRONT);
				gl.glEnable(GL.GL_CULL_FACE); 
				gl.glFrontFace(GL.GL_CW); 
				_board.draw(gl);
				break;
			case NORMAL: // regular drawing
				applyCameraPerspective(gl);		
				gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);
				gl.glEnable(GL.GL_DEPTH_TEST);  
				gl.glCullFace(GL.GL_FRONT);
				gl.glEnable(GL.GL_CULL_FACE); 
				gl.glFrontFace(GL.GL_CW); 
				_board.draw(gl);
				break;
			default:// should never get here
				assert(false); 
				break;
			}
		}

		/**
		 * Gets the GamePiece the user has selected. Occurs on click
		 * @param arg0
		 * @return whether or not _selection has been updated
		 */
		boolean setPieceSelection(GL2 gl){
		    SelectionRecorder recorder = new SelectionRecorder(gl);

		    // See if the (x, y) mouse position hits any primitives.
		    ArrayList<GamePiece> pieces = _board.getGamePieces();

		    recorder.enterSelectionMode((int) _selection_mouse.x, (int) _selection_mouse.y, pieces.size());
		    for (int i = 0; i < pieces.size(); i++){
		        recorder.setObjectIndex(i);
		        pieces.get(i).draw(gl);
		    }

		    // Set or clear the selection, and set m_hit to be the intersection point.
		    AtomicInteger index = new AtomicInteger(0);
		    GamePiece newSelection = recorder.exitSelectionMode(index, _hit) ? pieces.get(index.get()) : null;
		    _hit.w = 1;
		    if (newSelection != null){
		    	if (newSelection._piece.getPlayer() == _game.getCurrentPlayer()){
		    		if (_selection == newSelection){
		    			_selection.changeSelection();
		    			if (_hover != null && _hover.isSelected()) {
		    				_hover.changeSelection();
		    				_hover = null;
		    			}
		    			_selection = null;
		    			_mode = Mode.NORMAL;
		    		} else {
				    	if (_selection != null) _selection.changeSelection();
				    	_selection = newSelection;
				    	_selection.changeSelection();
				    	return true;
		    		}
		    	}
		    }
		    return false;
		}
		
		/**
		 * Gets the GamePiece the user has selected. Occurs on click
		 * @param arg0
		 * @return whether or not _selection has been updated
		 */
		boolean setBoardSelection(GL2 gl){
		    SelectionRecorder recorder = new SelectionRecorder(gl);

		    // See if the (x, y) mouse position hits any primitives.
		    List<BoardPiece> pieces = _board.getBoardPieces();
		    recorder.enterSelectionMode((int) _hover_mouse.x, 
		    		(int) _hover_mouse.y, 
		    		pieces.size());
		    for (int i = 0; i < pieces.size(); i++){
		        recorder.setObjectIndex(i);
		        pieces.get(i).draw(gl);
		    }

		    // Set or clear the selection, and set m_hit to be the intersection point.
		    AtomicInteger index = new AtomicInteger(0);
		    BoardPiece newSelection = recorder.exitSelectionMode(index, _hit) ? pieces.get(index.get()) : null;

		    _hit.w = 1;
		    if (newSelection != null && newSelection != _hover){
		    	if (_hover != null) _hover.changeSelection();
		    	_hover = newSelection;
		    	_hover.changeSelection();
		    }
		    return false;
		}

		public void displayChanged(GLAutoDrawable arg0, boolean arg1, boolean arg2) {}

		/**
		 * Setting up rendering environment: 
		 * lighting
		 * making game pieces
		 * setting up board
		 */
		public void init(GLAutoDrawable arg0) {

			GL2 gl=arg0.getGL().getGL2();
			
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
			float fogColor[]={.5f,.5f,.5f,1};
			gl.glFogfv(GL2.GL_FOG_COLOR,fogColor,0);
			gl.glClearColor(fogColor[0],fogColor[1],fogColor[2],fogColor[3]);
			

		    /*==== Gameplay ====*/ 
		    Game game = new Game(2, 8, Variant.STANDARD);
		    _game = game;
		    _board = new GUIBoard(gl, game);
			
		}

		public void reshape(GLAutoDrawable arg0, int arg1, int arg2, int arg3, int arg4) {}

		@Override
		public void dispose(GLAutoDrawable arg0) {}
	}
	


	static final long serialVersionUID=100;
	

	/**
	 * Used for zoom
	 */
	@Override
	public void mouseWheelMoved(MouseWheelEvent e) {
        _camera.mouseWheel(-e.getWheelRotation()*10);
	}

	@Override
	public void mousePressed(MouseEvent e) {
		_prevMousePos = new Vector2d(e.getX(), e.getY());
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		_selection_mouse = new Vector2d(e.getX(), e.getY());
		_mode = Mode.SELECTION;
		repaint();
	}
	
	@Override
	public void mouseDragged(MouseEvent e) {
		if ((e.getModifiers() & InputEvent.BUTTON3_MASK) != 0) {
			Vector2d pos = new Vector2d(e.getX(), e.getY());
			_camera.mouseMove(new Vector2d(pos.x - _prevMousePos.x, pos.y - _prevMousePos.y));
			_prevMousePos = pos;
		}
	}
	
	@Override
	public void mouseMoved(MouseEvent e) {
		if (_selection != null){
			_hover_mouse = new Vector2d(e.getX(), e.getY());
			_mode = Mode.HOVER;
		}
	}
	@Override
	public void mouseReleased(MouseEvent e) {}
	@Override
	public void mouseEntered(MouseEvent e) {}
	@Override
	public void mouseExited(MouseEvent e) {}
	/**
	 * TESTING PURPOSES ONLY
	 * TODO: delete later
	 * @param args
	 */
	public static void main(String[] args) {
		JFrame frame = new JFrame();
		frame.add(new GamePanel());
		frame.setSize(1200, 1200);
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
}
