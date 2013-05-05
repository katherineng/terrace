package terrace.gui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;

import terrace.*;
import terrace.gui.controls.TerraceButton;
import terrace.gui.controls.TerraceButtonGroup;

public class LocalGameSetup extends TerracePanel {
	private static final long serialVersionUID = 1L;
	private TerraceFrame _frame;
	private NetworkType _networkType;
	private static final Font headerFont = new Font("Verdana", Font.BOLD, 30);
	private static final Font defaultFont = new Font("Verdana", Font.BOLD, 24);
	private Integer numPlayers = 1;
	private Variant v = Variant.STANDARD;
	private static final Color backgroundColor = Color.DARK_GRAY;
	private static final Color headerColor = Color.WHITE;
	private static final Color defaultColor = new Color(255, 255, 255);
	private static final Color fadedColor = Color.LIGHT_GRAY;
	private JLabel p1;
	private JLabel p2;
	private JLabel p3;
	private JLabel p4;
	private JTextField player1;
	private JTextField player2;
	private JTextField player3;
	private JTextField player4;
	private TerraceButton standard;
	private TerraceButton onePlayer;
	private TerraceButton twoPlayer;
	private TerraceButton threePlayer;
	private TerraceButton fourPlayer;

	private TerraceButton large;
	
	private final static int MAX_NAME_LENGTH = 15;
	private int boardSize = 1;// 0 if small 1 if large
	private final String regexp = "[^,]+";
	private JLabel error;

	public LocalGameSetup(TerraceFrame frame, NetworkType networkType) {
		super(frame);
		_frame = frame;
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
	}

