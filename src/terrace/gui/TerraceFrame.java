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
		setSize(1200, 1200);
		setLayout(layout);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		add(new GamePanel(new Game(4, 8, Variant.STANDARD)), GAME);
	}
}
