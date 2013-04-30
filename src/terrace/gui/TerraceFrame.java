package terrace.gui;

import java.awt.CardLayout;
import java.awt.Dimension;

import javax.swing.JFrame;

import terrace.Game;
import terrace.Variant;
import terrace.exception.IllegalMoveException;

public class TerraceFrame extends JFrame {
	private static final String SETUP = "Setup";
	private static final String GAME = "Game";
	
	CardLayout layout = new CardLayout();
	
	public TerraceFrame(int numHuman, int numAI, int boardSize, Variant variant) throws IllegalMoveException {
		setPreferredSize(new Dimension(1200, 1200));
		
		setExtendedState(getExtendedState() | MAXIMIZED_BOTH);
		setLayout(layout);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		add(new GamePanel(new Game(numHuman, numAI, boardSize, variant)), GAME);
	}
}
