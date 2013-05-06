package terrace.gui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.FlowLayout;
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
import terrace.gui.controls.AbstractMouseListener;

public class GameSetupScreen extends TerracePanel implements MouseListener {
	private static final long serialVersionUID = 1L;

	private final static int MAX_NAME_LENGTH = 15;

	private static final Color backgroundColor = Color.DARK_GRAY;
	private static final Color headerColor = Color.WHITE;
	private static final Color defaultColor = Color.WHITE;
	private static final Color fadedColor = Color.LIGHT_GRAY;

	private boolean _isLarge = true;

	private TerraceFrame _frame;
	private NetworkType _networkType;

	private JPanel numPlayersPanel;
	private JLabel numPlayersLabel;
	private JPanel numPlayersOptions;

	private JLabel _p1Label;
	private JLabel _p2Label;
	private JLabel _p3Label;
	private JLabel _p4Label;

	private JTextField _p1Field;
	private JTextField _p2Field;
	private JTextField _p3Field;
	private JTextField _p4Field;

	private JPanel boardOptions;
	private JPanel typeLabelPanel;
	private JLabel boardType;
	private TerraceButton triangle;
	private TerraceButton _standard;
	private TerraceButton downhill;
	private TerraceButton aggressive;

	private TerraceButton _onePlayer;
	private TerraceButton _twoPlayer;
	private TerraceButton _threePlayer;
	private TerraceButton _fourPlayer;

	private JPanel boardSize;
	private JPanel sizeLabelPanel;
	private JLabel sizeLabel;
	private TerraceButton _large;
	private TerraceButton _small;

	private JButton goButton;
	private JButton backButton;

	private JPanel portPanel;
	private JLabel portLabel;
	private JTextField portField;

	private JPanel namesLabelPanel;
	private JLabel nameHeader;
	private JPanel playerNames;



