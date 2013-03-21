package terrace;

import java.util.List;
import java.util.Set;

import com.google.common.base.Optional;

public interface Board<T extends Board<T>> {
	int getWidth();
	int getHeight();
	
	// gets where a piece could move in the absence of any other pieces on the board 
	Set<Posn> getNeighbors(Posn posn);
	
	// gets where a piece can move
	Set<Posn> getMoves(Piece piece);
	
	Optional<Piece> getPieceAt(Posn posn);
	
	T movePiece(Piece piece, Posn posn);
	
	
}
