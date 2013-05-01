package terrace.gui;

import java.awt.CardLayout;
import java.awt.Dimension;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

import terrace.DefaultBoardGame;
import terrace.Variant;
import terrace.exception.IllegalMoveException;

public class TerraceFrame extends JFrame {
	private static final String START_SCREEN = "Setup";
	private static final String GAME = "Game";
	private static final String LOCAL_SETUP = "local game setup";
	private static final String NETWORK_SETUP = "networked game setup";
	private static final String JOIN_NETWORK = "join networked game";
	private int numPlayers;
	private JPanel cards;
	private Map<Integer, String> playerNames;
	private Variant ruleType;
	
	public TerraceFrame(int numHuman, int numAI, int boardSize, Variant variant) throws IllegalMoveException {
		playerNames = new HashMap<>();
		
		setPreferredSize(new Dimension(1200, 1200));
		setMinimumSize(new Dimension(600, 600));
		cards = new JPanel(new CardLayout());
		
		setExtendedState(getExtendedState() | MAXIMIZED_BOTH);
		getContentPane().setLayout(new CardLayout());
		setLayout(new CardLayout());
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		cards.add(new StartScreen(this), START_SCREEN);
		cards.add(new LocalGameSetup(this), LOCAL_SETUP);
		cards.add(new GamePanel(new DefaultBoardGame(numHuman, numAI, boardSize, variant)), GAME);
		add(cards);
	}
	
	public void changeCard(String cardName) {
		System.out.println(cardName);
		System.out.println(ruleType);
		System.out.println(numPlayers);
		CardLayout layout = (CardLayout) cards.getLayout();
		layout.show(cards, cardName);
	}
	void setPlayerNames(List<String> names) {
		int i = 0;
		for(String n : names) {
			playerNames.put(i, n);
			i++;
		}
	}
	void setNumPlayers(int n) {
		numPlayers = n;
	}
	void setVariant(Variant v) {
		ruleType = v;
	}
	
}
