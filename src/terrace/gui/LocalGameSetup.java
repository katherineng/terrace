package terrace.gui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

public class LocalGameSetup extends JPanel {
	TerraceFrame _frame;
	Font headerFont;
	Font defaultFont;
	private Integer numPlayers;
	public LocalGameSetup(TerraceFrame frame) {
		_frame = frame;
		headerFont = new Font("Verdana", Font.BOLD, 36);
		defaultFont = new Font("Verdana", Font.BOLD, 20);
		setBackground(Color.GRAY);
		addComponents();
	}
	private void addComponents() {
		setLayout(new GridBagLayout());
		JPanel boardOptions = new JPanel();
		boardOptions.setBackground(Color.GRAY);
		GridBagConstraints boardOptionsConst = new GridBagConstraints();
		boardOptionsConst.gridx = 0;
		boardOptionsConst.gridy = 0;
		boardOptions.setLayout(new BoxLayout(boardOptions, BoxLayout.PAGE_AXIS));
		boardOptions.setFont(defaultFont);
		JLabel boardType = new JLabel("Board Type");
		boardType.setAlignmentX(Component.CENTER_ALIGNMENT);
		JRadioButton triangle = new JRadioButton("triangle");
		triangle.setAlignmentX(Component.CENTER_ALIGNMENT);
		triangle.setBackground(Color.GRAY);
		JRadioButton square = new JRadioButton("square");
		square.setBackground(Color.GRAY);
		square.setAlignmentX(Component.CENTER_ALIGNMENT);
		ButtonGroup options = new ButtonGroup();
		options.add(triangle);
		options.add(square);
		boardOptions.add(boardType);
		boardOptions.add(square);
		boardOptions.add(triangle);
		
		
		JPanel playerNames = new JPanel();
		playerNames.setBackground(Color.GRAY);
		GridBagConstraints playerNamesConst = new GridBagConstraints();
		playerNamesConst.gridx = 1;
		playerNamesConst.gridy = 0;
		playerNames.setLayout(new GridBagLayout());
		JLabel header = new JLabel("Names");
		GridBagConstraints headerConst = new GridBagConstraints();
		headerConst.gridx = 1;
		headerConst.gridy = 0;
		JLabel p1 = new JLabel("Player 1");
		GridBagConstraints p1Const = new GridBagConstraints();
		p1Const.gridx = 0;
		p1Const.gridy = 1;
		JLabel p2 = new JLabel("Player 2");
		GridBagConstraints p2Const = new GridBagConstraints();
		p2Const.gridx = 0;
		p2Const.gridy = 2;
		JTextField player1 = new JTextField(10);
		GridBagConstraints player1Const = new GridBagConstraints();
		player1Const.gridx = 1;
		player1Const.gridy = 1;
		player1Const.insets = new Insets(0, 4, 0, 0);
		JTextField player2 = new JTextField(10);
		GridBagConstraints player2Const = new GridBagConstraints();
		player2Const.gridx = 1;
		player2Const.gridy = 2;
		player2Const.insets = new Insets(0, 4, 0, 0);
		
		playerNames.add(header, headerConst);
		playerNames.add(p1, p1Const);
		playerNames.add(p2, p2Const);
		playerNames.add(player1, player1Const);
		playerNames.add(player2, player2Const);
		
		JButton goButton = new JButton("GO");
		GridBagConstraints goConst = new GridBagConstraints();
		goButton.addActionListener(new GoListener());
		goConst.gridx = 3;
		goConst.gridy = 1;
		
		add(boardOptions, boardOptionsConst);
		add(playerNames, playerNamesConst);
		add(goButton, goConst);
	}
	
	class GoListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			_frame.changeCard("Game");
		}
		
	}
	 
}
