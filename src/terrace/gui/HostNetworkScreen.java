package terrace.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class HostNetworkScreen extends JPanel {
	private TerraceFrame _frame;
	private int _numPlayers;
	private List<String> _playerNames;
	private int numWaitingFor;
	private static final Color backgroundColor = Color.GRAY;
	private static final Color headerColor = Color.BLACK;
	private static final Color defaultColor = Color.WHITE;
	private static final Color fadedColor = Color.LIGHT_GRAY;
	private static final Font headerFont = new Font("Verdana", Font.BOLD, 30);
	private static final Font defaultFont = new Font("Verdana", Font.BOLD, 24);
	
	private JLabel p1;
	private JLabel p2;
	private JLabel p3;
	private JLabel p4;
	
	private JLabel p1Name;
	private JLabel p2Name;
	private JLabel p3Name;
	private JLabel p4Name;
	
	public HostNetworkScreen(TerraceFrame frame) {
		_frame = frame;
		setBackground(backgroundColor);
		setLayout(new GridBagLayout());
		
	}
	public void setPlayerNames(List<String> names) {
		_numPlayers = names.size();
		_playerNames = names;
		addComponents();
	}
	private void addComponents() {
		GridLayout playerNameLayout = new GridLayout(4, 2);
		JPanel currentPlayers = new JPanel();
		currentPlayers.setBackground(backgroundColor);
		currentPlayers.setLayout(playerNameLayout);
		GridBagConstraints currConstraints = new GridBagConstraints();
		currConstraints.gridx = 0;
		currConstraints.gridy = 0;
		
		JLabel currentLabel = new JLabel("Current Players");
		currentLabel.setForeground(headerColor);
		currentLabel.setBackground(backgroundColor);
		
		p1 = new JLabel("Player 1: ");
		p2 = new JLabel("Player 2: ");
		p3 = new JLabel("Player 3: ");
		p4 = new JLabel("Player 4: ");
		
		p1Name = new JLabel();
		p2Name = new JLabel();
		p2Name.setVisible(false);
		p3Name = new JLabel();
		p3Name.setVisible(false);
		p4Name = new JLabel();
		p4Name.setVisible(false);
		
		switch (_numPlayers) {
			case 3: p3Name.setText(_playerNames.get(2));
					p3.setVisible(true);
			case 2: p2Name.setText(_playerNames.get(1));
					p2.setVisible(true);
			case 1: p1Name.setText(_playerNames.get(0));
					p1.setVisible(true);
					System.out.println(_playerNames.get(0));
		}
		
		currentPlayers.add(p1);
		currentPlayers.add(p1Name);
		currentPlayers.add(p2);
		currentPlayers.add(p2Name);
		currentPlayers.add(p3);
		currentPlayers.add(p3Name);
		currentPlayers.add(p4);
		currentPlayers.add(p4Name);
		
		/*JPanel player1 = new JPanel(new FlowLayout());
		player1.setBackground(backgroundColor);
		player1.setAlignmentX(Component.RIGHT_ALIGNMENT);
		player1.add(p1);
		player1.add(p1Name);
		
		JPanel player2 = new JPanel(new FlowLayout());
		player2.setBackground(backgroundColor);
		player2.setAlignmentX(Component.RIGHT_ALIGNMENT);
		player2.add(p2);
		player2.add(p2Name);
		
		JPanel player3 = new JPanel(new FlowLayout());
		player3.setBackground(backgroundColor);
		player3.setAlignmentX(Component.RIGHT_ALIGNMENT);
		player3.add(p3);
		player3.add(p3Name);
		
		JPanel player4 = new JPanel(new FlowLayout());
		player4.setBackground(backgroundColor);
		player4.setAlignmentX(Component.RIGHT_ALIGNMENT);
		player4.add(p4);
		player4.add(p4Name);
		
		currentPlayers.add(player1);
		currentPlayers.add(player2);
		currentPlayers.add(player3);
		currentPlayers.add(player4);*/
		
		add(currentPlayers);
	}
}
