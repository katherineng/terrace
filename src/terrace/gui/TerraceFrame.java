package terrace.gui;

import java.awt.CardLayout;
import java.awt.Dimension;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JFrame;
import javax.swing.JPanel;

import terrace.GameBuilder;
import terrace.NetworkType;
import terrace.exception.IllegalMoveException;

public class TerraceFrame extends JFrame {
	private static final long serialVersionUID = 641801362474775997L;
	
	static final String START_SCREEN = "Setup";
	private static final String GAME = "Game";
	private static final String LOCAL_SETUP = "local game setup";
	private static final String NETWORK_SETUP = "networked game setup";
	private static final String JOIN_SETUP  = "join game setup";
	private static final String HOST_GAME = "host networked game";
	private static final String JOIN_NETWORK = "join networked game";
	private static final String HELP_SCREEN = "help screen";
	
	final GameBuilder _builder = new GameBuilder();
	private JPanel _cards;
	private List<String> _playerNames;
	private GamePanel _currentGame;
	private HostNetworkScreen _networkScreen;
	private GameScreen _currentGameScreen;
	
	public TerraceFrame() {
		setPreferredSize(new Dimension(1200, 1200));
		setMinimumSize(new Dimension(600, 600));
		_cards = new JPanel(new CardLayout());
		
		_networkScreen = new HostNetworkScreen(this);
		
		setExtendedState(getExtendedState() | MAXIMIZED_BOTH);
		getContentPane().setLayout(new CardLayout());
		setLayout(new CardLayout());
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		_cards.add(new StartScreen(this), START_SCREEN);
		_cards.add(new LocalGameSetup(this, NetworkType.LOCAL), LOCAL_SETUP);
		_cards.add(new LocalGameSetup(this, NetworkType.HOST), NETWORK_SETUP);
		_cards.add(new LocalGameSetup(this, NetworkType.JOIN), JOIN_SETUP);
		_cards.add(new HelpScreen(this), HELP_SCREEN);
		_cards.add(_networkScreen, HOST_GAME);
		_cards.add(new JoinNetworkScreen(this), JOIN_NETWORK);
		add(_cards);
	}
	
	public void changeCard(String cardName) {
		System.out.println(cardName);
		if (cardName.equals(START_SCREEN)) {
			if (_currentGameScreen != null) {
				_cards.remove(_currentGameScreen);
				_currentGameScreen = null;
				_currentGame = null;
			}
			
		} else if (cardName.equals(GAME)) {
			if (_currentGameScreen != null) _cards.remove(_currentGameScreen);
			
			_builder.setPlayerNames(_playerNames);
			
			_currentGameScreen = new GameScreen(_builder, this);
			_currentGame = _currentGameScreen.getGame();
			
			_cards.add(_currentGameScreen, GAME);
			
		} else if (cardName == HOST_GAME) {
			_networkScreen.setPlayerNames(_playerNames);
		}
		
		CardLayout layout = (CardLayout) _cards.getLayout();
		layout.show(_cards, cardName);
	}
	
	void setPlayerNames(List<String> names) {
		_playerNames = names;
		
	}
	
	GameBuilder getBuilder() {
		return _builder;
	}
}
