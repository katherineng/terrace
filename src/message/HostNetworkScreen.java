package message;

import java.util.List;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class HostNetworkScreen extends JPanel {
	private JFrame _frame;
	private int _numPlayers;
	private List<String> _playerNames;
	private int numWaitingFor;
	
	public HostNetworkScreen(JFrame frame, int numPlayers, List<String> playerNames) {
		_frame = frame;
		_numPlayers = numPlayers;
		_playerNames = playerNames;
		numWaitingFor = _numPlayers - playerNames.size();
		addComponents();
	}
	private void addComponents() {
		
	}
}
