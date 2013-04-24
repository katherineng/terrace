package terrace;

import java.util.*;

//import com.google.common.base.Optional;

/**
 * The default 8x8 square board
 * @author kwng
 *
 */
public class DefaultBoard implements Board<DefaultBoard> {
	private final int _dimensions;
	private static int[][] _elevations;
	private Piece[][] _board;
	private Variant _variant;

	public DefaultBoard(int dimensions, Variant variant) {
		_dimensions = dimensions;
		_board = new Piece[_dimensions][_dimensions];
		_variant = variant;
		_elevations = new int[_dimensions][_dimensions];
	}
	
	/**
	 * Fills in the elevations array with the appropriate elevations for each position
	 * Should only be invoked when setting up the board for the first time
	 */
	public void setUp() {
		int halfWidth = _dimensions / 2;
		
		for (int i = 0; i < halfWidth; i++) { // lower left quadrant
			for (int j = 0; j < halfWidth; j++) {
				_elevations[i][j] = Math.max(i, j);
			}
		}
		
		for (int i = halfWidth; i < _dimensions; i++) { // lower right quadrant
			for (int j = 0; j < halfWidth; j++) {
				_elevations[i][j] = Math.min(i, _dimensions - j - 1);
			}
		}
		
		for (int i = 0; i < halfWidth; i++) { // upper left quadrant
			for (int j = halfWidth; j < _dimensions; j++) {
				_elevations[i][j] = Math.min(_dimensions - i - 1, j);
			}
		}
		
		for (int i = halfWidth; i < _dimensions; i++) { // upper right quadrant
			for (int j = halfWidth; j < _dimensions; j++) {
				_elevations[i][j] = Math.max(_dimensions - i - 1, _dimensions - j - 1);
			}
		}
	}
	
	@Override
	public int getElevation(Posn p) {
		return _elevations[p.x][p.y];
	}
	
	@Override
	public List<Posn> getTerracePosns(Posn p) {
		int halfWidth = _dimensions / 2;
		int elevation = getElevation(p);
		List<Posn> positions = new ArrayList<Posn>();
		
		if (p.x < halfWidth && p.y < halfWidth) {
			for (int j = halfWidth - 1; j >= 0; j--) {
				for (int i = 0; i < halfWidth; i++) {
					if (Math.max(i, j) == elevation) positions.add(new Posn(i, j));
				}
			}
		} else if (p.x >= halfWidth && p.y < halfWidth) {
			for (int j = 0; j < halfWidth; j++) {
				for (int i = halfWidth; i < _dimensions; i++) {
					if (Math.min(i, _dimensions - j - 1) == elevation) positions.add(new Posn(i, j));
				}
			}
		} else if (p.x < halfWidth && p.y >= halfWidth) {
			for (int j = halfWidth; j < _dimensions; j++) {
				for (int i = 0; i < halfWidth; i++) {
					if (Math.min(_dimensions - i - 1, j) == elevation) positions.add(new Posn(i, j));
				}
			}
		} else { 
			for (int j = _dimensions - 1; j >= halfWidth; j--) {
				for (int i = halfWidth; i < _dimensions; i++) {
					if (Math.max(_dimensions - i - 1, _dimensions - j - 1) == elevation) positions.add(new Posn(i, j));
				}
			}
		}
		
		return positions;
	}
	
	private void getSameTerraceMoves(Piece piece, Set<Posn> positions) {
		Posn currPosn = piece.getPosn();
		
		List<Posn> sameTerrace = getTerracePosns(currPosn);
		int numPosns = sameTerrace.size();
		int idx = sameTerrace.indexOf(currPosn);
		
		for (int i = idx + 1; i < numPosns; i++) {
			Posn posn = sameTerrace.get(i);
			Piece p = _board[posn.x][posn.y];
			if (p != null && !p.getPlayer().equals(piece.getPlayer())) {
				break;
			} else if (p != null) {
				continue;
			} else {
				positions.add(posn);
			}
		}
		
		for (int i = idx - 1; i >= 0; i--) {
			Posn posn = sameTerrace.get(i);
			Piece p = _board[posn.x][posn.y];
			if (p != null && !p.getPlayer().equals(piece.getPlayer())) {
				break;
			} else if (p != null) {
				continue;
			} else {
				positions.add(posn);
			}
		}
	}
	
