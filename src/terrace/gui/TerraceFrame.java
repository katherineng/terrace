package terrace.gui;

import java.awt.CardLayout;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

import terrace.Game;
import terrace.Variant;
import terrace.exception.IllegalMoveException;

public class TerraceFrame extends JFrame {
	private static final String SETUP = "Setup";
	private static final String GAME = "Game";
	
	CardLayout layout = new CardLayout();
	
	public TerraceFrame(int numHuman, int numAI, int boardSize, Variant variant) throws IllegalMoveException {
		setSize(1200, 1200);

		setExtendedState(getExtendedState() | MAXIMIZED_BOTH);
		setLayout(layout);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		add(new GamePanel(new Game(numHuman, numAI, boardSize, variant)), GAME);
	}
	
	private void createCards() {
		JPanel mainMenu = new JPanel();
		mainMenu.setLayout(new FlowLayout());
		
		mainMenu.add(new JButton("New Game"));
		
		mainMenu.add(new JButton("Create new game"));
		mainMenu.add(new JButton("Join existing game"));
		
		JPanel players = new JPanel();
		players.add(new JButton("1 Player"));
		players.add(new JButton("2 Player"));
		players.add(new JButton("3 Player"));
		players.add(new JButton("4 Player"));
		
		JPanel newGame = new JPanel();
		
		JPanel networkCreate = new JPanel();
		
		JPanel networkJoin = new JPanel();
		
		JPanel local = new JPanel();
	}
}
