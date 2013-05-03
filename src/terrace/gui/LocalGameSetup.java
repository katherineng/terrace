package terrace.gui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;


import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;

import terrace.*;
import terrace.exception.IllegalMoveException;


public class LocalGameSetup extends JPanel {
	private static final long serialVersionUID = 1L;
	private TerraceFrame _frame;
	private NetworkType _networkType;
	private static final Font headerFont = new Font("Verdana", Font.BOLD, 30);
	private static final Font defaultFont = new Font("Verdana", Font.BOLD, 24);
	private Integer numPlayers = 1;
	private Variant v = Variant.STANDARD;
	private static final Color backgroundColor = Color.GRAY;
	private static final Color headerColor = Color.BLACK;
	private static final Color defaultColor = Color.WHITE;
	private static final Color fadedColor = Color.LIGHT_GRAY;
	private JLabel p1;
	private JLabel p2;
	private JLabel p3;
	private JLabel p4;
	private JTextField player1;
	private JTextField player2;
	private JTextField player3;
	private JTextField player4;
	private JRadioButton standard;
	private JRadioButton onePlayer;
	private final static int MAX_NAME_LENGTH = 15;
	private int boardSize = 1;// 0 if small 1 if large
	private final String regexp = "[^,]+";
	private JLabel error;
	