	private void getUpDownMoves(Piece piece, Set<Posn> positions) {
		Posn currPosn = piece.getPosn();
		
		List<Posn> neighbors = getNeighbors(currPosn);
		for (Posn posn: neighbors) {
			Piece p = _board[posn.x][posn.y];
			int neighborElevation = _elevations[posn.x][posn.y];
			int currElevation = _elevations[currPosn.x][currPosn.y];
			
			if (neighborElevation > currElevation) {  // up
				if (posn.x != currPosn.x && posn.y != currPosn.y) { // diagonally up
					if (_variant == Variant.AGGRESSIVE && p != null && p.compareTo(piece) < 0) positions.add(posn);
					else if (p == null) positions.add(posn);
				} 
				
				else if (p == null) { // straight up
					positions.add(posn);
				}
			} 
			
			else if (neighborElevation < currElevation) { // down
				if (posn.x != currPosn.x && posn.y != currPosn.y) { // diagonal down capture
					if (p != null) {
						if (_variant == Variant.AGGRESSIVE && p.compareTo(piece) <= 1) {
							positions.add(posn);
						} else if (p.compareTo(piece) <= 0) {
							positions.add(posn);
						}
					}
				}
				
				else if (p == null) {
					positions.add(posn);
				}
			}
		}
	}

	@Override
	public Set<Posn> getMoves(Piece piece) {
		Set<Posn> positions = new HashSet<Posn>();
		
		// same terrace, same quadrant, can't jump over opponent
		getSameTerraceMoves(piece, positions);
		
		// up or down a terrace
		getUpDownMoves(piece, positions);
		
		return positions;
	}

	@Override
	public Piece getPieceAt(Posn posn) {
		return _board[posn.x][posn.y];
	}
	
	@Override
	public void setPiece(int x, int y, Piece piece) {
		_board[x][y] = piece;
	}
	
	@Override
	public List<Posn> getNeighbors(Posn posn) {
		List<Posn> neighbors = new ArrayList<Posn>();
		for(int i = posn.x -1; i < posn.x + 1; i ++) {
			for(int j = posn.y - 1; j < posn.y + 1; j ++) {
				if ((i != posn.x) && (j != posn.y)) {
					if (((i >= 0) && (i < _dimensions)) && ((j >= 0) && (j < _dimensions))) {
						neighbors.add(new Posn(i, j));
					}
				}
			}
		}
		return neighbors;
	}
	
	@Override
	public int getWidth() {
		return _dimensions;
	}

	@Override
	public int getHeight() {
		return _dimensions;
	}
	
	@Override
	public DefaultBoard copyBoard() {
		DefaultBoard copy = new DefaultBoard(_dimensions, _variant);
		
		for (int i = 0; i < _dimensions; i++) {
			for (int j = 0; j < _dimensions; j++) {
				Piece p = _board[i][j];
				if (p != null) {
					copy.setPiece(i, j, new Piece(p.getSize(), p.isTPiece(), p.getPosn(), p.getPlayer()));
				}
			}
		}
		return null;
	}

	@Override
	public Piece movePiece(Posn from, Posn to) {
		Piece toMove = _board[from.x][from.y];
		
		_board[from.x][from.y] = null;
		toMove.updatePosn(to);
		
		Piece captured = _board[to.x][to.y];
		_board[to.x][to.y] = toMove;
		
		return captured;
		
	}
	
	public String elevationsToString() {
		String elevations = "";
		
		for (int y = _dimensions - 1; y >= 0; y--) {
			elevations += "[ ";
			for (int x = 0; x < _dimensions; x++) {
				elevations += _elevations[x][y] + " ";
			}
			elevations += "]\n";
		}
		
		return elevations;
	}
	
	public String piecesToString() {
		String pieces = "";
		
		for (int y = _dimensions - 1; y >= 0; y--) {
			pieces += "[ ";
			for (int x = 0; x < _dimensions; x++) {
				Piece p = _board[x][y];
				if (p != null) {
					pieces += p.toString() + " ";
				} else {
					pieces += "(EMPTY) ";
				}
			}
			pieces += "]\n";
		}
		
		return pieces;
	}

}
