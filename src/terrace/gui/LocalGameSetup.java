package terrace.gui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;


import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

import terrace.NetworkType;
import terrace.Variant;


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
	
	public LocalGameSetup(TerraceFrame frame, NetworkType networkType) {
		_frame = frame;
		_networkType = networkType;
		System.out.println(networkType);
		setBackground(backgroundColor);
		addComponents();
	}
	private void addComponents() {
		setLayout(new GridBagLayout());
		JPanel boardOptions = new JPanel();

		boardOptions.setBackground(backgroundColor);
		GridBagConstraints boardOptionsConst = new GridBagConstraints();
		boardOptionsConst.gridx = 0;
		boardOptionsConst.gridy = 1;
		boardOptionsConst.insets = new Insets(0,0,0, 30);
		boardOptions.setLayout(new BoxLayout(boardOptions, BoxLayout.PAGE_AXIS));

		JLabel boardType = new JLabel("Board Type");
		boardType.setFont(headerFont);
		boardType.setAlignmentX(Component.CENTER_ALIGNMENT);

		JRadioButton triangle = new JRadioButton("triangle");
		triangle.setActionCommand("TRIANGLE");
		triangle.setFont(defaultFont);
		triangle.setAlignmentX(Component.CENTER_ALIGNMENT);
		triangle.setForeground(defaultColor);
		triangle.setBackground(backgroundColor);
		
		standard = new JRadioButton("square - standard rules");
		standard.setActionCommand("STANDARD");
		standard.setFont(defaultFont);
		standard.setBackground(backgroundColor);
		standard.setForeground(defaultColor);
		standard.setAlignmentX(Component.CENTER_ALIGNMENT);
		standard.setSelected(true);

		JRadioButton downhill = new JRadioButton("square - downhill rules");
		downhill.setActionCommand("DOWNHILL");
		downhill.setFont(defaultFont);
		downhill.setBackground(backgroundColor);
		downhill.setForeground(defaultColor);
		downhill.setAlignmentX(Component.CENTER_ALIGNMENT);

		JRadioButton aggressive = new JRadioButton("square - aggresive rules");
		aggressive.setActionCommand("AGGRESSIVE");
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


		JPanel playerNames = new JPanel();
		playerNames.setBackground(backgroundColor);
		GridBagConstraints playerNamesConst = new GridBagConstraints();
		playerNamesConst.gridx = 1;
		playerNamesConst.gridy = 1;
		playerNames.setLayout(new GridBagLayout());

		JLabel header = new JLabel("Names");
		GridBagConstraints headerConst = new GridBagConstraints();
		headerConst.gridx = 1;
		headerConst.gridy = 0;
		header.setFont(headerFont);

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
		
		player1 = new JTextField(10);
		GridBagConstraints player1Const = new GridBagConstraints();
		player1Const.gridx = 1;
		player1Const.gridy = 1;
		player1Const.insets = new Insets(0, 4, 0, 0);
		player1.setBackground(backgroundColor);
		player1.setForeground(fadedColor);
		player1.setFont(defaultFont);
		player1.setText("Player 1");

		player2 = new JTextField(10);
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
		
		//Number of players card
		JPanel numPlayersPanel = new JPanel();
		numPlayersPanel.setBackground(Color.GRAY);
		JLabel numPlayersLabel = new JLabel("Number of local players");
		numPlayersLabel.setFont(defaultFont);
		numPlayersPanel.add(numPlayersLabel);		
		
		GridBagConstraints numPlayersConst = new GridBagConstraints();
		numPlayersConst.gridx = 0;
		numPlayersConst.gridy = 0;
		numPlayersConst.gridwidth = 2;
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
		
		add(playerNames, playerNamesConst);
		add(goButton, goConst);
		add(numPlayersPanel, numPlayersConst);
		add(backButton, backConst);
		
		if(_networkType == NetworkType.LOCAL) {
			player2.setEnabled(true);
			player2.setVisible(true);
			p2.setVisible(true);
			p2.setText("CPU");
			player2.setText("CPU");
			
			player4 = new JTextField(10);
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
			resetScreen();//maybe switch these two lines?
		}
		
	}
	class GoListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			_frame.setNumPlayers(numPlayers);
			List<String> playerNames = new ArrayList<>();
			playerNames.add(player1.getText());
			playerNames.add(player2.getText());
			if (numPlayers > 2) {
				playerNames.add(player3.getText());
				playerNames.add(player4.getText());
			}
			_frame.setPlayerNames(playerNames);
			_frame.setVariant(v);
			_frame.changeCard("Game");
		}

	}
	class variantTypeListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			v = Variant.valueOf(e.getActionCommand());
		}
		
	}
	class NumPlayerListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			numPlayers = Integer.parseInt(e.getActionCommand());
			switch (_networkType) {
			case LOCAL: 
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
				case 2: p2.setText("Player2");
						player2.setText("Player2");
						player3.setEnabled(false);
						player4.setEnabled(false);
						player3.setVisible(false);
						player4.setVisible(false);
						p3.setVisible(false);
						p4.setVisible(false);
						break;
				case 3: p2.setText("Player2");
						player2.setText("Player2");
						p3.setText("Player3");
						player3.setText("Player3");
						player3.setEnabled(true);
						p4.setText("CPU");
						player4.setText("CPU");
						player4.setEnabled(true);
						player3.setVisible(true);
						player4.setVisible(true);
						p3.setVisible(true);
						p4.setVisible(true);
						break;
				case 4: p2.setText("Player2");
						player2.setText("Player2");
						p3.setText("Player3");
						player3.setText("Player3");
						player3.setEnabled(true);
						p4.setText("Player4");
						player4.setText("Player4");
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
				System.out.println("sd;kfjdas");
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