	public LocalGameSetup(TerraceFrame frame, NetworkType networkType) {
		_frame = frame;
		_networkType = networkType;
		setBackground(backgroundColor);
		addComponents();
	}
	private void addComponents() {
		setLayout(new GridBagLayout());
		//board size panel
		JPanel boardSize = new JPanel();
		boardSize.setBackground(backgroundColor);
		GridBagConstraints boardSizeConst = new GridBagConstraints();
		boardSizeConst.gridx = 0;
		boardSizeConst.gridy = 1;
		boardSizeConst.insets = new Insets(0,0,0, 30);
		boardSize.setLayout(new BoxLayout(boardSize, BoxLayout.PAGE_AXIS));
		
		JLabel sizeLabel = new JLabel("Board Size");
		sizeLabel.setFont(headerFont);
		sizeLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

		JRadioButton small = new JRadioButton("small");
		small.setActionCommand("0");
		small.addActionListener(new BoardTypeListener());
		small.setFont(defaultFont);
		small.setAlignmentX(CENTER_ALIGNMENT);
		small.setForeground(defaultColor);
		small.setBackground(backgroundColor);

		JRadioButton large = new JRadioButton("large");
		large.setActionCommand("1");
		large.addActionListener(new BoardTypeListener());
		large.setFont(defaultFont);
		large.setAlignmentX(CENTER_ALIGNMENT);
		large.setForeground(defaultColor);
		large.setBackground(backgroundColor);
		large.setSelected(true);

		ButtonGroup sizeGroup = new ButtonGroup();
		sizeGroup.add(small);
		sizeGroup.add(large);

		boardSize.add(sizeLabel);
		boardSize.add(small);
		boardSize.add(large);

		//board type panel
		JPanel boardOptions = new JPanel();
		boardOptions.setBackground(backgroundColor);
		GridBagConstraints boardOptionsConst = new GridBagConstraints();
		boardOptionsConst.gridx = 1;
		boardOptionsConst.gridy = 1;
		boardOptionsConst.insets = new Insets(0,0,0, 30);
		boardOptions.setLayout(new BoxLayout(boardOptions, BoxLayout.PAGE_AXIS));
		
		
		JLabel boardType = new JLabel("Board Type");
		boardType.setFont(headerFont);
		boardType.setAlignmentX(Component.CENTER_ALIGNMENT);

		JRadioButton triangle = new JRadioButton("triangle");
		triangle.setActionCommand("TRIANGLE");
		triangle.addActionListener(new VariantTypeListener());
		triangle.setFont(defaultFont);
		triangle.setAlignmentX(Component.CENTER_ALIGNMENT);
		triangle.setForeground(defaultColor);
		triangle.setBackground(backgroundColor);
		
		standard = new JRadioButton("square - standard rules");
		standard.setActionCommand("STANDARD");
		standard.addActionListener(new VariantTypeListener());
		standard.setFont(defaultFont);
		standard.setBackground(backgroundColor);
		standard.setForeground(defaultColor);
		standard.setAlignmentX(Component.CENTER_ALIGNMENT);
		standard.setSelected(true);

		JRadioButton downhill = new JRadioButton("square - downhill rules");
		downhill.setActionCommand("DOWNHILL");
		downhill.addActionListener(new VariantTypeListener());
		downhill.setFont(defaultFont);
		downhill.setBackground(backgroundColor);
		downhill.setForeground(defaultColor);
		downhill.setAlignmentX(Component.CENTER_ALIGNMENT);

		JRadioButton aggressive = new JRadioButton("square - aggresive rules");
		aggressive.setActionCommand("AGGRESSIVE");
		aggressive.addActionListener(new VariantTypeListener());
		aggressive.setFont(defaultFont);
		aggressive.setBackground(backgroundColor);
		aggressive.setForeground(defaultColor);
		aggressive.setAlignmentX(Component.CENTER_ALIGNMENT);
		
		ButtonGroup options = new ButtonGroup();
		options.add(triangle);
		options.add(standard);
		options.add(downhill);
		options.add(aggressive);
		boardOptions.add(boardType);
		boardOptions.add(standard);
		boardOptions.add(downhill);
		boardOptions.add(aggressive);
		boardOptions.add(triangle);

		//player name panel
		JPanel playerNames = new JPanel();
		playerNames.setBackground(backgroundColor);
		GridBagConstraints playerNamesConst = new GridBagConstraints();
		playerNamesConst.gridx = 2;
		playerNamesConst.gridy = 1;
		playerNames.setLayout(new GridBagLayout());

		JLabel header = new JLabel("Names");
		GridBagConstraints headerConst = new GridBagConstraints();
		headerConst.gridx = 1;
		headerConst.gridy = 0;
		header.setFont(headerFont);
		
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
		player3.setDocument(new LengthLimit(MAX_NAME_LENGTH));
		GridBagConstraints player3Const = new GridBagConstraints();
		player3Const.gridx = 1;
		player3Const.gridy = 3;
		player3Const.insets = new Insets(0, 4, 0, 0);
		player3.setBackground(backgroundColor);
		player3.setForeground(fadedColor);
		player3.setFont(defaultFont);
		player3.setText("Player 3");
		player3.setEnabled(false);
		player3.setVisible(false);
		
		playerNames.add(header, headerConst);
		playerNames.add(p1, p1Const);
		playerNames.add(p2, p2Const);
		playerNames.add(p3, p3Const);
		playerNames.add(p4, p4Const);
		playerNames.add(player1, player1Const);
		playerNames.add(player2, player2Const);
		playerNames.add(player3, player3Const);
		
		//Number of players panel
		JPanel numPlayersPanel = new JPanel();
		numPlayersPanel.setBackground(Color.GRAY);
		JLabel numPlayersLabel = new JLabel("Number of local players");
		numPlayersLabel.setFont(defaultFont);
		numPlayersPanel.add(numPlayersLabel);		
		
		GridBagConstraints numPlayersConst = new GridBagConstraints();
		numPlayersConst.gridx = 0;
		numPlayersConst.gridy = 0;
		numPlayersConst.gridwidth = 3;
		numPlayersConst.insets = new Insets(0, 0, 0, 20);

		JPanel numPlayersOptions = new JPanel();
		numPlayersOptions.setLayout(new GridBagLayout());

		onePlayer = new JRadioButton("1");
		onePlayer.setActionCommand("1");
		onePlayer.setBackground(backgroundColor);
		onePlayer.setForeground(defaultColor);
		onePlayer.setFont(defaultFont);
		onePlayer.setSelected(true);
		onePlayer.addActionListener(new NumPlayerListener());
		GridBagConstraints oneConst = new GridBagConstraints();
		oneConst.gridx = 0;
		oneConst.gridy = 0;

		JRadioButton twoPlayer = new JRadioButton("2");
		twoPlayer.setActionCommand("2");
		twoPlayer.setBackground(backgroundColor);
		twoPlayer.setForeground(defaultColor);
		twoPlayer.setFont(defaultFont);
		twoPlayer.addActionListener(new NumPlayerListener());
		GridBagConstraints twoConst = new GridBagConstraints();
		twoConst.gridx = 1;
		twoConst.gridy = 0;
		
		JRadioButton threePlayer = new JRadioButton("3");
		threePlayer.setActionCommand("3");
		threePlayer.setBackground(backgroundColor);
		threePlayer.setForeground(defaultColor);
		threePlayer.setFont(defaultFont);
		threePlayer.addActionListener(new NumPlayerListener());
		GridBagConstraints threeConst = new GridBagConstraints();
		threeConst.gridx = 0;
		threeConst.gridy = 1;
		
		ButtonGroup numPlayersButtons = new ButtonGroup();
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
		goConst.gridy = 2;
		goConst.insets = new Insets(30, 0, 0,0);
		
		JButton backButton = new JButton("Back");
		backButton.addActionListener(new BackListener());
		GridBagConstraints backConst = new GridBagConstraints();
		backConst.gridx = 0;
		backConst.gridy = 2;
		backConst.insets = new Insets(30, 0, 0,0);
		
		error = new JLabel("");
		error.setPreferredSize(new Dimension(250, 50));
		GridBagConstraints errorConst = new GridBagConstraints();
		error.setVisible(false);
		errorConst.gridx = 1;
		errorConst.gridy = 5;
		//errorConst.gridwidth = 2;
		
		
		add(playerNames, playerNamesConst);
		add(goButton, goConst);
		add(numPlayersPanel, numPlayersConst);
		add(backButton, backConst);
		playerNames.add(error, errorConst);
		
		if(_networkType == NetworkType.LOCAL) {
			player2.setEnabled(true);
			player2.setVisible(true);
			p2.setVisible(true);
			p2.setText("CPU");
			player2.setText("CPU");
			
			player4 = new JTextField(10);
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
			
			JRadioButton fourPlayer = new JRadioButton("4");
			fourPlayer.setActionCommand("4");
			fourPlayer.setBackground(backgroundColor);
			fourPlayer.setForeground(defaultColor);
			fourPlayer.setFont(defaultFont);
			fourPlayer.addActionListener(new NumPlayerListener());
			GridBagConstraints fourConst = new GridBagConstraints();
			fourConst.gridx = 1;
			fourConst.gridy = 1;
			numPlayersButtons.add(fourPlayer);
			numPlayersPanel.add(fourPlayer, fourConst);
			
			goButton.setText("Start Game");
			
			
			
		} else if(_networkType == NetworkType.HOST) {
			goButton.setText("Create Game");
		} else {
			goButton.setText("Join Game");
		}
		
		if (_networkType != NetworkType.JOIN) {
			add(boardOptions, boardOptionsConst);
			add(boardSize, boardSizeConst);
		}
		
		
	}
	private void resetScreen() {
		switch (_networkType) {
		case LOCAL: standard.setSelected(true);
					onePlayer.setSelected(true);
					p2.setText("CPU");
					player2.setText("CPU");
					player3.setEnabled(false);
					player4.setEnabled(false);
					p3.setVisible(false);
					p4.setVisible(false);
					player3.setVisible(false);
					player4.setVisible(false);
					break;
		default :	standard.setSelected(true);
					onePlayer.setSelected(true);
					player2.setVisible(false);
					player2.setEnabled(false);
					p2.setVisible(false);
					player3.setEnabled(false);
					p3.setVisible(false);
					player3.setVisible(false);
					break;
		}
		
	}
	class BackListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			_frame.changeCard("Setup");
			resetScreen();
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
	//[^,]+
	class NameLengthListener implements KeyListener {

