package terrace.gui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;

import terrace.GameBuilder;
import terrace.NetworkType;
import terrace.Variant;
import terrace.gui.controls.TerraceButton;
import terrace.gui.controls.TerraceButtonGroup;

public class GameSetupScreen extends TerracePanel implements MouseListener {
	private static final long serialVersionUID = 1L;
	
	private static final Pattern validName = Pattern.compile("[^,]+");
	private final static int MAX_NAME_LENGTH = 15;
	
	private static final Font headerFont = new Font("Verdana", Font.BOLD, 30);
	private static final Font defaultFont = new Font("Verdana", Font.BOLD, 24);
	
	private static final Color backgroundColor = Color.DARK_GRAY;
	private static final Color headerColor = Color.WHITE;
	private static final Color defaultColor = new Color(255, 255, 255);
	private static final Color fadedColor = Color.LIGHT_GRAY;
	
	private TerraceFrame _frame;
	private NetworkType _networkType;
	private JLabel _p1;
	private JLabel _p2;
	private JLabel _p3;
	private JLabel _p4;
	private JTextField _player1;
	private JTextField _player2;
	private JTextField _player3;
	private JTextField _player4;
	private TerraceButton _standard;
	private TerraceButton _onePlayer;
	private TerraceButton _twoPlayer;
	private TerraceButton _threePlayer;
	private TerraceButton _fourPlayer;
	private TerraceButton _large;
	private JTextField portField;
	
	private boolean _isLarge = true;
	
	public GameSetupScreen(TerraceFrame frame, NetworkType networkType) {
		super(frame);
		_frame = frame;
		_frame._builder.setNumLocalPlayers(1);
		_frame._builder.setVariant(Variant.STANDARD);
		_networkType = networkType;
		setBackground(backgroundColor);
		JPanel infoPanel = new JPanel(new GridBagLayout());
		JPanel outerPanel = new JPanel();
		outerPanel.setBackground(backgroundColor);
		outerPanel.setBorder(BorderFactory.createLineBorder(defaultColor));
		infoPanel.setBackground(backgroundColor);
		addComponents(infoPanel);
		outerPanel.add(infoPanel);
		add(outerPanel);
		this.addMouseListener(this);
	}
	
