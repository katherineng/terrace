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

public class StartScreen extends JPanel {
	private TerraceFrame _frame;
	Font headerFont;
	Font defaultFont;
	
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
		
		
		
		pane.add(terrace, terraceConst);
		pane.add(startGameCard, startGameConst);
		//pane.add(numPlayersPanel, numPlayersConst);
		
	}

	class GameTypeListener implements ActionListener {
		
		@Override
		public void actionPerformed(ActionEvent e) {
				_frame.changeCard(e.getActionCommand());
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
