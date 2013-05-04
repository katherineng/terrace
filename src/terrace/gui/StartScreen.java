package terrace.gui;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import terrace.exception.IllegalMoveException;

public class StartScreen extends TerracePanel {
	private TerraceFrame _frame;
	private static final Font headerFont = new Font("Verdana", Font.BOLD, 50);
	private static final Font defaultFont = new Font("Verdana", Font.BOLD, 20);
	private static final Color color1 = Color.BLUE;
	private static final Color color2 = Color.PINK;
	private static final Color color3 = Color.YELLOW;
	private static final Color color4 = Color.GREEN;
	private Dimension _size;
	private List<Color> playerColors;
	public StartScreen(TerraceFrame frame) {
		super(frame);
		_frame = frame;
		_size = new Dimension(_frame.getWidth(), _frame.getHeight());
		playerColors = new LinkedList<>();
		playerColors.add(color1);
		playerColors.add(color2);
		playerColors.add(color3);
		playerColors.add(color4);
		addComponents(this);
	}
	private void addComponents(Container pane) {
		pane.setLayout(new GridBagLayout());
		pane.setBackground(Color.DARK_GRAY);
		JPanel headerPanel = new JPanel();
		JLabel terrace = new JLabel("TERRACE");
		terrace.setFont(headerFont);
		terrace.setForeground(Color.WHITE);
		headerPanel.setBackground(Color.DARK_GRAY);
		GridBagConstraints terraceConst = new GridBagConstraints();
		terraceConst.gridx = 0;
		terraceConst.gridy = 0;
		terraceConst.anchor = GridBagConstraints.PAGE_START;
		terraceConst.insets = new Insets(30, 0, 30, 0);
		headerPanel.add(terrace);
		
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
		startGameCard.setBackground(Color.DARK_GRAY);
		GridBagConstraints startGameConst = new GridBagConstraints();
		startGameConst.gridx = 0;
		startGameConst.gridy = 1;
		
		JPanel helpPanel = new JPanel();
		helpPanel.setBackground(Color.DARK_GRAY);
		JButton helpButton = new JButton("Help");
		GridBagConstraints helpConst = new GridBagConstraints();
		helpConst.gridx = 0;
		helpConst.gridy = 2;
		helpButton.addActionListener(new HelpButtonListener());
		helpPanel.add(helpButton);
		
		pane.add(headerPanel, terraceConst);
		pane.add(startGameCard, startGameConst);
		pane.add(helpPanel, helpConst);
	}
	class GameTypeListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			_frame.changeCard(e.getActionCommand());
		}
	}
	class HelpButtonListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			_frame.changeCard("help screen");
		}
	}
}