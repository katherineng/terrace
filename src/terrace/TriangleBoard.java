package terrace;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import terrace.util.Posn;

import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;

public class TriangleBoard extends Board {
	private final int _dimensions;
	private static HashMap<Integer, int[][]> _elevationsMap;
	public TriangleBoard(int dimensions) {
		_dimensions = dimensions;
		_board = new Piece[_dimensions][_dimensions*2];
		_elevationsMap = new HashMap<Integer, int[][]>();
	}
	public void setUp() {
		if (_elevationsMap.get(_dimensions) == null) {
			int[][] elevations  = new int[_dimensions][_dimensions*2];
			int diagonalMax = _dimensions - 1;
			int diagonalMin = 0;
			int currY = 0;
			boolean secondHalf = false;
			//fill in the middle diagonal
			for(int x = 0; x < _dimensions; x ++) {
				if(secondHalf) {
					elevations[x][currY] = diagonalMin;
					elevations[x][currY + 1] = diagonalMin - 1;
					diagonalMin -= 2;
				} else {
					elevations[x][currY] = diagonalMin;
					if(diagonalMin < diagonalMax) {
						elevations[x][currY + 1] = diagonalMin + 1;
						if(diagonalMin + 1== diagonalMax) {
							secondHalf = true;
							diagonalMin++;
						} else {
							diagonalMin +=2;
						}
					} else {
						elevations[x][currY + 1] = diagonalMin;
						secondHalf = true;
						diagonalMin --;
					}
				}
				currY += 2;
			}
			//fill in the rest
			diagonalMin ++;
			int yOffset = 2;
			for(int xOffset = 1; xOffset < _dimensions; xOffset ++) {
				currY = 0;
				diagonalMin += 2;
				secondHalf = false;
				diagonalMax ++;
				for(int x = 0; x < _dimensions - xOffset; x ++) {
					if(secondHalf) {
						elevations[x + xOffset][currY] = diagonalMin;
						elevations[x + xOffset][currY + 1] = diagonalMin - 1;

						elevations[x][currY + yOffset] = diagonalMin;
						elevations[x][currY + yOffset + 1] = diagonalMin -1;

						diagonalMin -= 2;

					} else {
						elevations[x + xOffset][currY] = diagonalMin;

						elevations[x][currY + yOffset] = diagonalMin;

						if(diagonalMin < diagonalMax) {
							elevations[x + xOffset][currY + 1] = diagonalMin + 1;

							elevations[x][currY + yOffset + 1] = diagonalMin + 1;

							if(diagonalMin + 1== diagonalMax) {
								secondHalf = true;
								diagonalMin++;
							} else {
								diagonalMin +=2;
							}
						} else {
							elevations[x + xOffset][currY + 1] = diagonalMin;

							elevations[x][currY + yOffset + 1] = diagonalMin;

							secondHalf = true;
							diagonalMin --;
						}
					}
					currY +=2;
				}
				yOffset +=2;
				diagonalMin ++;
			}
			_elevationsMap.put(_dimensions, elevations);
		}
	}
	
	@Override
	public int getWidth() {
		return _dimensions;
	}

	@Override
	public int getHeight() {
		return _dimensions * 2;
	}

	@Override
	public List<Posn> getNeighbors(Posn posn) {
		List<Posn> neighborPosns = getCornerNeighbors(posn);
		neighborPosns.addAll(getEdgeNeighbors(posn));
		return neighborPosns;
	}
	
