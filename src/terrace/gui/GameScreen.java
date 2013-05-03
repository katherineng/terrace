package terrace.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import terrace.NetworkType;

@SuppressWarnings("serial")
public class GameScreen extends JPanel {
	private static final Color backgroundColor = Color.GRAY;
	private final TerraceFrame _frame;
	private NetworkType _networkType;
	
	
	public GameScreen(GamePanel game, TerraceFrame frame) {
		_frame = frame;
		//_networkType = networkType;
		setBackground(backgroundColor);
		
		setLayout(new BorderLayout());
		
		JPanel topBar = new JPanel();
		topBar.setLayout(new BoxLayout(topBar, BoxLayout.X_AXIS));
		topBar.setBorder(new EmptyBorder(5, 20, 5, 5));
		
		JLabel turn = new JLabel("Player 1's turn");
		JButton mainMenu = new JButton("Main Menu");
		mainMenu.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				int n = JOptionPane.showConfirmDialog(_frame,
				"Are you sure you want to forfeit this match?",
				"Return to Main Menu",
				JOptionPane.YES_NO_OPTION);
				
				if (n == JOptionPane.YES_OPTION) {
					_frame.changeCard("Setup");
					
					if (_networkType == NetworkType.JOIN) {
						
					} else if (_networkType == NetworkType.HOST) {
						
					}
				}
			}
		});
		JButton pause = new JButton("Pause");
		
		topBar.add(turn);
		topBar.add(Box.createHorizontalGlue());
		topBar.add(pause);
		topBar.add(Box.createRigidArea(new Dimension(5, 1)));
		topBar.add(mainMenu);
		
		add(topBar, BorderLayout.PAGE_START);
		add(game, BorderLayout.CENTER);
	}
}
