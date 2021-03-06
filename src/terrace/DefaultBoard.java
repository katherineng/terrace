package terrace;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import terrace.util.Posn;

//import com.google.common.base.Optional;

/**
 * The default 8x8 square board
 * @author kwng
 *
 */
public class DefaultBoard extends Board {
	final int _dimensions;
	static HashMap<Integer, int[][]> _elevationsMap = new HashMap<Integer, int[][]>();;
	
	public DefaultBoard(int dimensions, Variant variant) {
		_dimensions = dimensions;
		_pieces = new Piece[_dimensions][_dimensions];
		_variant = variant;
		setUp();
	}
	
	@Override
	public Board copy() {
		Board copy = new DefaultBoard(_dimensions, _variant);
		for (int x = 0; x < getWidth(); x++) {
			for (int y = 0; y < getHeight(); y++) {
				if (_pieces[x][y] != null) {
					copy._pieces[x][y] = _pieces[x][y].copy();
				}
			}
		}
		return copy;
	}
	
	/**
	 * Fills in the elevations array with the appropriate elevations for each position
	 * Should only be invoked when setting up the board for the first time
	 */
	public void setUp() {
		if (_elevationsMap.get(_dimensions) == null) { 
			int halfWidth = _dimensions / 2;
			int[][] elevations = new int[_dimensions][_dimensions];
			
			for (int i = 0; i < halfWidth; i++) { // lower left quadrant
				for (int j = 0; j < halfWidth; j++) {
					elevations[i][j] = Math.max(i, j);
				}
			}
			
			for (int i = halfWidth; i < _dimensions; i++) { // lower right quadrant
				for (int j = 0; j < halfWidth; j++) {
					elevations[i][j] = Math.min(i, _dimensions - j - 1);
				}
			}
			
			for (int i = 0; i < halfWidth; i++) { // upper left quadrant
				for (int j = halfWidth; j < _dimensions; j++) {
					elevations[i][j] = Math.min(_dimensions - i - 1, j);
				}
			}
			
			for (int i = halfWidth; i < _dimensions; i++) { // upper right quadrant
				for (int j = halfWidth; j < _dimensions; j++) {
					elevations[i][j] = Math.max(_dimensions - i - 1, _dimensions - j - 1);
				}
			}
			
			_elevationsMap.put(_dimensions, elevations);
		}
	}
	
	@Override
	public int getElevation(Posn p) {
		return _elevationsMap.get(_dimensions)[p.x][p.y];
	}
	
	public int getElevation(int x, int y) {
		return _elevationsMap.get(_dimensions)[x][y];
	}
	
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
	
	
	/**
	 * Helper method to find all the possible positions on the same terrace to which a given piece may move
	 * @param piece The piece for which to find possible moves
	 * @param moves A reference to a set of positions to which the possible positions should be added
	 */
	private void getSameTerraceMoves(Piece piece, List<Move> moves) {
		Posn currPosn = piece.getPosn();
		
		List<Posn> sameTerrace = getTerracePosns(currPosn);
		int numPosns = sameTerrace.size();
		int idx = sameTerrace.indexOf(currPosn);
		
		for (int i = idx + 1; i < numPosns; i++) {
			Posn posn = sameTerrace.get(i);
			Piece p = _pieces[posn.x][posn.y];
			if (p != null && !p.getColor().equals(piece.getColor())) {
				break;
			} else if (p != null) {
				continue;
			} else {
				moves.add(new Move(piece, posn));
			}
		}
		
		for (int i = idx - 1; i >= 0; i--) {
			Posn posn = sameTerrace.get(i);
			Piece p = _pieces[posn.x][posn.y];
			if (p != null && !p.getColor().equals(piece.getColor())) {
				break;
			} else if (p != null) {
				continue;
			} else {
				moves.add(new Move(piece, posn));
			}
		}
	}
	
