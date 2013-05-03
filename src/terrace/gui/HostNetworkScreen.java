package terrace.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public class HostNetworkScreen extends JPanel {
	private TerraceFrame _frame;
	private int _numPlayers;
	private List<String> _playerNames;
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
	
	private JList<String> requestList;
	private JButton addButton;
	private DefaultListModel<String> requestListModel;
	
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
		currConstraints.gridy = 1;
		currConstraints.insets = new Insets(0, 0, 0, 30);
		
		JLabel currentLabel = new JLabel("Current Players");
		currentLabel.setForeground(headerColor);
		currentLabel.setBackground(backgroundColor);
		
		p1 = new JLabel("Player 1: ");
		p1.setFont(defaultFont);
		p2 = new JLabel("Player 2: ");
		p2.setFont(defaultFont);
		p3 = new JLabel("Player 3: ");
		p3.setFont(defaultFont);
		p4 = new JLabel("Player 4: ");
		p4.setFont(defaultFont);
		
		p1Name = new JLabel();
		p1Name.setFont(defaultFont);
		p2Name = new JLabel();
		p2Name.setFont(defaultFont);;
		p3Name = new JLabel();
		p3Name.setFont(defaultFont);
		p4Name = new JLabel();
		p4Name.setFont(defaultFont);
		
		System.out.println(_numPlayers);
		
		switch (_numPlayers) {
			case 3: p3Name.setText(_playerNames.get(2));
			case 2: p2Name.setText(_playerNames.get(1));
			case 1: p1Name.setText(_playerNames.get(0));
		}
		
		currentPlayers.add(p1);
		currentPlayers.add(p1Name);
		currentPlayers.add(p2);
		currentPlayers.add(p2Name);
		currentPlayers.add(p3);
		currentPlayers.add(p3Name);
		currentPlayers.add(p4);
		currentPlayers.add(p4Name);
		
		addButton = new JButton("add to game");
		GridBagConstraints buttonConst = new GridBagConstraints();
		buttonConst.gridx = 1;
		buttonConst.gridy = 2;
		addButton.addActionListener(new AddButtonListener());

		requestListModel = new DefaultListModel<>();
		requestListModel.addElement("john, joe");
		requestListModel.addElement("dlasfjkldafj");
		requestListModel.addElement("dlasfjsdfasdfdsfdsfdsafsfakldafj");
		requestListModel.addElement("dlasfjkldafj");
		requestListModel.addElement("dlasfjkldafj");
		requestListModel.addElement("dlasfjkldafj");
		requestList = new JList<String>(requestListModel);
		requestList.addListSelectionListener(new AcceptRequestListener());
		requestList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		requestList.setSelectedIndex(0);
		requestList.setVisibleRowCount(5);
		JScrollPane listScrollPane = new JScrollPane(requestList);
		GridBagConstraints scrollConstraints = new GridBagConstraints();
		scrollConstraints.gridx  = 1;
		scrollConstraints.gridy = 1;
		listScrollPane.setPreferredSize(new Dimension(200, 200));
		
		add(currentPlayers, currConstraints);
		add(listScrollPane, scrollConstraints);
		add(addButton, buttonConst);
	}
	class AcceptRequestListener implements ListSelectionListener {

		@Override
		public void valueChanged(ListSelectionEvent e) {
			if (requestList.getSelectedIndex() == -1) {
				addButton.setEnabled(false);
			} else {
				if (_numPlayers != 4) {
					addButton.setEnabled(true);
				}	
			}
		}
	}
	class AddButtonListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			String line = (String) requestList.getSelectedValue();
			String[] names = line.split(", ");
			int index = requestList.getSelectedIndex();
			for(String name : names) {
				if (_numPlayers == 1) {
					_playerNames.add(name);
					p2Name.setText(name);
					requestListModel.remove(index);
					_numPlayers ++;
				} else if (_numPlayers == 2) {
					_playerNames.add(name);
					p3Name.setText(name);
					requestListModel.remove(index);
					_numPlayers++;
				} else if (_numPlayers == 3) {
					_playerNames.add(name);
					p4Name.setText(name);
					requestListModel.remove(index);
					_numPlayers++;
					addButton.setEnabled(false);
				}
			}
		}
		
	}
}
