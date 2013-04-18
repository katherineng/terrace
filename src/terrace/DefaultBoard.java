package terrace;

import java.util.*;

//import com.google.common.base.Optional;

public class DefaultBoard implements Board<DefaultBoard> {
	private final int _dimensions;
	private static int[][] _elevations;
	private Piece[][] _board;

	public DefaultBoard() {
		_dimensions = 8;
		_board = new Piece[_dimensions][_dimensions];
	}
	
	public void setUp() {
		int halfWidth = _dimensions / 2;
		
		// lower left quadrant
		for (int i = 0; i < halfWidth; i++) {
			for (int j = 0; j < halfWidth; j++) {
				_elevations[i][j] = Math.max(i, j);
			}
		}
		
		// lower right quadrant
		for (int i = halfWidth; i < _dimensions; i++) {
			for (int j = 0; j < halfWidth; j++) {
				_elevations[i][j] = Math.min(i, _dimensions - j - 1);
			}
		}
		
		// upper left quadrant
		for (int i = 0; i < halfWidth; i++) {
			for (int j = halfWidth; j < _dimensions; j++) {
				_elevations[i][j] = Math.min(_dimensions - i - 1, j);
			}
		}
		
		// upper right quadrant
		for (int i = halfWidth; i < _dimensions; i++) {
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
		
		if (p.x < halfWidth && p.y < halfWidth) { //
			for (int i = 0; i < halfWidth; i++) {
				for (int j = halfWidth - 1; j >= 0; j--) {
					if (Math.max(i, j) == elevation) positions.add(new Posn(i, j));
				}
			}
		} else if (p.x >= halfWidth && p.y < halfWidth) {
			for (int i = halfWidth; i < _dimensions; i++) {
				for (int j = 0; j < halfWidth; j++) {
					if (Math.min(i, _dimensions - j - 1) == elevation) positions.add(new Posn(i, j));
				}
			}
		} else if (p.x < halfWidth && p.y >= halfWidth) {
			for (int i = 0; i < halfWidth; i++) {
				for (int j = halfWidth; j < _dimensions; j++) {
					if (Math.min(_dimensions - i - 1, j) == elevation) positions.add(new Posn(i, j));
				}
			}
		} else { //
			for (int i = halfWidth; i < _dimensions; i++) {
				for (int j = _dimensions - 1; j >= halfWidth; j--) {
					if (Math.max(_dimensions - i - 1, _dimensions - j - 1) == elevation) positions.add(new Posn(i, j));
				}
			}
		}
		
		return positions;
	}

	@Override
	public Set<Posn> getMoves(Piece piece) {
		Set<Posn> positions = new HashSet<Posn>();
		Posn currPosn = piece.getPosn();
		
		// same terrace, same quadrant, can't jump over opponent
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
		
		// up straight or diagonally
		// down straight or diagonally for capture if piece is same/smaller
		List<Posn> neighbors = getNeighbors(currPosn);
		for (Posn posn: neighbors) {
			Piece p = _board[posn.x][posn.y];
			int neighborElevation = _elevations[posn.x][posn.y];
			int currElevation = _elevations[currPosn.x][currPosn.y];
			if (neighborElevation > currElevation && p == null) {  // up
				positions.add(posn);
			} else if (neighborElevation < currElevation) {
				if ((posn.x == currPosn.x || posn.y == currPosn.y) && p == null) { // straight down
					positions.add(posn);
				} else if (posn.x != currPosn.x && posn.y != currPosn.y) { // diagonal down capture
					if (p != null && p.getSize().compareTo(piece.getSize()) <= 0) {
						positions.add(posn);
					}
				}
			}
		}
		
		return positions;
	}

	@Override
	public Piece getPieceAt(Posn posn) {
		return _board[posn.x][posn.y];
	}
	
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
		DefaultBoard copy = new DefaultBoard();
		
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
	public void movePiece(Posn from, Posn to) {
		Piece p = _board[from.x][from.y];
		
		_board[from.x][from.y] = null;
		p.updatePosn(to);
		_board[to.x][to.y] = p;
		
	}

}