	/**
	 * Helper method that adds all the possible positions on another terrace that a given piece can make
	 * @param piece The piece for which to find the possible positions
	 * @param moves A reference to set of possible positions to which the positions should be added
	 */
	private void getUpDownMoves(Piece piece, List<Move> moves) {
		Posn currPosn = piece.getPosn();
		
		List<Posn> neighbors = getNeighbors(currPosn);
		for (Posn posn: neighbors) {
			Piece p = getPieceAt(posn);
			int neighborElevation = _elevationsMap.get(_dimensions)[posn.x][posn.y];
			int currElevation = _elevationsMap.get(_dimensions)[currPosn.x][currPosn.y];
			
			if (neighborElevation > currElevation) {  // up
				if (posn.x != currPosn.x && posn.y != currPosn.y) { // diagonally up
					if (_variant == Variant.AGGRESSIVE && p != null && p.compareTo(piece) < 0) {
						moves.add(new Move(piece, posn, p));
					}
					else if (p == null) {
						moves.add(new Move(piece, posn));
					}
				} 
				
				else if (p == null) { // straight up
					moves.add(new Move(piece, posn));
				}
			} 
			
			else if (neighborElevation < currElevation) { // down
				if (posn.x != currPosn.x && posn.y != currPosn.y) { // diagonal down capture
					if (p != null) {
						if (_variant == Variant.AGGRESSIVE && p.compareTo(piece) <= 1) {
							moves.add(new Move(piece, posn, p));
						} else if (p.compareTo(piece) <= 0) {
							moves.add(new Move(piece, posn, p));
						}
					}
				}
				
				else if (p == null) {
					moves.add(new Move(piece, posn));
					if (_variant == Variant.DOWNHILL) {
						getDownhillMoves(posn, piece, moves);
					}
				}
			}
		}
	}

	/**
	 * Helper method to get all the positions downhill from a given position to which the given piece
	 * can move in the downhill rule variant of the game
	 * @param start A position that is downhill and adjacent to the given piece to be moved
	 * @param piece The piece for which to find possible moves
	 * @param moves A reference to the set of positions representing the possible moves
	 */
	private void getDownhillMoves(Posn start, Piece piece, List<Move> moves) {
		Posn currPosn = piece.getPosn();
		int prevElevation = _elevationsMap.get(_dimensions)[start.x][start.y];
		
		if (currPosn.x != start.x) {
			int increment = start.x - currPosn.x;
			for (int i = start.x + increment; i >= 0 && i < _dimensions; i += increment) {
				Piece p = _pieces[i][start.y];
				if (p != null) break;
				
				int currElevation = _elevationsMap.get(_dimensions)[i][start.y];
				if (prevElevation - currElevation != 1) break;
				prevElevation = currElevation;
				moves.add(new Move(piece, new Posn(i, start.y), p));
			}
		}
		else if (currPosn.y != start.y) {
			int increment = start.y - currPosn.y;
			for (int i = start.y + increment; i >= 0 && i < _dimensions; i += increment) {
				Piece p = _pieces[start.x][i];
				if (p != null) break;
				int currElevation = _elevationsMap.get(_dimensions)[start.x][i];
				if (prevElevation - currElevation != 1) break;
				prevElevation = currElevation;
				moves.add(new Move(piece, new Posn(start.x, i), p));
			}
		}
	}

	@Override
	public List<Move> getMoves(Piece piece) {
		List<Move> moves = new LinkedList<>();
		
		// same terrace, same quadrant, can't jump over opponent
		getSameTerraceMoves(piece, moves);
		
		// up or down a terrace
		getUpDownMoves(piece, moves);
		
		return moves;
	}
	
	@Override
	public List<Posn> getNeighbors(Posn posn) {
		List<Posn> neighbors = new ArrayList<Posn>();
		
		int startX = Math.max(0, posn.x - 1);
		int endX = Math.min(posn.x + 1, _dimensions - 1);
		int startY = Math.max(0, posn.y - 1);
		int endY = Math.min(posn.y + 1, _dimensions - 1);
		
		for(int i = startX; i <= endX; i++) {
			for(int j = startY; j <= endY; j++) {
				if ((i != posn.x) || (j != posn.y)) {
					neighbors.add(new Posn(i, j));
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
	
	public int getDimensions() {
		return _dimensions;
	}
	
	public String elevationsToString() {
		String elevations = "";
		
		for (int y = getHeight() - 1; y >= 0; y--) {
			elevations += "[ ";
			for (int x = 0; x < getWidth(); x++) {
				elevations += _elevationsMap.get(_dimensions)[x][y] + " ";
			}
			elevations += "]\n";
		}
		
		return elevations;
	}
}
