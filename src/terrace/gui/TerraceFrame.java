package terrace.gui;

import java.awt.CardLayout;

import javax.swing.JFrame;

import terrace.Game;
import terrace.Variant;

public class TerraceFrame extends JFrame {
	private static final String SETUP = "Setup";
	private static final String GAME = "Game";
	
	CardLayout layout = new CardLayout();
	
	public TerraceFrame() {
		setExtendedState(getExtendedState() | MAXIMIZED_BOTH);
		setLayout(layout);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		add(new GamePanel(new Game(2, 8, Variant.STANDARD)), GAME);
	}
}
