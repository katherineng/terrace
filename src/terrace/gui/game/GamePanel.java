package terrace.gui.game;

import java.awt.Frame;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.util.List;

import javax.media.opengl.GL;
import javax.media.opengl.GL2;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLCapabilities;
import javax.media.opengl.GLEventListener;
import javax.media.opengl.GLProfile;
import javax.media.opengl.awt.GLJPanel;
import javax.media.opengl.glu.GLU;
import javax.swing.SwingUtilities;

import terrace.GameServer;
import terrace.GameState;
import terrace.Move;
import terrace.Piece;
import terrace.Player;
import terrace.gui.GameScreen;
import terrace.util.Callback;
import terrace.util.Vector2d;
import terrace.util.Vector3d;

import com.google.common.base.Function;
import com.google.common.base.Optional;
import com.jogamp.opengl.util.Animator;

public class GamePanel extends GLJPanel implements MouseWheelListener, MouseListener, MouseMotionListener {
	private static final long serialVersionUID=100;
	
	private final GameScreen _screen;
	
	/** Determines current drawing mode **/
	private enum Mode {NORMAL, SELECTION, HOVER};
	
	/*==== General Drawing ====*/
	private Camera _camera;
	private Vector2d _prevMousePos;
	
	/*==== For Gameplay ====*/
	private GUIBoard _board;
	GameState _game;
	private Optional<Player> _winner = Optional.absent();
	
	/*==== For Selection/Hoover ====*/
	private GamePiece _selection; 		/** The GamePiece that has currently been selected **/
	private BoardTile _hover;
	private List<Move> _possibleMoves;
	private Vector2d _selection_mouse;
	private Vector2d _hover_mouse;
	private Mode _mode;
	
