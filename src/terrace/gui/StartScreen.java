package terrace.gui;

import java.awt.Color;
import java.awt.Container;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import terrace.exception.IllegalMoveException;

public class StartScreen extends JPanel {
	private TerraceFrame _frame;
	private static final Font headerFont = new Font("Verdana", Font.BOLD, 36);
	private static final Font defaultFont = new Font("Verdana", Font.BOLD, 20);
	
	public StartScreen(TerraceFrame frame) {
		_frame = frame;
		
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
		joinNetGameButton.setActionCommand("join game setup");
		
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
		startGameConst.gridy = 1;
		
		JButton helpButton = new JButton("Help");
		GridBagConstraints helpConst = new GridBagConstraints();
		helpConst.gridx = 0;
		helpConst.gridy = 2;
		helpButton.addActionListener(new HelpButtonListener());
		
		pane.add(terrace, terraceConst);
		pane.add(startGameCard, startGameConst);
		pane.add(helpButton, helpConst);
	}
	
	class GameTypeListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
				try {
					_frame.changeCard(e.getActionCommand());
				} catch (IllegalMoveException e1) {
					// TODO not sure what to do here, swallow for now
				}
		}
	}
	
	class HelpButtonListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			try {
				_frame.changeCard("help screen");
			} catch (IllegalMoveException e1) {
				// TODO not sure what to do here, swallow for now
			}
		}
		
	}
}
