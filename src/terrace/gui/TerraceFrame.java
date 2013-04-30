package terrace.gui;

import java.awt.CardLayout;
import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.JPanel;

import terrace.Game;
import terrace.Variant;
import terrace.exception.IllegalMoveException;

public class TerraceFrame extends JFrame {
	private static final String START_SCREEN = "Setup";
	private static final String GAME = "Game";
	private static final String LOCAL_SETUP = "local game setup";
	private static final String NETWORK_SETUP = "networked game setup";
	private static final String JOIN_NETWORK = "join networked game";
	private JPanel cards;
	
	public TerraceFrame(int numHuman, int numAI, int boardSize, Variant variant) throws IllegalMoveException {
		setPreferredSize(new Dimension(1200, 1200));
		
		cards = new JPanel(new CardLayout());
		
		setExtendedState(getExtendedState() | MAXIMIZED_BOTH);
		getContentPane().setLayout(new CardLayout());
		setLayout(new CardLayout());
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		cards.add(new StartScreen(this), START_SCREEN);
		cards.add(new LocalGameSetup(this), LOCAL_SETUP);
		cards.add(new GamePanel(new Game(numHuman, numAI, boardSize, variant)), GAME);
		add(cards);
	}
	
	public void changeCard(String cardName) {
		System.out.println(cardName);
		CardLayout layout = (CardLayout) cards.getLayout();
		layout.show(cards, cardName);
	}
}