	public GamePanel(GameServer game, Frame frame, GameScreen screen) {
		/*==== General Drawing ====*/
		super(new GLCapabilities(GLProfile.getDefault()));
		setSize(600,600);
		GraphicListener listener=new GraphicListener();
		setVisible(true);
	    addGLEventListener(listener);
	    Animator animator = new Animator(this);
	    animator.start();
	    _mode = Mode.NORMAL;
	    _screen = screen;
	    
	    /*==== Gameplay ====*/ 
	    _game = game.getState();
	    _board = GUIBoardFactory.create(GamePanel.this);
	    _board.resetPieces();
	    _screen.setCurrPlayer(_game.getActivePlayer());
	    game.addUpdateStateCB(new Callback<GameState>() {
			@Override
			public void call(GameState state) {
				_game = state;
				_board.resetPieces();
				_board.clearMoves();
				
				if (!_game.getWinner().isPresent()) {
					_screen.setCurrPlayer(_game.getActivePlayer());
				}
				
				SwingUtilities.invokeLater(new Runnable() {
					@Override
					public void run() {
						repaint();
					}
				});
			}
	    });
	    game.addWinnerCB(new Callback<Player>() {
			@Override
			public void call(final Player winner) {
				_winner = Optional.of(winner);
				SwingUtilities.invokeLater(new Runnable() {
					@Override
					public void run() {
						_screen.setWinner(winner);
					}
				});
			}
	    });
	    
	    /*==== Camera setup ====*/
	    Vector3d center = new Vector3d(0., 0., 0.);
	    Vector3d up = new Vector3d(0.f, 1.f, 0.f);
	    double theta = Math.PI * 1.5f, phi = -.6, fovx = 60., zoom = 1.2;
	    _camera = new Camera(center, up, theta, phi, fovx, zoom);
	    
	    /*==== Selection ===*/
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
		private void applyCameraPerspective(GL2 gl) {
			GLU glu = new GLU();
			//set up camera stuff
			double ratio = ((double) getWidth()) / getHeight();
			Vector3d dir = _camera.fromAngles();
			dir = new Vector3d(dir.x * _camera.zoom, dir.y * _camera.zoom, dir.z * _camera.zoom);
			Vector3d eye = new Vector3d(_camera.center.x - dir.x, _camera.center.y - dir.y, _camera.center.z - dir.z);

			gl.glMatrixMode(GL2.GL_PROJECTION);
			gl.glLoadIdentity();
			perspectiveFovX(gl, _camera.fovx, ratio, 0.1, 21000.0);
			glu.gluLookAt(eye.x, eye.y, eye.z,
					eye.x + dir.x, eye.y + dir.y, eye.z + dir.z,
					_camera.up.x, _camera.up.y, _camera.up.z);
			gl.glMatrixMode(GL2.GL_MODELVIEW);
			gl.glLoadIdentity();
		}
		
		private void perspectiveFovX(GL2 gl, double fovx, double aspect, double znear, double zfar) {
			double m[] = new double[16];
			
			double xmax = znear * Math.tan(fovx * Math.PI / 360.0);
			double xmin = -xmax;
			
			double ymin = xmin / aspect;
			double ymax = xmax / aspect;
			
			m[0 + 0*4] = (2.0 * znear) / (xmax - xmin);
			m[1 + 1*4] = (2.0 * znear) / (ymax - ymin);
			m[2 + 2*4] = -(zfar + znear) / (zfar - znear);
			
			m[0 + 2*4] = (xmax + xmin) / (xmax - xmin);
			m[1 + 2*4] = (ymax + ymin) / (ymax - ymin);
			m[3 + 2*4] = -1.0;
			
			m[2 + 3*4] = -(2.0 * zfar * znear) / (zfar - znear);
			
			gl.glMultMatrixd(m, 0);
		}
		
		/**
		 * Does the actual drawing
		 */
		public void display(GLAutoDrawable arg0) {
			GL2 gl=arg0.getGL().getGL2();
			
			switch (_mode) {
			case SELECTION: // activated when the user has selected something
				Optional<GamePiece> pieceSelection = getSelection(gl, _selection_mouse, _board.getGamePieces());
				
				if (pieceSelection.isPresent()) {
					setPieceSelection(pieceSelection.get());
				} else {
					Optional<BoardTile> boardSelection = getSelection(gl, _selection_mouse, _board.getBoardPieces());
					if (boardSelection.isPresent() && _selection != null) {
						Optional<BoardTile> hoverTile = getSelection(gl, _hover_mouse, _board.getBoardPieces());
						
						if(hoverTile.isPresent()) setMove(hoverTile.get());
					}
				}
				_mode = (_selection == null) ? Mode.NORMAL : Mode.HOVER;
				break;
			case HOVER: // the user has selected something and is moving over objects
				Optional<BoardTile> boardSelection = getSelection(gl, _hover_mouse, _board.getBoardPieces());
				if (boardSelection.isPresent()) setBoardSelection(boardSelection.get());
				normalDraw(gl);
				break;
			case NORMAL: // regular drawing
				normalDraw(gl);
				break;
			default:// should never get here
				assert(false); 
				break;
			}
		}
		
		/**
		 * draws everything normally
		 * @param gl
		 */
		private void normalDraw(GL2 gl) {
			applyCameraPerspective(gl);		
			gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);

			gl.glEnable(GL.GL_DEPTH_TEST);  
			gl.glCullFace(GL.GL_FRONT);
			gl.glEnable(GL.GL_CULL_FACE); 
			gl.glFrontFace(GL.GL_CW); 
			_board.draw(gl);
		}
		
		/**
		 * Sets the GamePiece the user has selected. Occurs on click
		 * @param newSelection - the GamePiece the user has clicked. Can not be null
		 * @return whether or not the user selected their own game piece (i.e. selecting change anything)
		 */
		private boolean setPieceSelection(GamePiece newSelection) {
			assert(newSelection != null);
			
			//only act of user is the same
			if (
					newSelection.getPiece().getPlayer() == _game.getActivePlayer() &&
					_game.getActivePlayer() instanceof LocalPlayer
			) { 
				clearPossible();
				
				if (_selection == newSelection) { // if they are the same, that means user unselected
					//change selection settings
					_selection.changeSelection();
					_selection = null;
					
					// change hover settings
					if (_hover != null && _hover.isSelected()) _hover.changeSelection();
					_hover = null;
					
					// change mode
					_mode = Mode.NORMAL;
				} else { // set selection to something new. Remains in selection mode
					_possibleMoves = _game.getBoard().getMoves(newSelection.getPiece());
					for (Move move : _possibleMoves) {
						_board.posToTile(move.getTo()).setMoveColor(_board.getPlayerColors(newSelection.getPiece().getColor()));
					}
					if (_selection != null) _selection.changeSelection(); // unselect old thing
					_selection = newSelection;
					_selection.changeSelection();
				}
				
				return true;
			}
			return false;
		}
		