		@Override
		public void keyTyped(KeyEvent e) {
			JTextField source = (JTextField) e.getSource();
			String word = (String)source.getText();
			char key = e.getKeyChar();
			if(key == 127) {
				source.setText(word.substring(0, 0));
			}
			if(word.length() < MAX_NAME_LENGTH -1) {
				source.setText(word + e.getKeyChar());
			}
		}

		@Override
		public void keyPressed(KeyEvent e) {
		}

		@Override
		public void keyReleased(KeyEvent e) {
		}
		
	}
	class LengthLimit extends PlainDocument {
		  private int limit;
		  LengthLimit(int limit) {
		    super();
		    this.limit = limit;
		  }
		  public void insertString(int offset, String str, AttributeSet attr) throws BadLocationException {
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
		} else if (!player4.getText().matches(regexp)) {
			return 4;
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
				setErrorMsg("<html>Player " + n + "'s name may not contain commas (,)</html>");
				return;
			}
			
			_frame._builder.setNumLocalPlayers(numPlayers);
			List<String> playerNames = new ArrayList<>();
			if(_networkType == NetworkType.LOCAL) {
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
				case 3: playerNames.add(player3.getText());
				case 2: playerNames.add(player2.getText());
				case 1: playerNames.add(player1.getText());
				}
			}
			Collections.reverse(playerNames);
			_frame.setPlayerNames(playerNames);
			if (v == Variant.TRIANGLE) {
				if (boardSize == 0) {
					_frame._builder.setSize(4);
				} else {
					_frame._builder.setSize(3);
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
	
	class VariantTypeListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			v = Variant.valueOf(e.getActionCommand());
		}
		
	}
	
	class BoardTypeListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			boardSize = Integer.parseInt(e.getActionCommand());
		}
		
	}
	
	class NumPlayerListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			numPlayers = Integer.parseInt(e.getActionCommand());
			switch (_networkType) {
			case LOCAL: //TODO resets names when numPlayers changes
				switch (numPlayers) {
				case 1: p2.setText("CPU");
						player2.setText("CPU");
						player3.setEnabled(false);
						player4.setEnabled(false);
						p3.setVisible(false);
						p4.setVisible(false);
						player3.setVisible(false);
						player4.setVisible(false);
						break;
				case 2: p2.setText("Player 2");
						player2.setText("Player 2");
						player3.setEnabled(false);
						player4.setEnabled(false);
						player3.setVisible(false);
						player4.setVisible(false);
						p3.setVisible(false);
						p4.setVisible(false);
						break;
				case 3: p2.setText("Player 2");
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
				case 4: p2.setText("Player 2");
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
				case 1: player2.setEnabled(false);
						player2.setVisible(false);
						player3.setEnabled(false);
						p2.setVisible(false);
						p3.setVisible(false);
						player3.setVisible(false);
						break;
				case 2: player2.setEnabled(true);
						player2.setVisible(true);
						player3.setEnabled(false);
						p2.setVisible(true);
						p3.setVisible(false);
						player3.setVisible(false);
						break;
				case 3: player2.setEnabled(true);
						player2.setVisible(true);
						player3.setEnabled(true);
						p2.setVisible(true);
						p3.setVisible(true);
						player3.setVisible(true);
						break;
				}
			}
		}
	}

}