	public GameSetupScreen(TerraceFrame frame, NetworkType networkType) {
		super(frame);

		_frame = frame;
		_frame._builder.setNumLocalPlayers(1);
		_frame._builder.setVariant(Variant.STANDARD);
		_networkType = networkType;
		setBackground(backgroundColor);

		numPlayersPanel = new JPanel();
		numPlayersLabel = new JLabel("Number of local players");
		numPlayersOptions = new JPanel();

		_onePlayer = new TerraceButton("1");
		_twoPlayer = new TerraceButton("2");
		_threePlayer = new TerraceButton("3");
		_fourPlayer = new TerraceButton("4");

		goButton = new JButton();
		backButton = new JButton("Back");

		portPanel = new JPanel();
		portLabel = new JLabel("Port ");
		portField = new JTextField(10);

		boardSize = new JPanel();
		sizeLabelPanel = new JPanel();
		sizeLabel = new JLabel("Board Size");
		_small = new TerraceButton("small");
		_large = new TerraceButton("large");

		boardOptions = new JPanel();
		typeLabelPanel = new JPanel();
		boardType = new JLabel("Game Type");
		_standard = new TerraceButton("square - standard rules");
		downhill = new TerraceButton("square - downhill rules");
		aggressive = new TerraceButton("square - aggresive rules");
		triangle = new TerraceButton("triangle");

		namesLabelPanel = new JPanel();
		nameHeader = new JLabel("Names");
		playerNames = new JPanel();

		_p1Label = new JLabel("Player 1");
		_p2Label = new JLabel("Player 2");
		_p3Label = new JLabel("Player 3");
		_p4Label = new JLabel("Player 4");

		_p1Field = new JTextField(10);
		_p2Field = new JTextField(10);
		_p3Field = new JTextField(10);
		_p4Field = new JTextField(10);

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

		//board size options
		boardSize.setBackground(backgroundColor);
		boardSize.setBorder(BorderFactory.createLineBorder(Color.WHITE));
		GridBagConstraints boardSizeConst = makeGBC(0, 3);
		boardSizeConst.anchor = GridBagConstraints.NORTH;
		boardSizeConst.insets = new Insets(0,0,0, 30);
		boardSize.setLayout(new BoxLayout(boardSize, BoxLayout.PAGE_AXIS));

		headerSetting(sizeLabelPanel);
		sizeLabelPanel.setVisible(false);
		headerSetting(sizeLabel);
		sizeLabelPanel.add(sizeLabel);
		GridBagConstraints sizeLabelConst = makeGBC(0, 2);
		sizeLabelConst.insets = new Insets(20, 0, 20, 0);

		_small.addMouseListener(new BoardTypeListener(false));
		defaultSetting(_small);
		_small.setAlignmentX(CENTER_ALIGNMENT);

		_large.addMouseListener(new BoardTypeListener(true));
		defaultSetting(_large);
		_large.setAlignmentX(CENTER_ALIGNMENT);
		_large.setSelected(true);

		TerraceButtonGroup sizeGroup = new TerraceButtonGroup();
		sizeGroup.add(_small);
		sizeGroup.add(_large);

		boardSize.add(_small);
		boardSize.add(_large);

		//board type Options
		boardOptions.setBackground(backgroundColor);
		boardOptions.setBorder(BorderFactory.createLineBorder(Color.WHITE));
		GridBagConstraints boardOptionsConst = makeGBC(1, 3);
		boardOptionsConst.insets = new Insets(0,10,0, 30);
		boardOptions.setLayout(new BoxLayout(boardOptions, BoxLayout.PAGE_AXIS));

		typeLabelPanel.setBackground(backgroundColor);
		typeLabelPanel.setVisible(false);
		headerSetting(boardType);
		typeLabelPanel.add(boardType);
		GridBagConstraints boardLabelConst = makeGBC(1, 2);
		boardLabelConst.insets = new Insets(20, 0, 20, 0);

		triangle.addMouseListener(new VariantTypeListener(Variant.TRIANGLE));
		defaultSetting(triangle);
		triangle.setAlignmentX(Component.CENTER_ALIGNMENT);

		_standard.addMouseListener(new VariantTypeListener(Variant.STANDARD));
		defaultSetting(_standard);
		_standard.setAlignmentX(Component.CENTER_ALIGNMENT);
		_standard.setSelected(true);

		downhill.addMouseListener(new VariantTypeListener(Variant.DOWNHILL));
		defaultSetting(downhill);
		downhill.setAlignmentX(Component.CENTER_ALIGNMENT);

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
		namesLabelPanel.setBackground(backgroundColor);
		namesLabelPanel.add(nameHeader);
		GridBagConstraints headerConst = makeGBC(2, 2);
		headerConst.insets = new Insets(20, 0, 20, 0);
		headerSetting(nameHeader);

		//player names panel
		playerNames.setLayout(new GridBagLayout());
		playerNames.setBorder(BorderFactory.createLineBorder(Color.WHITE));
		playerNames.setBackground(backgroundColor);
		GridBagConstraints playerNamesConst = makeGBC(2, 3);
		playerNamesConst.ipady = 10;
		playerNamesConst.ipadx = 10;
		playerNamesConst.anchor = GridBagConstraints.NORTH;

		//lables for player textfields
		GridBagConstraints p1LabelConst = makeGBC(0, 1);
		defaultSetting(_p1Label);

		GridBagConstraints p2LabelConst = makeGBC(0, 2);
		defaultSetting(_p2Label);
		_p2Label.setVisible(false);

		GridBagConstraints p3LabelConst = makeGBC(0, 3);
		defaultSetting(_p3Label);
		_p3Label.setVisible(false);

		GridBagConstraints p4LabelConst = makeGBC(0, 4);
		defaultSetting(_p4Label);
		_p4Label.setVisible(false);

		//text fields for player names
		_p1Field.setDocument(new LengthLimit(MAX_NAME_LENGTH));
		_p1Field.addFocusListener(new InputFocusListener("Player 1"));
		GridBagConstraints p1FieldConst = makeGBC(1, 1);
		p1FieldConst.insets = new Insets(0, 4, 0, 0);
		textFieldSetting(_p1Field);
		_p1Field.setText("Player 1");

		_p2Field.addFocusListener(new InputFocusListener("Player 2"));//TODO when on CPU focusListener renames to player 2
		_p2Field.setDocument(new LengthLimit(MAX_NAME_LENGTH));
		GridBagConstraints p2FieldConst = makeGBC(1, 2);
		p2FieldConst.insets = new Insets(0, 4, 0, 0);
		_p2Field.setText("Player 2");
		_p2Field.setEnabled(false);
		_p2Field.setVisible(false);
		textFieldSetting(_p2Field);

		_p3Field.addFocusListener(new InputFocusListener("Player 3"));
		_p3Field.setDocument(new LengthLimit(MAX_NAME_LENGTH));
		GridBagConstraints p3FieldConst = makeGBC(1, 3);
		p3FieldConst.insets = new Insets(0, 4, 0, 0);
		textFieldSetting(_p3Field);
		_p3Field.setText("Player 3");
		_p3Field.setEnabled(false);
		_p3Field.setVisible(false);
		textFieldSetting(_p3Field);

		_p4Field.addFocusListener(new InputFocusListener("Player 4"));
		_p4Field.setDocument(new LengthLimit(MAX_NAME_LENGTH));
		GridBagConstraints p4FieldConst = makeGBC(1, 4);
		p4FieldConst.insets = new Insets(0, 4, 0, 0);
		_p4Field.setText("Player 4");
		textFieldSetting(_p4Field);
		_p4Field.setVisible(false);
		_p4Field.setEnabled(false);

		playerNames.add(_p1Label, p1LabelConst);
		playerNames.add(_p2Label, p2LabelConst);
		playerNames.add(_p3Label, p3LabelConst);
		playerNames.add(_p4Label, p4LabelConst);
		playerNames.add(_p1Field, p1FieldConst);
		playerNames.add(_p2Field, p2FieldConst);
		playerNames.add(_p3Field, p3FieldConst);
		playerNames.add(_p4Field, p4FieldConst);

		//Number of players panel
		numPlayersPanel.setBorder(BorderFactory.createLineBorder(Color.WHITE));
		numPlayersPanel.setBackground(backgroundColor);
		numPlayersLabel.setForeground(headerColor);
		numPlayersLabel.setFont(defaultFont);
		numPlayersPanel.add(numPlayersLabel);		

		GridBagConstraints numPlayersConst = makeGBC(0, 0);
		numPlayersConst.gridwidth = 4;
		numPlayersConst.insets = new Insets(0, 0, 0, 20);

		numPlayersOptions.setLayout(new GridBagLayout());

		defaultSetting(_onePlayer);
		_onePlayer.setSelected(true);
		_onePlayer.addMouseListener(new NumPlayerListener(1));
		GridBagConstraints oneConst = makeGBC(0, 0);

		defaultSetting(_twoPlayer);
		_twoPlayer.addMouseListener(new NumPlayerListener(2));
		GridBagConstraints twoConst = makeGBC(1, 0);

		defaultSetting(_threePlayer);
		_threePlayer.addMouseListener(new NumPlayerListener(3));
		GridBagConstraints threeConst = makeGBC(0, 1);

		defaultSetting(_fourPlayer);
		_fourPlayer.addMouseListener(new NumPlayerListener(4));
		GridBagConstraints fourConst = makeGBC(1, 1);

		TerraceButtonGroup numPlayersButtons = new TerraceButtonGroup();
		numPlayersButtons.add(_onePlayer);
		numPlayersButtons.add(_twoPlayer);
		numPlayersButtons.add(_threePlayer);
		numPlayersButtons.add(_fourPlayer);

		numPlayersPanel.add(_onePlayer, oneConst);
		numPlayersPanel.add(_twoPlayer, twoConst);
		numPlayersPanel.add(_threePlayer, threeConst);
		numPlayersPanel.add(_fourPlayer, fourConst);
		
		GridBagConstraints goConst = makeGBC(3, 4);
		goButton.addActionListener(new GoListener());
		goConst.insets = new Insets(30, 0, 0, 0);
		goConst.anchor = GridBagConstraints.EAST;

		backButton.addActionListener(new BackListener());
		GridBagConstraints backConst = makeGBC(0, 4);
		backConst.insets = new Insets(30, 0, 0,0);
		backConst.anchor = GridBagConstraints.WEST;

		portPanel.setLayout(new FlowLayout());
		portPanel.setBackground(backgroundColor);
		GridBagConstraints portPanelConst = makeGBC(0, 1);
		portPanelConst.gridwidth = 3;
		portPanelConst.anchor = GridBagConstraints.CENTER; 
		portPanelConst.insets = new Insets(15, 0, 0, 0);

		headerSetting(portLabel);
		portLabel.setVisible(false);

		portField.addFocusListener(new InputFocusListener(Integer.toString(GameBuilder.DEFAULT_PORT)));
		portField.setDocument(new PortInputVerifier());
		portField.setText(Integer.toString(GameBuilder.DEFAULT_PORT));
		portField.setVisible(false);
		textFieldSetting(portField);

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
			_p2Field.setEnabled(true);
			_p2Field.setVisible(true);
			_p2Label.setVisible(true);
			_p2Label.setText("CPU");
			_p2Field.setText("CPU");			

			goButton.setText("Start Game");
		} else if(_networkType == NetworkType.HOST) {
			_fourPlayer.setEnabled(false);
			_fourPlayer.setVisible(false);
			_p4Field.setEnabled(false);
			_p4Field.setVisible(false);
			_p4Label.setVisible(false);
			_p4Label.setEnabled(false);
			portField.setVisible(true);
			portLabel.setVisible(true);
			goButton.setText("Create Game");
		} else {
			_fourPlayer.setEnabled(false);
			_fourPlayer.setVisible(false);
			_p4Field.setEnabled(false);
			_p4Field.setVisible(false);
			_p4Label.setVisible(false);
			_p4Label.setEnabled(false);
			goButton.setText("Join Game");
		}