		/**
		 * Makes a move
		 * @param newSelection - the BoardTile where you want to move your selection to.
		 * Can't be null
		 * @return true if move successfully made, false otherwise
		 */
		private boolean setMove(BoardTile newSelection) {
			assert(newSelection != null);
			
			if (_game.getActivePlayer() instanceof LocalPlayer) {
				LocalPlayer p = (LocalPlayer)_game.getActivePlayer();
				
				Piece captured = _game.getBoard().getPieceAt(newSelection.getPosn());
				Move m = new Move(_selection.getPiece(), newSelection.getPosn(), captured);
				if (_game.isValid(m, p)) {
					p.sendMove(m);
					clearPossible();
				} else {
					_hover.incorrect();
					_mode = Mode.HOVER;
					return false;
				}
			}
			
			if (_selection != null && _selection.isSelected()) _selection.changeSelection();
			if (_hover != null && _hover.isSelected()) _hover.changeSelection();
			_hover = null;
			_selection = null;
			_mode = Mode.NORMAL;
			return true;
		}
		
		/**
		 * Sets what is currently being hovered over
		 * @param newSelection - a BoardPiece that is being hovered over
		 * @return true if the selection has changed, false otherwise
		 */
		private boolean setBoardSelection(BoardTile newSelection) {
			assert(newSelection != null);
			
		    if (newSelection != _hover) {
		    	if (_hover != null) _hover.changeSelection();
		    	_hover = newSelection;
		    	_hover.changeSelection();
		    	return true;
		    }
		    return false;
		}
		
		/*===================
		 * SELECTION HELPERS
		 * =================*/
		
		/**
		 * clears the list of possible moves
		 */
		private void clearPossible() {
			//change board tile settings
			if (_possibleMoves != null) {
				for (Move move : _possibleMoves) _board.posToTile(move.getTo()).setMoveColor(null);
				_possibleMoves.clear();
			}
		}
		
		/**
		 * Gets the object that is intersecting with the given mouse coordinate
		 * @param gl          a GL2 object
		 * @param mouse_coord a Vector2d representing the mouse's position
		 * @param objs        the objects to be tested for selection
		 * @return - a BoardPiece intersecting with <mouse_coord>
		 */
		private <T extends Drawable> Optional<T> getSelection(GL2 gl, Vector2d mouse_coord, final List<T> objs) {
			SelectionRecorder recorder = new SelectionRecorder(gl);
		    
		    // See if the (x, y) mouse position hits any primitives.
		    recorder.enterSelectionMode((int) mouse_coord.x, (int) mouse_coord.y, objs.size());
		    
		    int i = 0;
		    for (T obj : objs) {
		    	recorder.setObjectIndex(i);
		        obj.draw(gl);
		        i++;
		    }
		    
		    return recorder.exitSelectionMode().transform(new Function<Integer, T>() {
		    	@Override
		    	public T apply(Integer index) {
		    		return objs.get(index);
		    	}
		    });
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
		}
		
		public void reshape(GLAutoDrawable arg0, int arg1, int arg2, int arg3, int arg4) {}
		
		@Override
		public void dispose(GLAutoDrawable arg0) {}
	}
	
	/**
	 * Used for zoom
	 */
	@Override
	public void mouseWheelMoved(MouseWheelEvent e) {
		_camera.mouseWheel(-e.getWheelRotation());
	}
	
	@Override
	public void mousePressed(MouseEvent e) {
		_prevMousePos = new Vector2d(e.getX(), e.getY());
	}
	
	@Override
	public void mouseClicked(MouseEvent e) {
		if (!_winner.isPresent()) {
			
			_selection_mouse = new Vector2d(e.getX(), e.getY());
			_mode = Mode.SELECTION;
			repaint();
		} else {

			System.out.println(_winner.get());
		}
	}
	
	@Override
	public void mouseDragged(MouseEvent e) {
		Vector2d pos = new Vector2d(e.getX(), e.getY());
		_camera.mouseMove(new Vector2d(pos.x - _prevMousePos.x, pos.y - _prevMousePos.y));
		_prevMousePos = pos;
	}
	
	@Override
	public void mouseMoved(MouseEvent e) {
		if (_selection != null && !_winner.isPresent()) {
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
}
