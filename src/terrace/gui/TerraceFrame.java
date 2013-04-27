package terrace.gui;

import java.awt.CardLayout;

import javax.swing.JFrame;

import terrace.Game;
import terrace.Variant;
import terrace.exception.IllegalMoveException;

public class TerraceFrame extends JFrame {
	private static final String SETUP = "Setup";
	private static final String GAME = "Game";
	
	CardLayout layout = new CardLayout();
	
	public TerraceFrame(int numHuman, int numAI, int boardSize, Variant variant) throws IllegalMoveException {
		setSize(1200, 1200);
		setLayout(layout);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		add(new GamePanel(new Game(numHuman, numAI, boardSize, variant)), GAME);
	}
}