	private void addComponents(Container pane) {
		setLayout(new GridBagLayout());
		//board size panel
		JPanel boardSize = new JPanel();
		boardSize.setBackground(backgroundColor);
		boardSize.setBorder(BorderFactory.createLineBorder(Color.WHITE));
		GridBagConstraints boardSizeConst = makeGBC(0, 3);
		boardSizeConst.anchor = GridBagConstraints.NORTH;
		boardSizeConst.insets = new Insets(0,0,0, 30);
		boardSize.setLayout(new BoxLayout(boardSize, BoxLayout.PAGE_AXIS));
		
		JPanel sizeLabelPanel = new JPanel();
		//sizeLabelPanel.setBorder(BorderFactory.createLineBorder(Color.WHITE));
		headerSetting(sizeLabelPanel);
		JLabel sizeLabel = new JLabel("Board Size");
		sizeLabelPanel.setVisible(false);
		headerSetting(sizeLabel);
		sizeLabelPanel.add(sizeLabel);
		GridBagConstraints sizeLabelConst = makeGBC(0, 2);
		sizeLabelConst.insets = new Insets(20, 0, 20, 0);
		
		TerraceButton small = new TerraceButton("small");
		small.addMouseListener(new BoardTypeListener(false));
		defaultSetting(small);
		small.setAlignmentX(CENTER_ALIGNMENT);
		
		_large = new TerraceButton("large");
		_large.addMouseListener(new BoardTypeListener(true));
		defaultSetting(_large);
		_large.setAlignmentX(CENTER_ALIGNMENT);
		_large.setSelected(true);
		
		TerraceButtonGroup sizeGroup = new TerraceButtonGroup();
		sizeGroup.add(small);
		sizeGroup.add(_large);
		
		boardSize.add(small);
		boardSize.add(_large);
		
		//board type panel
		JPanel boardOptions = new JPanel();
		boardOptions.setBackground(backgroundColor);
		boardOptions.setBorder(BorderFactory.createLineBorder(Color.WHITE));
		GridBagConstraints boardOptionsConst = makeGBC(1, 3);
		boardOptionsConst.insets = new Insets(0,10,0, 30);
		boardOptions.setLayout(new BoxLayout(boardOptions, BoxLayout.PAGE_AXIS));
		
		JPanel typeLabelPanel = new JPanel();
		typeLabelPanel.setBackground(backgroundColor);
		typeLabelPanel.setVisible(false);
		//typeLabelPanel.setBorder(BorderFactory.createLineBorder(Color.WHITE));
		JLabel boardType = new JLabel("Board Type");
		headerSetting(boardType);
		typeLabelPanel.add(boardType);
		GridBagConstraints boardLabelConst = makeGBC(1, 2);
		boardLabelConst.insets = new Insets(20, 0, 20, 0);
		
		TerraceButton triangle = new TerraceButton("triangle");
		triangle.addMouseListener(new VariantTypeListener(Variant.TRIANGLE));
		triangle.setFont(defaultFont);
		triangle.setAlignmentX(Component.CENTER_ALIGNMENT);
		triangle.setForeground(defaultColor);
		triangle.setBackground(backgroundColor);
		
		_standard = new TerraceButton("square - standard rules");
		_standard.addMouseListener(new VariantTypeListener(Variant.STANDARD));
		defaultSetting(_standard);
		_standard.setAlignmentX(Component.CENTER_ALIGNMENT);
		_standard.setSelected(true);
		
		TerraceButton downhill = new TerraceButton("square - downhill rules");
		downhill.addMouseListener(new VariantTypeListener(Variant.DOWNHILL));
		defaultSetting(downhill);
		downhill.setAlignmentX(Component.CENTER_ALIGNMENT);
		
		TerraceButton aggressive = new TerraceButton("square - aggresive rules");
		aggressive.addMouseListener(new VariantTypeListener(Variant.AGGRESSIVE));
		defaultSetting(aggressive);
		aggressive.setAlignmentX(Component.CENTER_ALIGNMENT);
		
		TerraceButtonGroup options = new TerraceButtonGroup();
		options.add(triangle);
		options.add(_standard);
		options.add(downhill);
		options.add(aggressive);
		boardOptions.add(_standard);
		boardOptions.add(downhill);
		boardOptions.add(aggressive);
		boardOptions.add(triangle);
		
		//player names header
		JPanel namesLabelPanel = new JPanel();
		namesLabelPanel.setBackground(backgroundColor);
		JLabel nameHeader = new JLabel("Names");
		namesLabelPanel.add(nameHeader);
		GridBagConstraints headerConst = makeGBC(2, 2);
		headerConst.insets = new Insets(20, 0, 20, 0);
		headerSetting(nameHeader);
		
		//player names textfields
		JPanel playerNames = new JPanel();
		playerNames.setLayout(new GridBagLayout());
		playerNames.setBorder(BorderFactory.createLineBorder(Color.WHITE));
		playerNames.setBackground(backgroundColor);
		GridBagConstraints playerNamesConst = makeGBC(2, 3);
		playerNamesConst.ipady = 10;
		playerNamesConst.ipadx = 10;
		playerNamesConst.anchor = GridBagConstraints.NORTH;
		
		//lables for player textfields
		_p1 = new JLabel("Player 1");
		GridBagConstraints p1Const = makeGBC(0, 1);
		defaultSetting(_p1);
		
		_p2 = new JLabel("Player 2");
		GridBagConstraints p2Const = makeGBC(0, 2);
		defaultSetting(_p2);
		_p2.setVisible(false);
		
		_p3 = new JLabel("Player 3");
		GridBagConstraints p3Const = makeGBC(0, 3);
		defaultSetting(_p3);
		_p3.setVisible(false);
		
		_p4 = new JLabel("Player 4");
		GridBagConstraints p4Const = makeGBC(0, 4);
		defaultSetting(_p4);
		_p4.setVisible(false);
		
		//text fields for player names
		_player1 = new JTextField(10);
		_player1.setCaretColor(fadedColor);
		_player1.setDocument(new LengthLimit(MAX_NAME_LENGTH));
		_player1.addFocusListener(new InputFocusListener("Player 1"));
		GridBagConstraints player1Const = makeGBC(1, 1);
		player1Const.insets = new Insets(0, 4, 0, 0);
		_player1.setBackground(backgroundColor);
		_player1.setForeground(fadedColor);
		_player1.setFont(defaultFont);
		_player1.setText("Player 1");
		
		_player2 = new JTextField(10);
		_player2.addFocusListener(new InputFocusListener("Player 2"));
		_player2.setCaretColor(fadedColor);
		_player2.setDocument(new LengthLimit(MAX_NAME_LENGTH));
		GridBagConstraints player2Const = makeGBC(1, 2);
		player2Const.insets = new Insets(0, 4, 0, 0);
		_player2.setBackground(backgroundColor);
		_player2.setForeground(fadedColor);
		_player2.setFont(defaultFont);
		_player2.setText("Player 2");
		_player2.setEnabled(false);
		_player2.setVisible(false);
		
		_player3 = new JTextField(10);
		_player3.addFocusListener(new InputFocusListener("Player 3"));
		_player3.setCaretColor(fadedColor);
		_player3.setDocument(new LengthLimit(MAX_NAME_LENGTH));
		GridBagConstraints player3Const = makeGBC(1, 3);
		player3Const.insets = new Insets(0, 4, 0, 0);
		_player3.setBackground(backgroundColor);
		_player3.setForeground(fadedColor);sizeLabelPanel.setVisible(false);
		_player3.setFont(defaultFont);
		_player3.setText("Player 3");
		_player3.setEnabled(false);
		_player3.setVisible(false);
		
		playerNames.add(_p1, p1Const);
		playerNames.add(_p2, p2Const);
		playerNames.add(_p3, p3Const);
		playerNames.add(_p4, p4Const);
		playerNames.add(_player1, player1Const);
		playerNames.add(_player2, player2Const);
		playerNames.add(_player3, player3Const);
		
		//Number of players panel
		JPanel numPlayersPanel = new JPanel();
		numPlayersPanel.setBorder(BorderFactory.createLineBorder(Color.WHITE));
		numPlayersPanel.setBackground(backgroundColor);
		JLabel numPlayersLabel = new JLabel("Number of local players");
		numPlayersLabel.setForeground(headerColor);
		numPlayersLabel.setFont(defaultFont);
		numPlayersPanel.add(numPlayersLabel);		
		
		GridBagConstraints numPlayersConst = makeGBC(0, 0);
		numPlayersConst.gridwidth = 4;
		numPlayersConst.insets = new Insets(0, 0, 0, 20);
		
		JPanel numPlayersOptions = new JPanel();
		numPlayersOptions.setLayout(new GridBagLayout());
		
		_onePlayer = new TerraceButton("1");
		defaultSetting(_onePlayer);
		_onePlayer.setSelected(true);
		_onePlayer.addMouseListener(new NumPlayerListener(1));
		GridBagConstraints oneConst = makeGBC(0, 0);
		
		_twoPlayer = new TerraceButton("2");
		defaultSetting(_twoPlayer);
		_twoPlayer.addMouseListener(new NumPlayerListener(2));
		GridBagConstraints twoConst = makeGBC(1, 0);
		
		_threePlayer = new TerraceButton("3");
		defaultSetting(_threePlayer);
		_threePlayer.addMouseListener(new NumPlayerListener(3));
		GridBagConstraints threeConst = makeGBC(0, 1);
		
		TerraceButtonGroup numPlayersButtons = new TerraceButtonGroup();
		numPlayersButtons.add(_onePlayer);
		numPlayersButtons.add(_twoPlayer);
		numPlayersButtons.add(_threePlayer);
		
		numPlayersPanel.add(_onePlayer, oneConst);
		numPlayersPanel.add(_twoPlayer, twoConst);
		numPlayersPanel.add(_threePlayer, threeConst);
		
		JButton goButton = new JButton();
		GridBagConstraints goConst = makeGBC(3, 4);
		goButton.addActionListener(new GoListener());
		goConst.insets = new Insets(30, 0, 0, 0);
		goConst.anchor = GridBagConstraints.EAST;
		
		JButton backButton = new JButton("Back");
		backButton.addActionListener(new BackListener());
		GridBagConstraints backConst = makeGBC(0, 4);
		backConst.insets = new Insets(30, 0, 0,0);
		backConst.anchor = GridBagConstraints.WEST;
		
		JLabel portLabel = new JLabel("Port ");
		portLabel.setForeground(headerColor);
		portLabel.setFont(defaultFont);
		portLabel.setVisible(false);
		
		portField = new JTextField(10);
		portField.addFocusListener(new InputFocusListener(Integer.toString(_frame._builder.DEFAULT_PORT)));
		portField.setDocument(new PortInputVerifier());
		portField.setText(Integer.toString(GameBuilder.DEFAULT_PORT));
		portField.setCaretColor(fadedColor);
		portField.setFont(defaultFont);
		portField.setBackground(backgroundColor);
		portField.setForeground(fadedColor);
		portField.setVisible(false);
		
		JPanel portPanel = new JPanel();
		portPanel.setLayout(new FlowLayout());
		portPanel.setBackground(backgroundColor);
		GridBagConstraints portPanelConst = makeGBC(0, 1);
		portPanelConst.gridx = 0;
		portPanelConst.gridy = 1;
		portPanelConst.gridwidth = 3;
		portPanelConst.anchor = GridBagConstraints.CENTER; 
		portPanelConst.insets = new Insets(15, 0, 0, 0);
		
		portPanel.add(portLabel);
		portPanel.add(portField);
		
		pane.add(sizeLabelPanel, sizeLabelConst);
		pane.add(typeLabelPanel, boardLabelConst);
		pane.add(namesLabelPanel, headerConst);
		pane.add(playerNames, playerNamesConst);
		pane.add(goButton, goConst);
		pane.add(numPlayersPanel, numPlayersConst);
		pane.add(backButton, backConst);
		pane.add(portPanel, portPanelConst);
		
		if(_networkType == NetworkType.LOCAL) {
			_player2.setEnabled(true);
			_player2.setVisible(true);
			_p2.setVisible(true);
			_p2.setText("CPU");
			_player2.setText("CPU");
			
			_player4 = new JTextField(10);
			_player4.addFocusListener(new InputFocusListener("Player 4"));
			_player4.setCaretColor(fadedColor);
			_player4.setDocument(new LengthLimit(MAX_NAME_LENGTH));
			GridBagConstraints player4Const = makeGBC(1, 4);
			player4Const.insets = new Insets(0, 4, 0, 0);
			_player4.setBackground(backgroundColor);
			_player4.setForeground(fadedColor);
			_player4.setFont(defaultFont);
			_player4.setText("Player 4");
			_player4.setEnabled(false);
			_player4.setVisible(false);
			playerNames.add(_player4, player4Const);
			
			_fourPlayer = new TerraceButton("4");
			_fourPlayer.setBackground(backgroundColor);
			_fourPlayer.setForeground(defaultColor);
			_fourPlayer.setFont(defaultFont);
			_fourPlayer.addMouseListener(new NumPlayerListener(4));
			GridBagConstraints fourConst = makeGBC(1, 1);
			numPlayersButtons.add(_fourPlayer);
			numPlayersPanel.add(_fourPlayer, fourConst);
			
			goButton.setText("Start Game");
		} else if(_networkType == NetworkType.HOST) {
			portField.setVisible(true);
			portLabel.setVisible(true);
			goButton.setText("Create Game");
		} else {
			goButton.setText("Join Game");
		}
		
		if (_networkType != NetworkType.JOIN) {
			sizeLabelPanel.setVisible(true);
			typeLabelPanel.setVisible(true);
			pane.add(boardOptions, boardOptionsConst);
			pane.add(boardSize, boardSizeConst);
		}
	}
	private void headerSetting(Component comp) {
		comp.setBackground(backgroundColor);
		comp.setFont(headerFont);
		comp.setForeground(headerColor);
	}
	private void defaultSetting(Component comp) {
		comp.setBackground(backgroundColor);
		comp.setFont(defaultFont);
		comp.setForeground(defaultColor);
	}
	private GridBagConstraints makeGBC(int x, int y) {
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridx = x;
		gbc.gridy = y;
		return gbc;
	}
	