	private List<Posn> getCornerNeighbors(Posn posn) {
		List<Posn> cornerNeighbors;
		if(posn.getY() % 2 == 0) {
			cornerNeighbors = Arrays.asList(
					new Posn(posn.getX(), posn.getY() - 2),
					new Posn(posn.getX() + 1, posn.getY() - 2),
					new Posn(posn.getX() + 1, posn.getY() - 1),
					new Posn(posn.getX() - 1, posn.getY()),
					new Posn(posn.getX() + 1, posn.getY()),
					new Posn(posn.getX() - 1, posn.getY() + 2), 
					new Posn(posn.getX(), posn.getY() + 2), 
					new Posn(posn.getX() - 1, posn.getY() + 3)
			);
		} else {
			cornerNeighbors = Arrays.asList(
					new Posn(posn.getX() + 1, posn.getY() - 3),
					new Posn(posn.getX(), posn.getY() - 2),
					new Posn(posn.getX() + 1, posn.getY() - 2),
					new Posn(posn.getX() + 1, posn.getY() - 1),
					new Posn(posn.getX() - 1, posn.getY()),
					new Posn(posn.getX() + 1, posn.getY()),
					new Posn(posn.getX() + 1, posn.getY() + 1),
					new Posn(posn.getX() - 1, posn.getY() + 2), 
					new Posn(posn.getX(), posn.getY() + 2)
			);
		}
		return new LinkedList<>(Collections2.filter(cornerNeighbors, new Predicate<Posn>() {
			@Override
			public boolean apply(Posn x) {
				return inBounds(x);
			}
		}));
	}
	
	private List<Posn> getEdgeNeighbors(Posn posn) {
		List<Posn> edgeNeighbors;
		if(posn.getY() % 2 == 0) {
			edgeNeighbors = Arrays.asList(
					new Posn(posn.getX() - 1, posn.getY() + 1),
					new Posn(posn.getX(), posn.getY() + 1),
					new Posn(posn.getX(), posn.getY() - 1)
			);
		} else {
			edgeNeighbors = Arrays.asList(
					new Posn(posn.getX() - 1, posn.getY() - 1),
					new Posn(posn.getX(), posn.getY() - 1),
					new Posn(posn.getX(), posn.getY() + 1)
			);
		}
		return new LinkedList<>(Collections2.filter(edgeNeighbors, new Predicate<Posn>() {
			@Override
			public boolean apply(Posn x) {
				return inBounds(x);
			}
		}));
	}
	
	private boolean inBounds(Posn p) {
		return p.getX() >= 0 && p.getY() >= 0 && p.getX() < _dimensions && p.getY() < _dimensions * 2;
	}
	
	private int getElevationAt(Posn p) {
		return _elevationsMap.get(_dimensions)[p.getX()][p.getY()];
	}
	
	@Override
	public List<Move> getMoves(Piece piece) {
		List<Posn> cornerNeighbors = getCornerNeighbors(piece.getPosn());
		List<Posn> edgeNeighbors = getEdgeNeighbors(piece.getPosn());
		List<Move> possibleMoves = new LinkedList<>();
		Posn currentPosn = piece.getPosn();
		
		for(Posn to : edgeNeighbors) {
			if(getPieceAt(to) == null) {
				possibleMoves.add(new Move(piece, to));
			} else {
				if((getElevationAt(to)<= getElevationAt(currentPosn)) 
						&& (getPieceAt(to).compareTo(piece) <= 0)) {
					possibleMoves.add(new Move(piece, to, getPieceAt(to)));
				}
			}
		}
		
		for(Posn to : cornerNeighbors) {
			if((getElevationAt(to) == getElevationAt(currentPosn)) && (getPieceAt(to) != null)
					&& (getPieceAt(to).compareTo(piece) <= 0)) {
				possibleMoves.add(new Move(piece, to, getPieceAt(to)));
			}
		}
		return possibleMoves;			
	}
	
	@Override
	public TriangleBoard copyBoard() {
		TriangleBoard copy = new TriangleBoard(_dimensions);
		for (int i = 0; i < _dimensions; i++) {
			for (int j = 0; j < _dimensions*2; j++) {
				Piece p = _board[i][j];
				if (p != null) {
					copy.setPieceAt(p.getPosn(), p.copy());
				}
			}
		}
		return copy;
	}
	
	@Override
	public int getElevation(Posn p) {
		return _elevationsMap.get(_dimensions)[p.getX()][p.getY()];
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