	private void addComponents(Container pane) {
		setLayout(new GridBagLayout());
		//board size panel
		JPanel boardSize = new JPanel();
		boardSize.setBackground(backgroundColor);
		boardSize.setBorder(BorderFactory.createLineBorder(Color.WHITE));
		GridBagConstraints boardSizeConst = new GridBagConstraints();
		boardSizeConst.gridx = 0;
		boardSizeConst.gridy = 3;
		boardSizeConst.anchor = GridBagConstraints.NORTH;
		boardSizeConst.insets = new Insets(0,0,0, 30);
		boardSize.setLayout(new BoxLayout(boardSize, BoxLayout.PAGE_AXIS));
		
		JPanel sizeLabelPanel = new JPanel();
		//sizeLabelPanel.setBorder(BorderFactory.createLineBorder(Color.WHITE));
		sizeLabelPanel.setBackground(backgroundColor);
		JLabel sizeLabel = new JLabel("Board Size");
		sizeLabelPanel.setVisible(false);
		sizeLabel.setForeground(headerColor);
		sizeLabel.setFont(headerFont);
		sizeLabelPanel.add(sizeLabel);
		GridBagConstraints sizeLabelConst = new GridBagConstraints();
		sizeLabelConst.gridx = 0;
		sizeLabelConst.gridy = 2;
		sizeLabelConst.insets = new Insets(20, 0, 20, 0);

		TerraceButton small = new TerraceButton("small");
		small.addMouseListener(new BoardTypeListener(0));
		small.setFont(defaultFont);
		small.setAlignmentX(CENTER_ALIGNMENT);
		small.setForeground(defaultColor);
		small.setBackground(backgroundColor);

		large = new TerraceButton("large");
		large.addMouseListener(new BoardTypeListener(1));
		large.setFont(defaultFont);
		large.setAlignmentX(CENTER_ALIGNMENT);
		large.setForeground(defaultColor);
		large.setBackground(backgroundColor);
		large.setSelected(true);

		TerraceButtonGroup sizeGroup = new TerraceButtonGroup();
		sizeGroup.add(small);
		sizeGroup.add(large);

		boardSize.add(small);
		boardSize.add(large);

		//board type panel
		JPanel boardOptions = new JPanel();
		boardOptions.setBackground(backgroundColor);
		boardOptions.setBorder(BorderFactory.createLineBorder(Color.WHITE));
		GridBagConstraints boardOptionsConst = new GridBagConstraints();
		boardOptionsConst.gridx = 1;
		boardOptionsConst.gridy = 3;
		boardOptionsConst.insets = new Insets(0,10,0, 30);
		boardOptions.setLayout(new BoxLayout(boardOptions, BoxLayout.PAGE_AXIS));
		
		JPanel typeLabelPanel = new JPanel();
		typeLabelPanel.setBackground(backgroundColor);
		typeLabelPanel.setVisible(false);
		//typeLabelPanel.setBorder(BorderFactory.createLineBorder(Color.WHITE));
		JLabel boardType = new JLabel("Board Type");
		boardType.setForeground(headerColor);
		boardType.setFont(headerFont);
		typeLabelPanel.add(boardType);
		GridBagConstraints boardLabelConst = new GridBagConstraints();
		boardLabelConst.gridx = 1;
		boardLabelConst.gridy = 2;
		boardLabelConst.insets = new Insets(20, 0, 20, 0);

		TerraceButton triangle = new TerraceButton("triangle");
		triangle.addMouseListener(new VariantTypeListener(Variant.TRIANGLE));
		triangle.setFont(defaultFont);
		triangle.setAlignmentX(Component.CENTER_ALIGNMENT);
		triangle.setForeground(defaultColor);
		triangle.setBackground(backgroundColor);
		
		standard = new TerraceButton("square - standard rules");
		standard.addMouseListener(new VariantTypeListener(Variant.STANDARD));
		standard.setFont(defaultFont);
		standard.setBackground(backgroundColor);
		standard.setForeground(defaultColor);
		standard.setAlignmentX(Component.CENTER_ALIGNMENT);
		standard.setSelected(true);

		TerraceButton downhill = new TerraceButton("square - downhill rules");
		downhill.addMouseListener(new VariantTypeListener(Variant.DOWNHILL));
		downhill.setFont(defaultFont);
		downhill.setBackground(backgroundColor);
		downhill.setForeground(defaultColor);
		downhill.setAlignmentX(Component.CENTER_ALIGNMENT);

		TerraceButton aggressive = new TerraceButton("square - aggresive rules");
		aggressive.addMouseListener(new VariantTypeListener(Variant.AGGRESSIVE));
		aggressive.setFont(defaultFont);
		aggressive.setBackground(backgroundColor);
		aggressive.setForeground(defaultColor);
		aggressive.setAlignmentX(Component.CENTER_ALIGNMENT);
		
		TerraceButtonGroup options = new TerraceButtonGroup();
		options.add(triangle);
		options.add(standard);
		options.add(downhill);
		options.add(aggressive);
		boardOptions.add(standard);sizeLabelPanel.setVisible(false);
		boardOptions.add(downhill);
		boardOptions.add(aggressive);
		boardOptions.add(triangle);

		//player name panel
		JPanel playerNames = new JPanel();
		playerNames.setBorder(BorderFactory.createLineBorder(Color.WHITE));
		playerNames.setBackground(backgroundColor);
		GridBagConstraints playerNamesConst = new GridBagConstraints();
		playerNamesConst.gridx = 2;
		playerNamesConst.gridy = 3;
		playerNamesConst.ipady = 10;
		playerNamesConst.ipadx = 10;
		playerNamesConst.anchor = GridBagConstraints.NORTH;
		playerNames.setLayout(new GridBagLayout());

		JPanel namesLabelPanel = new JPanel();
		//namesLabelPanel.setBorder(BorderFactory.createLineBorder(Color.WHITE));
		namesLabelPanel.setBackground(backgroundColor);
		JLabel nameHeader = new JLabel("Names");
		namesLabelPanel.add(nameHeader);
		GridBagConstraints headerConst = new GridBagConstraints();
		headerConst.gridx = 2;
		headerConst.gridy = 2;
		headerConst.insets = new Insets(20, 0, 20, 0);
		nameHeader.setFont(headerFont);
		nameHeader.setForeground(headerColor);
		
		//lables for player textfields
		p1 = new JLabel("Player 1");
		GridBagConstraints p1Const = new GridBagConstraints();
		p1Const.gridx = 0;
		p1Const.gridy = 1;
		p1.setForeground(defaultColor);
		p1.setFont(defaultFont);
		
		p2 = new JLabel("Player 2");
		GridBagConstraints p2Const = new GridBagConstraints();
		p2Const.gridx = 0;
		p2Const.gridy = 2;
		p2.setFont(defaultFont);
		p2.setForeground(defaultColor);
		p2.setVisible(false);
		
		p3 = new JLabel("Player 3");
		GridBagConstraints p3Const = new GridBagConstraints();
		p3Const.gridx = 0;
		p3Const.gridy = 3;
		p3.setFont(defaultFont);
		p3.setForeground(defaultColor);
		p3.setVisible(false);
		
		p4 = new JLabel("Player 4");
		GridBagConstraints p4Const = new GridBagConstraints();
		p4Const.gridx = 0;
		p4Const.gridy = 4;
		p4.setFont(defaultFont);
		p4.setForeground(defaultColor);
		p4.setVisible(false);
		
		//text fields for player names
		player1 = new JTextField(10);
		player1.setCaretColor(fadedColor);
		player1.setDocument(new LengthLimit(MAX_NAME_LENGTH));
		GridBagConstraints player1Const = new GridBagConstraints();
		player1Const.gridx = 1;
		player1Const.gridy = 1;
		player1Const.insets = new Insets(0, 4, 0, 0);
		player1.setBackground(backgroundColor);
		player1.setForeground(fadedColor);
		player1.setFont(defaultFont);
		player1.setText("Player 1");

		player2 = new JTextField(10);
		player2.setCaretColor(fadedColor);
		player2.setDocument(new LengthLimit(MAX_NAME_LENGTH));
		GridBagConstraints player2Const = new GridBagConstraints();
		player2Const.gridx = 1;
		player2Const.gridy = 2;
		player2Const.insets = new Insets(0, 4, 0, 0);
		player2.setBackground(backgroundColor);
		player2.setForeground(fadedColor);
		player2.setFont(defaultFont);
		player2.setText("Player 2");
		player2.setEnabled(false);
		player2.setVisible(false);
		
		player3 = new JTextField(10);
		player3.setCaretColor(fadedColor);
		player3.setDocument(new LengthLimit(MAX_NAME_LENGTH));
		GridBagConstraints player3Const = new GridBagConstraints();
		player3Const.gridx = 1;
		player3Const.gridy = 3;
		player3Const.insets = new Insets(0, 4, 0, 0);
		player3.setBackground(backgroundColor);
		player3.setForeground(fadedColor);sizeLabelPanel.setVisible(false);
		player3.setFont(defaultFont);
		player3.setText("Player 3");
		player3.setEnabled(false);
		player3.setVisible(false);
		
		playerNames.add(p1, p1Const);
		playerNames.add(p2, p2Const);
		playerNames.add(p3, p3Const);
		playerNames.add(p4, p4Const);
		playerNames.add(player1, player1Const);
		playerNames.add(player2, player2Const);
		playerNames.add(player3, player3Const);
		
		//Number of players panel
		JPanel numPlayersPanel = new JPanel();
		numPlayersPanel.setBorder(BorderFactory.createLineBorder(Color.WHITE));
		numPlayersPanel.setBackground(backgroundColor);
		JLabel numPlayersLabel = new JLabel("Number of local players");
		numPlayersLabel.setForeground(headerColor);
		numPlayersLabel.setFont(defaultFont);
		numPlayersPanel.add(numPlayersLabel);		
		
		GridBagConstraints numPlayersConst = new GridBagConstraints();
		numPlayersConst.gridx = 0;
		numPlayersConst.gridy = 0;
		numPlayersConst.gridwidth = 4;
		numPlayersConst.insets = new Insets(0, 0, 0, 20);

		JPanel numPlayersOptions = new JPanel();
		numPlayersOptions.setLayout(new GridBagLayout());

		onePlayer = new TerraceButton("1");
		onePlayer.setBackground(backgroundColor);
		onePlayer.setForeground(defaultColor);
		onePlayer.setFont(defaultFont);
		onePlayer.setSelected(true);
		onePlayer.addMouseListener(new NumPlayerListener(1));
		GridBagConstraints oneConst = new GridBagConstraints();
		oneConst.gridx = 0;
		oneConst.gridy = 0;

		twoPlayer = new TerraceButton("2");
		twoPlayer.setBackground(backgroundColor);
		twoPlayer.setForeground(defaultColor);
		twoPlayer.setFont(defaultFont);
		twoPlayer.addMouseListener(new NumPlayerListener(2));
		GridBagConstraints twoConst = new GridBagConstraints();
		twoConst.gridx = 1;
		twoConst.gridy = 0;
		
		threePlayer = new TerraceButton("3");
		threePlayer.setBackground(backgroundColor);
		threePlayer.setForeground(defaultColor);
		threePlayer.setFont(defaultFont);
		threePlayer.addMouseListener(new NumPlayerListener(3));
		GridBagConstraints threeConst = new GridBagConstraints();
		threeConst.gridx = 0;
		threeConst.gridy = 1;sizeLabelPanel.setVisible(false);
		
		TerraceButtonGroup numPlayersButtons = new TerraceButtonGroup();
		numPlayersButtons.add(onePlayer);
		numPlayersButtons.add(twoPlayer);
		numPlayersButtons.add(threePlayer);
		
		
		
		numPlayersPanel.add(onePlayer, oneConst);
		numPlayersPanel.add(twoPlayer, twoConst);
		numPlayersPanel.add(threePlayer, threeConst);
		
		JButton goButton = new JButton();
		GridBagConstraints goConst = new GridBagConstraints();
		goButton.addActionListener(new GoListener());
		goConst.gridx = 3;
		goConst.gridy = 4;
		goConst.insets = new Insets(30, 0, 0,0);
		goConst.anchor = GridBagConstraints.EAST;
		
		JButton backButton = new JButton("Back");
		backButton.addActionListener(new BackListener());
		GridBagConstraints backConst = new GridBagConstraints();
		backConst.gridx = 0;
		backConst.gridy = 4;
		backConst.insets = new Insets(30, 0, 0,0);
		backConst.anchor = GridBagConstraints.WEST;
		
		error = new JLabel("");
		error.setPreferredSize(new Dimension(250, 50));
		GridBagConstraints errorConst = new GridBagConstraints();
		error.setVisible(false);
		error.setForeground(defaultColor);
		errorConst.gridx = 0;
		errorConst.gridy = 6;
		errorConst.gridwidth = 2;
		JLabel portLabel = new JLabel("Port ");
		portLabel.setForeground(headerColor);
		portLabel.setFont(defaultFont);
		portLabel.setVisible(false);
		
		JTextField portField = new JTextField(10);
		portField.setCaretColor(fadedColor);
		portField.setFont(defaultFont);
		portField.setBackground(backgroundColor);
		portField.setForeground(fadedColor);
		portField.setVisible(false);
		
		JPanel portPanel = new JPanel();
		portPanel.setLayout(new FlowLayout());
		portPanel.setBackground(backgroundColor);
		GridBagConstraints portPanelConst = new GridBagConstraints();
		portPanelConst.gridx = 0;
		portPanelConst.gridy = 1;
		portPanelConst.gridwidth = 3;
		portPanelConst.anchor = GridBagConstraints.CENTER;sizeLabelPanel.setVisible(false);
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
		playerNames.add(error, errorConst);
		
		if(_networkType == NetworkType.LOCAL) {
			
			player2.setEnabled(true);
			player2.setVisible(true);
			p2.setVisible(true);
			p2.setText("CPU");
			player2.setText("CPU");
			
			player4 = new JTextField(10);
			player4.setCaretColor(fadedColor);
			player4.setDocument(new LengthLimit(MAX_NAME_LENGTH));
			GridBagConstraints player4Const = new GridBagConstraints();
			player4Const.gridx = 1;
			player4Const.gridy = 4;
			player4Const.insets = new Insets(0, 4, 0, 0);
			player4.setBackground(backgroundColor);
			player4.setForeground(fadedColor);
			player4.setFont(defaultFont);
			player4.setText("Player 4");
			player4.setEnabled(false);
			player4.setVisible(false);
			playerNames.add(player4, player4Const);
			
			fourPlayer = new TerraceButton("4");
			fourPlayer.setBackground(backgroundColor);
			fourPlayer.setForeground(defaultColor);
			fourPlayer.setFont(defaultFont);
			fourPlayer.addMouseListener(new NumPlayerListener(4));
			GridBagConstraints fourConst = new GridBagConstraints();
			fourConst.gridx = 1;
			fourConst.gridy = 1;
			numPlayersButtons.add(fourPlayer);
			numPlayersPanel.add(fourPlayer, fourConst);
			
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

	public void resetScreen() {
		switch (_networkType) {
		case LOCAL:
			standard.setSelected(true);// TODO if field is empty when focus is
										// lost, reset to "player _"
			player1.setText("Player 1");
			onePlayer.setSelected(true);
			standard.setSelected(true);
			large.setSelected(true);
			p2.setText("CPU");
			player2.setText("CPU");
			player3.setEnabled(false);
			player4.setEnabled(false);
			p3.setVisible(false);
			p4.setVisible(false);
			player3.setVisible(false);
			player4.setVisible(false);
			threePlayer.setEnabled(true);
			fourPlayer.setEnabled(true);
			v = Variant.STANDARD;
			numPlayers = 1;
			boardSize = 1;
			break;
		default:
			player1.setText("Player 1");
			standard.setSelected(true);
			v = Variant.STANDARD;
			numPlayers = 1;
			boardSize = 1;
			onePlayer.setSelected(true);
			threePlayer.setEnabled(true);
			large.setSelected(true);
			player2.setVisible(false);
			player2.setText("Player 2");
			player2.setEnabled(false);
			p2.setVisible(false);
			player3.setEnabled(false);
			player3.setText("Player 3");
			p3.setVisible(false);
			player3.setVisible(false);
			break;
		}

	}

	class BackListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			_frame.changeCard("Setup");
		}
	}