	public void resetScreen() {
		switch (_networkType) {
		case LOCAL:
			_standard.setSelected(true);
			_player1.setText("Player 1");
			_onePlayer.setSelected(true);
			_standard.setSelected(true);
			_large.setSelected(true);
			_p2.setText("CPU");
			_player2.setText("CPU");
			_player3.setEnabled(false);
			_player4.setEnabled(false);
			_p3.setVisible(false);
			_p4.setVisible(false);
			_player3.setVisible(false);
			_player4.setVisible(false);
			_threePlayer.setEnabled(true);
			_fourPlayer.setEnabled(true);
			_frame._builder.setVariant(Variant.STANDARD);
			_frame._builder.setNumLocalPlayers(1);
			_isLarge = true;
			break;
		default:
			_player1.setText("Player 1");
			_standard.setSelected(true);
			_frame._builder.setVariant(Variant.STANDARD);
			_frame._builder.setNumLocalPlayers(1);
			_isLarge = true;
			_onePlayer.setSelected(true);
			_threePlayer.setEnabled(true);
			_large.setSelected(true);
			_player2.setVisible(false);
			_player2.setText("Player 2");
			_player2.setEnabled(false);
			_p2.setVisible(false);
			_player3.setEnabled(false);
			_player3.setText("Player 3");
			_p3.setVisible(false);
			_player3.setVisible(false);
			break;
		}
	}
	private class InputFocusListener implements FocusListener {
		String _defaultStr;
		InputFocusListener(String defaultStr) {
			_defaultStr = defaultStr;
		}
		@Override
		public void focusGained(FocusEvent e) {}

