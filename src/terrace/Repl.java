package terrace;

import java.io.*;

import terrace.exception.IllegalMoveException;

public class Repl {

	// num players, board dims, rule variant, board shape
	public static void main(String[] args) throws IllegalMoveException {
		if (args.length != 4) {
			System.err.println("Usage: ./terrace <num players> <num AI> <board dimensions> <rule variant> <board shape>");
			System.exit(0);
		} else {
			int numHuman = Integer.parseInt(args[0]);
			int numAI = Integer.parseInt(args[1]);
			int dimensions = Integer.parseInt(args[2]);
			
			String variant = args[3];
			Variant var = null;
			
			if (variant.equals("standard")) {
				var = Variant.STANDARD;
			} else if (variant.equals("aggressive")) {
				var = Variant.AGGRESSIVE;
			} else if (variant.equals("downhill")) {
				var = Variant.DOWNHILL;
			} else {
				System.err.println("ERROR: Rule variant must be either standard, aggressive, or downhill");
				System.exit(0);
			}
			
			DefaultBoardGame game = new DefaultBoardGame(numHuman, numAI, dimensions, var);
			System.out.println(game.getBoard().piecesToString());
			System.out.println(game.getCurrentPlayer().getColor() + "\'s turn");
			
			try (BufferedReader br = new BufferedReader(new InputStreamReader(System.in))) {
				String line = br.readLine();
				
				while (line != null && !game.isGameOver()) {
					String[] move = line.trim().split("\\s+");
					
					try {
						int fromX = Integer.parseInt(move[0]);
						int fromY = Integer.parseInt(move[1]);
						int toX = Integer.parseInt(move[2]);
						int toY = Integer.parseInt(move[3]);
						
						game.movePiece(new Posn(fromX, fromY), new Posn(toX, toY));
						System.out.println(game.getBoard().piecesToString());
						System.out.println(game.getCurrentPlayer().getColor() + "\'s turn");
						
					} catch (NumberFormatException e) {
						System.err.println("ERROR: Position coordinates must be integers");
					} catch (IllegalMoveException e) {
						System.err.println("ERROR: " + e.getMessage());
					}
						
					line = br.readLine();
				}
				
				System.out.println("GAME OVER! " + game.getWinner().getName() + " won!");
			} catch (IOException e) {
				System.err.println(e.getMessage());
				System.exit(0);
			}
			
		}
	}
}