	class LengthListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			JTextField source = (JTextField) e.getSource();
			String word = source.getText();
			if (word.length() > MAX_NAME_LENGTH) {
				source.setText(word.substring(0, MAX_NAME_LENGTH));
			}
		}

	}
	class LengthLimit extends PlainDocument {
		private int limit;

		LengthLimit(int limit) {
			super();
			this.limit = limit;
		}

		public void insertString(int offset, String str, AttributeSet attr)
				throws BadLocationException {
			if (str == null)
				return;

			if ((getLength() + str.length()) <= limit) {
				super.insertString(offset, str, attr);
			}
		}
	}

	public int checkRegexp() {
		if (!player1.getText().matches(regexp)) {
			return 1;
		} else if (!player2.getText().matches(regexp)) {
			return 2;
		} else if (!player3.getText().matches(regexp)) {
			return 3;
		} else if (_networkType == NetworkType.LOCAL) {
			if (!player4.getText().matches(regexp)) {
				return 4;
			}
		}

		return 0;
	}

	public void setErrorMsg(String msg) {
		error.setText(msg);
		error.setVisible(true);
	}

	class GoListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			error.setVisible(false);

			int n;
			if ((n = checkRegexp()) != 0) {
				if (numPlayers - n == 1) {
					setErrorMsg("<html>CPU's name may not contain commas (,)</html>");
				} else {
					setErrorMsg("<html>Player " + n
							+ "'s name may not contain commas (,)</html>");
				}
				return;
			}

			if (_networkType == NetworkType.JOIN) {
				System.out.println("daslfkjdasfkljdak");// DEBUG
				_frame.changeCard("join networked game");
			} else {
				_frame._builder.setNumLocalPlayers(numPlayers);
				List<String> playerNames = new ArrayList<>();
				if (_networkType == NetworkType.LOCAL) {
					if (numPlayers > 2) {
						playerNames.add(player4.getText());
						playerNames.add(player3.getText());
						playerNames.add(player2.getText());
						playerNames.add(player1.getText());
					} else {
						playerNames.add(player2.getText());
						playerNames.add(player1.getText());
					}
				} else {
					switch (numPlayers) {
					case 3:
						playerNames.add(player3.getText());
					case 2:
						playerNames.add(player2.getText());
					case 1:
						playerNames.add(player1.getText());
					}
				}
				Collections.reverse(playerNames);
				_frame.setPlayerNames(playerNames);
				if (v == Variant.TRIANGLE) {
					if (boardSize == 0) {
						_frame._builder.setSize(4);
					} else {
						_frame._builder.setSize(5);
					}
				} else {
					if (boardSize == 0) {
						_frame._builder.setSize(6);
					} else {
						_frame._builder.setSize(8);
					}
				}
				_frame._builder.setVariant(v);

				if (_networkType == NetworkType.HOST) {
					_frame.changeCard("host networked game");
				} else {
					_frame.changeCard("Game");
				}
			}
		}

	}

	class VariantTypeListener implements MouseListener {
		
		Variant _var;
		
		VariantTypeListener(Variant var) {
			_var = var;
		}
		
		@Override
		public void mouseClicked(MouseEvent e) {
			v = _var;
			if (!((TerraceButton)e.getSource()).isEnabled()) {
				return;
			} else if (_var == Variant.TRIANGLE) {
				
				player3.setVisible(false);
				p3.setVisible(false);
				p4.setVisible(false);
				onePlayer.setSelected(true);
				threePlayer.setEnabled(false);
				if (_networkType == NetworkType.LOCAL) {
					fourPlayer.setEnabled(false);
					player4.setVisible(false);
				}
			} else {
				threePlayer.setEnabled(true);
				if (_networkType == NetworkType.LOCAL) {
					fourPlayer.setEnabled(true);
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
		
		private int _size;
		
		BoardTypeListener(int size) {
			_size = size;
		}

		@Override
		public void mouseClicked(MouseEvent e) {
			if (!((TerraceButton)e.getSource()).isEnabled()) {
				return;
			}
			boardSize = _size;
			
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

	class NumPlayerListener implements MouseListener {
		
		private int _num;
		
		NumPlayerListener(int num) {
			_num = num;
		}

		@Override
		public void mouseClicked(MouseEvent e) {
			if (!((TerraceButton)e.getSource()).isEnabled()) {
				return;
			}
			numPlayers = _num;
			switch (_networkType) {
			case LOCAL: // TODO resets names when numPlayers changes
				switch (numPlayers) {
				case 1:
					p2.setText("CPU");
					player2.setText("CPU");
					player3.setEnabled(false);
					player4.setEnabled(false);
					p3.setVisible(false);
					p4.setVisible(false);
					player3.setVisible(false);
					player4.setVisible(false);
					break;
				case 2:
					p2.setText("Player 2");
					player2.setText("Player 2");
					player3.setEnabled(false);
					player4.setEnabled(false);
					player3.setVisible(false);
					player4.setVisible(false);
					p3.setVisible(false);
					p4.setVisible(false);
					break;
				case 3:
					p2.setText("Player 2");
					player2.setText("Player 2");
					p3.setText("Player 3");
					player3.setText("Player 3");
					player3.setEnabled(true);
					p4.setText("CPU");
					player4.setText("CPU");
					player4.setEnabled(true);
					player3.setVisible(true);
					player4.setVisible(true);
					p3.setVisible(true);
					p4.setVisible(true);
					break;
				case 4:
					p2.setText("Player 2");
					player2.setText("Player 2");
					p3.setText("Player 3");
					player3.setText("Player 3");
					player3.setEnabled(true);
					p4.setText("Player 4");
					player4.setText("Player 4");
					player4.setEnabled(true);
					player4.setEnabled(true);
					player3.setVisible(true);
					player4.setVisible(true);
					p3.setVisible(true);
					p4.setVisible(true);
					break;
				}
				break;
			default:
				switch (numPlayers) {
				case 1:
					player2.setEnabled(false);
					player2.setVisible(false);
					player3.setEnabled(false);
					p2.setVisible(false);
					p3.setVisible(false);
					player3.setVisible(false);
					break;
				case 2:
					player2.setEnabled(true);
					player2.setVisible(true);
					player3.setEnabled(false);
					p2.setVisible(true);
					p3.setVisible(false);
					player3.setVisible(false);
					break;
				case 3:
					player2.setEnabled(true);
					player2.setVisible(true);
					player3.setEnabled(true);
					p2.setVisible(true);
					p3.setVisible(true);
					player3.setVisible(true);
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

}
