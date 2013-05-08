package terrace.gui;

import java.awt.CardLayout;
import java.awt.Dimension;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import terrace.GameBuilder;
import terrace.GameServer;
import terrace.NetworkType;
import terrace.network.ClientConnection;
import terrace.util.Callback;

public class TerraceFrame extends JFrame {
	private static final long serialVersionUID = 641801362474775997L;
	
	static final String START_SCREEN = "Setup";
	static final String GAME = "Game";
	static final String LOCAL_SETUP = "local game setup";
	static final String NETWORK_SETUP = "networked game setup";
	static final String JOIN_SETUP  = "join game setup";
	static final String HOST_GAME = "host networked game";
	static final String JOIN_NETWORK = "join networked game";
	static final String HELP_SCREEN = "help screen";
	
	GameBuilder _builder = new GameBuilder();
	private JPanel _cards;
	private List<String> _playerNames;
	private HostNetworkScreen _networkScreen;
	private GameScreen _currentGameScreen;
	private GameSetupScreen _localSetup;
	private GameSetupScreen _hostSetup;
	private JoinNetworkScreen _joinSetup;
	private int _port;
	private String _hostname;
	private List<ClientConnection> _clients = new LinkedList<>();
	
	public TerraceFrame() {
		setPreferredSize(new Dimension(1200, 1200));
		setMinimumSize(new Dimension(600, 600));
		_cards = new JPanel(new CardLayout());
		
		_networkScreen = new HostNetworkScreen(this);
		_localSetup = new GameSetupScreen(this, NetworkType.LOCAL);
		_hostSetup = new GameSetupScreen(this, NetworkType.HOST);
		_joinSetup = new JoinNetworkScreen(this);
		
		setExtendedState(getExtendedState() | MAXIMIZED_BOTH);
		getContentPane().setLayout(new CardLayout());
		setLayout(new CardLayout());
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		_cards.add(new StartScreen(this), START_SCREEN);
		_cards.add(_localSetup, LOCAL_SETUP);
		_cards.add(_hostSetup, NETWORK_SETUP);
		_cards.add(_joinSetup, JOIN_SETUP);
		_cards.add(new HelpScreen(this), HELP_SCREEN);
		_cards.add(_networkScreen, HOST_GAME);
		add(_cards);
	}
	
	public void changeCard(String cardName) {
		if (cardName.equals(START_SCREEN)) {
			if (_currentGameScreen != null) {
				_cards.remove(_currentGameScreen);
				_currentGameScreen = null;
			}
			_localSetup.resetScreen();
			_joinSetup.resetScreen();
			_hostSetup.resetScreen();
		} else if (cardName.equals(GAME)) {
			if (_currentGameScreen != null) _cards.remove(_currentGameScreen);
			
			_builder.setPlayerNames(_playerNames);
			_currentGameScreen = new GameScreen(_builder, this, _clients);
			_cards.add(_currentGameScreen, GAME);
		} else if (cardName.equals(HOST_GAME)) {
			_networkScreen.setPlayerNames(_playerNames);
			_builder.hostGame(
					_port,
					_networkScreen.getNewRequestCallback(),
					_networkScreen.getConnectionDroppedCallback()
			);
		} else if (cardName.equals(JOIN_NETWORK)) {
			_builder.joinGame(
					_hostname,
					_port,
					new Callback<GameServer>() {
						@Override
						public void call(GameServer val) {
							if (_currentGameScreen != null) _cards.remove(_currentGameScreen);
							_currentGameScreen = new GameScreen(val, TerraceFrame.this);
							CardLayout layout = (CardLayout) _cards.getLayout();
							layout.show(_cards, GAME);
						}
					},
					new Runnable() {
						@Override
						public void run() {
							_joinSetup.notifyUnableToConnect();
						}
					},
					new Runnable() {
						@Override
						public void run() {
							_joinSetup.notifyConnectionLost();
						}
					},
					new Runnable() {
						@Override
						public void run() {
							if (JOptionPane.showConfirmDialog(
									TerraceFrame.this,
									"Connection has been lost.",
									"Connection lost",
									JOptionPane.YES_OPTION) == JOptionPane.YES_OPTION) {
								TerraceFrame.this.changeCard(START_SCREEN);
							}
						}
					}
			);
			
		}
		if (!cardName.equals(JOIN_NETWORK)) {
			CardLayout layout = (CardLayout) _cards.getLayout();
			layout.show(_cards, cardName);
		}
	}
	
	public void startGame(List<ClientConnection> clients) {
		_clients = clients;
		changeCard(GAME);
	}
	
	void setPlayerNames(List<String> names) {
		_playerNames = names;
	}
	
	void addPlayerNames(List<String> names) {
		_playerNames.addAll(names);
	}
	
	GameBuilder getBuilder() {
		return _builder;
	}

	public void setPort(int port) {
		_port = port; 
	}
	public void setHostName(String hostname) {
		_hostname = hostname;
	}
}
