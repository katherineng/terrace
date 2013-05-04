package terrace.gui;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class JoinNetworkScreen extends JPanel {
	private TerraceFrame _frame;
	private JLabel _portLabel;
	private JLabel _hostLabel;
	private JTextField _portField;
	private JTextField _hostField;
	private JLabel _update;
	
	private JButton goButton;
	private JButton backButton;
	
	private static final Color backgroundColor = Color.GRAY;
	private static final Color headerColor = Color.BLACK;
	private static final Color defaultColor = Color.WHITE;
	private static final Color fadedColor = Color.LIGHT_GRAY;
	private static final Font headerFont = new Font("Verdana", Font.BOLD, 30);
	private static final Font defaultFont = new Font("Verdana", Font.BOLD, 20);
	
	public JoinNetworkScreen(TerraceFrame frame) {
		_frame = frame;
		_portLabel = new JLabel("Port ");
		_hostLabel = new JLabel("Hostname ");
		_portField = new JTextField(10);
		_hostField = new JTextField(10);
		_update = new JLabel();
		_update.setVisible(false);
		goButton = new JButton("Request to Join");
		backButton = new JButton("Back");
		setLayout(new GridBagLayout());
		setBackground(backgroundColor);
		addComponents();
	}
	
	private void addComponents() {
		_portLabel.setFont(headerFont);
		_hostLabel.setFont(headerFont);
		_portField.setFont(headerFont);
		_portField.setBackground(backgroundColor);
		_hostField.setFont(headerFont);
		_hostField.setBackground(backgroundColor);
		_update.setFont(defaultFont);
		_update.setForeground(defaultColor);
		
		backButton.addActionListener(new BackListener());
		goButton.addActionListener(new GoListener());
		
		add(_hostLabel, makeConstraints(0, 0));
		add(_hostField, makeConstraints(1, 0));
		add(_portLabel, makeConstraints(0, 1));
		add(_portField, makeConstraints(1, 1));
		
		GridBagConstraints updateConst = makeConstraints(0, 2);
		updateConst.gridwidth = 2;
		
		add(_update, updateConst);
		
		add(goButton, makeConstraints(1, 3));
		add(backButton, makeConstraints(0, 3));
	}
	private GridBagConstraints makeConstraints(int x, int y){
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridx = x;
		gbc.gridy = y;
		gbc.insets = new Insets(10, 0, 0, 0);
		return gbc;
	}
	public void resetScreen() {
		_hostField.setText("");
		_portField.setText("");
	}
	class BackListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			_frame.changeCard("join game setup");
			resetScreen();
		}
	}
	class GoListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			//TODO request for networking stuff
			_update.setText("trying to connect...");
			_update.setVisible(true);
		}
		
	}
	
}