		if (_networkType != NetworkType.JOIN) {
			sizeLabelPanel.setVisible(true);
			typeLabelPanel.setVisible(true);
			pane.add(boardOptions, boardOptionsConst);
			pane.add(boardSize, boardSizeConst);
		}
	}

	public void resetScreen() {
		_standard.setSelected(true);
		_p1Field.setText("Player 1");
		_onePlayer.setSelected(true);
		_isLarge = true;
		_large.setSelected(true);
		_frame._builder.setNumLocalPlayers(1);
		_frame._builder.setVariant(Variant.STANDARD);
		_p3Field.setEnabled(false);
		_p3Field.setVisible(false);
		_p3Label.setVisible(false);
		_p3Field.setVisible(false);

		switch (_networkType) {
		case LOCAL:
			_p2Label.setText("CPU");
			_p2Field.setText("CPU");
			_p4Field.setEnabled(false);
			_p4Label.setVisible(false);
			_p4Field.setVisible(false);
			_threePlayer.setEnabled(true);
			_fourPlayer.setEnabled(true);
			break;
		default:
			_threePlayer.setEnabled(true);
			_p2Field.setVisible(false);
			_p2Field.setText("Player 2");
			_p2Field.setEnabled(false);
			_p2Label.setVisible(false);
			_p3Field.setText("Player 3");
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
			_frame.changeCard(TerraceFrame.START_SCREEN);
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
			if (_networkType == NetworkType.JOIN) {
				_frame.changeCard(TerraceFrame.JOIN_SETUP);
			} else {
				int numPlayers = _frame._builder.getNumLocalPlayers();
				List<String> playerNames = new ArrayList<>();
				if (_networkType == NetworkType.LOCAL) {
					if (numPlayers > 2) {
						playerNames.add(_p4Field.getText());
						playerNames.add(_p3Field.getText());
						playerNames.add(_p2Field.getText());
						playerNames.add(_p1Field.getText());
					} else {
						playerNames.add(_p2Field.getText());
						playerNames.add(_p1Field.getText());
					}
				} else {
					switch (numPlayers) {
					case 3:
						playerNames.add(_p3Field.getText());
					case 2:
						playerNames.add(_p2Field.getText());
					case 1:
						playerNames.add(_p1Field.getText());
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

	private class VariantTypeListener extends AbstractMouseListener {
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
					_p2Label.setText("CPU");
					_p2Field.setText("CPU");
				}
				_p3Field.setVisible(false);
				_p3Label.setVisible(false);
				_p4Label.setVisible(false);
				_threePlayer.setEnabled(false);
				if (_networkType == NetworkType.LOCAL) {
					_fourPlayer.setEnabled(false);
					_p4Field.setVisible(false);
				}
			} else {
				_threePlayer.setEnabled(true);
				if (_networkType == NetworkType.LOCAL) {
					_fourPlayer.setEnabled(true);
				}
			}			
		}
	}

	class BoardTypeListener extends AbstractMouseListener {
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
	}

	private class NumPlayerListener extends AbstractMouseListener {
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
			_frame._builder.setNumLocalPlayers(_num);
			if (oldNum < _num) {
				switch (oldNum) {
				case 1: 
					_p2Field.setText("Player 2");
				case 2:
					_p3Field.setText("Player 3");
				case 4:
					if (_networkType == NetworkType.LOCAL) _p4Field.setText("Player 4");
				}
			}
			switch (_networkType) {
			case LOCAL:
				switch (_num) {
				case 1:
					if (oldNum != 1) {
						_p2Field.setText("CPU");
					}
					_p2Label.setText("CPU");
					_p3Field.setEnabled(false);
					_p4Field.setEnabled(false);
					_p3Label.setVisible(false);
					_p4Label.setVisible(false);
					_p3Field.setVisible(false);
					_p4Field.setVisible(false);
					break;
				case 2:
					_p2Label.setText("Player 2");
					_p3Field.setEnabled(false);
					_p4Field.setEnabled(false);
					_p3Field.setVisible(false);
					_p4Field.setVisible(false);
					_p3Label.setVisible(false);
					_p4Label.setVisible(false);
					break;
				case 3:
					_p3Field.setEnabled(true);
					_p2Label.setText("Player 2");
					_p4Label.setText("CPU");
					_p4Field.setText("CPU");
					_p4Field.setEnabled(true);
					_p3Field.setVisible(true);
					_p4Field.setVisible(true);
					_p3Label.setVisible(true);
					_p4Label.setVisible(true);
					break;
				case 4:
					_p3Field.setEnabled(true);
					_p2Label.setText("Player 2");
					_p4Label.setText("Player 4");
					if (oldNum != 4) {
						_p4Field.setText("Player 4");
					}
					_p4Field.setEnabled(true);
					_p4Field.setEnabled(true);
					_p3Field.setVisible(true);
					_p4Field.setVisible(true);
					_p3Label.setVisible(true);
					_p4Label.setVisible(true);
					break;
				}
				break;
			default:
				switch (_frame._builder.getNumLocalPlayers()) {
				case 1:
					_p2Field.setEnabled(false);
					_p2Field.setVisible(false);
					_p3Field.setEnabled(false);
					_p2Label.setVisible(false);
					_p3Label.setVisible(false);
					_p3Field.setVisible(false);
					break;
				case 2:
					_p2Field.setEnabled(true);
					_p2Field.setVisible(true);
					_p3Field.setEnabled(false);
					_p2Label.setVisible(true);
					_p3Label.setVisible(false);
					_p3Field.setVisible(false);
					break;
				case 3:
					_p2Field.setEnabled(true);
					_p2Field.setVisible(true);
					_p3Field.setEnabled(true);
					_p2Label.setVisible(true);
					_p3Label.setVisible(true);
					_p3Field.setVisible(true);
					break;
				}
			}
		}
	}
	
	@Override
	public void mouseClicked(MouseEvent e) {
		this.requestFocus();
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