		@Override
		public void focusLost(FocusEvent e) {
			JTextField source = (JTextField) e.getSource();
			if (source.getText() == null || source.getText().equals("")) {
				source.setText(_defaultStr);
			}
			int end = source.getSelectionEnd();
			source.setCaretPosition(end);
		}
		
	}
	private class BackListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			_frame.changeCard("Setup");
		}
	}

	private class PortInputVerifier extends PlainDocument {
		private static final long serialVersionUID = 1159717759453623084L;
		
		PortInputVerifier() {
			super();
		}
		
		@Override
		public void insertString(int offset, String str, AttributeSet attr) throws BadLocationException {
			if (str == null) return;
			System.out.println(str);
			 char[] chars = str.toCharArray();
			 for (char c : chars) {
				 if (c >= 48 && c <= 57) {
						super.insertString(offset, Character.toString(c) , attr);
					}
			 }
		}
	}
	
	private class LengthLimit extends PlainDocument {
		private static final long serialVersionUID = 1159717759453623084L;
		
		private int limit;
		
		LengthLimit(int limit) {
			super();
			this.limit = limit;
		}
		
		@Override
		public void insertString(int offset, String str, AttributeSet attr) throws BadLocationException {
			if (str == null) return;
			
			if ((getLength() + str.length()) <= limit) {
				super.insertString(offset, str, attr);
			}
		}
	}
	
	private class GoListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {		
			int n;
			if (_networkType == NetworkType.JOIN) {
				_frame.changeCard("join networked game");
			} else {
				int numPlayers = _frame._builder.getNumLocalPlayers();
				List<String> playerNames = new ArrayList<>();
				if (_networkType == NetworkType.LOCAL) {
					if (numPlayers > 2) {
						playerNames.add(_player4.getText());
						playerNames.add(_player3.getText());
						playerNames.add(_player2.getText());
						playerNames.add(_player1.getText());
					} else {
						playerNames.add(_player2.getText());
						playerNames.add(_player1.getText());
					}
				} else {
					switch (numPlayers) {
					case 3:
						playerNames.add(_player3.getText());
					case 2:
						playerNames.add(_player2.getText());
					case 1:
						playerNames.add(_player1.getText());
					}
				}
				Collections.reverse(playerNames);
				_frame.setPlayerNames(playerNames);
				Variant v = _frame._builder.getVariant();
				if (v == Variant.TRIANGLE) {
					if (!_isLarge) {
						_frame._builder.setSize(4);
					} else {
						_frame._builder.setSize(5);
					}
				} else {
					if (!_isLarge) {
						_frame._builder.setSize(6);
					} else {
						_frame._builder.setSize(8);
					}
				}
				
				if (_networkType == NetworkType.HOST) {
					_frame.setPort(Integer.parseInt(portField.getText()));
					_frame.changeCard("host networked game");
				} else {
					_frame.changeCard("Game");
				}
			}
		}

	}
	
	private class VariantTypeListener implements MouseListener {
		Variant _var;
		
		VariantTypeListener(Variant var) {
			_var = var;
		}
		
		@Override
		public void mouseClicked(MouseEvent e) {
			_frame._builder.setVariant(_var);
			if (!((TerraceButton)e.getSource()).isEnabled()) {
				return;
			} else if (_var == Variant.TRIANGLE) {
				if (_frame._builder.getNumLocalPlayers() > 2) {
					_frame._builder.setNumLocalPlayers(1);
					_onePlayer.setSelected(true);
					_p2.setText("CPU");
					_player2.setText("CPU");
				}
				_player3.setVisible(false);
				_p3.setVisible(false);
				_p4.setVisible(false);
				_threePlayer.setEnabled(false);
				if (_networkType == NetworkType.LOCAL) {
					_fourPlayer.setEnabled(false);
					_player4.setVisible(false);
				}
			} else {
				_threePlayer.setEnabled(true);
				if (_networkType == NetworkType.LOCAL) {
					_fourPlayer.setEnabled(true);
				}
			}			
		}
		
		@Override
		public void mousePressed(MouseEvent e) {}
		
		@Override
		public void mouseReleased(MouseEvent e) {}
		
		@Override
		public void mouseEntered(MouseEvent e) {}
		
		@Override
		public void mouseExited(MouseEvent e) {}
	}
	
	class BoardTypeListener implements MouseListener {
		private boolean _large;
		
		BoardTypeListener(boolean large) {
			_large = large;
		}
		
		@Override
		public void mouseClicked(MouseEvent e) {
			if (!((TerraceButton)e.getSource()).isEnabled()) {
				return;
			} 
			_isLarge = _large;
		}
		
		@Override
		public void mousePressed(MouseEvent e) {}
		
		@Override
		public void mouseReleased(MouseEvent e) {}
		
		@Override
		public void mouseEntered(MouseEvent e) {}
		
		@Override
		public void mouseExited(MouseEvent e) {}
	}
	
	private class NumPlayerListener implements MouseListener {
		private int _num;
		
		NumPlayerListener(int num) {
			_num = num;
		}
		
		@Override
		public void mouseClicked(MouseEvent e) {
			if (!((TerraceButton)e.getSource()).isEnabled()) {
				return;
			}
			int oldNum = _frame._builder.getNumLocalPlayers();
			System.out.println(oldNum);
			_frame._builder.setNumLocalPlayers(_num);
			if (oldNum < _num) {
				switch (oldNum) {
				case 1: 
					_player2.setText("Player 2");
				case 2:
					_player3.setText("Player 3");
				case 4:
					if (_networkType == NetworkType.LOCAL) _player4.setText("Player 4");
				}
			}
			switch (_networkType) {
			case LOCAL:
				switch (_num) {
				case 1:
					if (oldNum != 1) {
						_player2.setText("CPU");
					}
					_p2.setText("CPU");
					_player3.setEnabled(false);
					_player4.setEnabled(false);
					_p3.setVisible(false);
					_p4.setVisible(false);
					_player3.setVisible(false);
					_player4.setVisible(false);
					break;
				case 2:
					_p2.setText("Player 2");
					_player3.setEnabled(false);
					_player4.setEnabled(false);
					_player3.setVisible(false);
					_player4.setVisible(false);
					_p3.setVisible(false);
					_p4.setVisible(false);
					break;
				case 3:
					_player3.setEnabled(true);
					_p2.setText("Player 2");
					_p4.setText("CPU");
					_player4.setText("CPU");
					_player4.setEnabled(true);
					_player3.setVisible(true);
					_player4.setVisible(true);
					_p3.setVisible(true);
					_p4.setVisible(true);
					break;
				case 4:
					_player3.setEnabled(true);
					_p2.setText("Player 2");
					_p4.setText("Player 4");
					if (oldNum != 4) {
						_player4.setText("Player 4");
					}
					_player4.setEnabled(true);
					_player4.setEnabled(true);
					_player3.setVisible(true);
					_player4.setVisible(true);
					_p3.setVisible(true);
					_p4.setVisible(true);
					break;
				}
				break;
			default:
				switch (_frame._builder.getNumLocalPlayers()) {
				case 1:
					_player2.setEnabled(false);
					_player2.setVisible(false);
					_player3.setEnabled(false);
					_p2.setVisible(false);
					_p3.setVisible(false);
					_player3.setVisible(false);
					break;
				case 2:
					_player2.setEnabled(true);
					_player2.setVisible(true);
					_player3.setEnabled(false);
					_p2.setVisible(true);
					_p3.setVisible(false);
					_player3.setVisible(false);
					break;
				case 3:
					_player2.setEnabled(true);
					_player2.setVisible(true);
					_player3.setEnabled(true);
					_p2.setVisible(true);
					_p3.setVisible(true);
					_player3.setVisible(true);
					break;
				}
			}
		}
		
		@Override
		public void mousePressed(MouseEvent e) {}
		
		@Override
		public void mouseReleased(MouseEvent e) {}
		
		@Override
		public void mouseEntered(MouseEvent e) {}
		
		@Override
		public void mouseExited(MouseEvent e) {}
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		this.requestFocus();
	}

	@Override
	public void mousePressed(MouseEvent e) {
		this.requestFocus();
	}

	@Override
	public void mouseReleased(MouseEvent e) {}

	@Override
	public void mouseEntered(MouseEvent e) {}

	@Override
	public void mouseExited(MouseEvent e) {}
}
