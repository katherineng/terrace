package terrace.gui;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

public class StartScreen extends JPanel {
	private TerraceFrame _frame;
	Font headerFont;
	Font defaultFont;
	private Integer numPlayers;
	
	public StartScreen(TerraceFrame frame) {
		_frame = frame;
		headerFont = new Font("Verdana", Font.BOLD, 36);
		defaultFont = new Font("Verdana", Font.BOLD, 20);
		addComponents(this);
	}
	private void addComponents(Container pane) {
		
		pane.setLayout(new GridBagLayout());
		pane.setBackground(Color.GRAY);
		
		JLabel terrace = new JLabel("TERRACE");
		terrace.setFont(headerFont);
		GridBagConstraints terraceConst = new GridBagConstraints();
		terraceConst.gridx = 0;
		terraceConst.gridy = 0;
		terraceConst.anchor = GridBagConstraints.PAGE_START;
		terraceConst.insets = new Insets(30, 0, 30, 0);
		
		JButton localGameButton = new JButton("new local game");
		localGameButton.setActionCommand("local game setup");
		
		JButton newNetGameButton = new JButton("new networked game");
		newNetGameButton.setActionCommand("networked game setup");
		
		JButton joinNetGameButton = new JButton("join networked game");
		joinNetGameButton.setActionCommand("join networked game");
		
		localGameButton.addActionListener(new GameTypeListener());
		newNetGameButton.addActionListener(new GameTypeListener());
		joinNetGameButton.addActionListener(new GameTypeListener());
		
		JPanel startGameCard = new JPanel();
		startGameCard.add(localGameButton);
		startGameCard.add(newNetGameButton);
		startGameCard.add(joinNetGameButton);
		startGameCard.setBackground(Color.GRAY);
		GridBagConstraints startGameConst = new GridBagConstraints();
		startGameConst.gridx = 0;
		startGameConst.gridy = 2;
		
		//Number of players card
		JPanel numPlayersPanel = new JPanel();
		numPlayersPanel.setBackground(Color.GRAY);
		numPlayersPanel.setFont(defaultFont);
		numPlayersPanel.add(new JLabel("Number of local players"));
		GridBagConstraints numPlayersConst = new GridBagConstraints();
		numPlayersConst.gridx = 0;
		numPlayersConst.gridy = 1;
		
		JPanel numPlayersOptions = new JPanel();
		numPlayersOptions.setLayout(new GridBagLayout());
		
		JRadioButton onePlayer = new JRadioButton("1");
		onePlayer.setActionCommand("1");
		onePlayer.setBackground(Color.GRAY);
		onePlayer.addActionListener(new NumPlayerListener());
		GridBagConstraints oneConst = new GridBagConstraints();
		oneConst.gridx = 0;
		oneConst.gridy = 0;
		
		JRadioButton twoPlayer = new JRadioButton("2");
		twoPlayer.setActionCommand("2");
		twoPlayer.setBackground(Color.GRAY);
		twoPlayer.addActionListener(new NumPlayerListener());
		GridBagConstraints twoConst = new GridBagConstraints();
		twoConst.gridx = 1;
		twoConst.gridy = 0;
		
		JRadioButton threePlayer = new JRadioButton("3");
		threePlayer.setActionCommand("3");
		threePlayer.setBackground(Color.GRAY);
		threePlayer.addActionListener(new NumPlayerListener());
		GridBagConstraints threeConst = new GridBagConstraints();
		threeConst.gridx = 0;
		threeConst.gridy = 1;
		
		JRadioButton fourPlayer = new JRadioButton("4");
		fourPlayer.setActionCommand("4");
		fourPlayer.setBackground(Color.GRAY);
		fourPlayer.addActionListener(new NumPlayerListener());
		GridBagConstraints fourConst = new GridBagConstraints();
		fourConst.gridx = 1;
		fourConst.gridy = 1;
		
		ButtonGroup numPlayersButtons = new ButtonGroup();
		numPlayersButtons.add(onePlayer);
		numPlayersButtons.add(twoPlayer);
		numPlayersButtons.add(threePlayer);
		numPlayersButtons.add(fourPlayer);
		
		numPlayersPanel.add(onePlayer, oneConst);
		numPlayersPanel.add(twoPlayer, twoConst);
		numPlayersPanel.add(threePlayer, threeConst);
		numPlayersPanel.add(fourPlayer, fourConst);
		
		pane.add(terrace, terraceConst);
		pane.add(startGameCard, startGameConst);
		pane.add(numPlayersPanel, numPlayersConst);
		
	}
	class NumPlayerListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			System.out.println("dsklfjdaewt");
			numPlayers = Integer.parseInt(e.getActionCommand());
		}
	}
	class GameTypeListener implements ActionListener {
		
		@Override
		public void actionPerformed(ActionEvent e) {
			if(numPlayers != null) {
				_frame.changeCard(e.getActionCommand());
			}
		}
		
	}
	/* private static void createAndShowGUI() {
	        //Create and set up the window.
	        JFrame frame = new JFrame("CardLayoutDemo");
	        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	        frame.setPreferredSize(new Dimension(800, 800));
	        
	        //Create and set up the content pane.
	        StartScreen demo = new StartScreen(frame);
	        demo.addComponents(frame.getContentPane());
	        
	        //Display the window.
	        frame.pack();
	        frame.setVisible(true);
	    }*/
}
