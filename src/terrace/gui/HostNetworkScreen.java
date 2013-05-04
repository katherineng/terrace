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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.SpringLayout;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import terrace.network.Request;

public class HostNetworkScreen extends JPanel {
	private TerraceFrame _frame;
	private int _numPlayers;
	private List<String> _playerNames;
	private Set<String> _localPlayers;
	private DefaultListModel<Request> _requestListModel;
	private JList<Request> _requests;
	private static final Color backgroundColor = Color.GRAY;
	private static final Color headerColor = Color.BLACK;
	private static final Color defaultColor = Color.WHITE;
	private static final Color fadedColor = Color.LIGHT_GRAY;
	private static final Font headerFont = new Font("Verdana", Font.BOLD, 30);
	private static final Font defaultFont = new Font("Verdana", Font.BOLD, 16);
	
	//private JList<String> requestList;
	private JButton addButton;
	//private DefaultListModel<String> requestListModel;
	private Map<String, Request> acceptedRequests;
	
	private JLabel error;
	
	private DefaultListModel<String> currentListModel;
	private JList<String> currentList;
	private JButton removeButton;
	
	public HostNetworkScreen(TerraceFrame frame) {
		_frame = frame;
		acceptedRequests = new HashMap<>();
		setBackground(backgroundColor);
		setLayout(new GridBagLayout());
		_requestListModel = new DefaultListModel<>();
		currentListModel = new DefaultListModel<>();
		removeButton = new JButton();
		addButton = new JButton();
	}
	public void removeRequest(Request r) {
		if (!_requestListModel.removeElement(r)) {
			acceptedRequests.remove(r.toString());
			currentListModel.removeElement(r.toString());
		}
	}
	public void addRequest(Request r) {
		_requestListModel.addElement(r);
	}
	public void setPlayerNames(List<String> names) {
		_numPlayers = names.size();
		_playerNames = names;
		_localPlayers = new HashSet<>();
		_localPlayers.addAll(names);
		for(String name : names) {
			currentListModel.addElement(name);
		}
		addComponents();
	}
	private void addComponents() {
		List<String> names1 = new ArrayList<>();
		names1.add("name1");
		names1.add("name2");
		names1.add("name3");
		
		List<String> names2 = new ArrayList<>();
		names2.add("longNameldkjsfd");
		
		List<String> names3 = new ArrayList<>();
		names3.add("name");
		
		_requests  = new JList<>(_requestListModel);
		_requestListModel.addElement(new Request(names1));
		_requestListModel.addElement(new Request(names2));
		_requestListModel.addElement(new Request(names3));
		_requests.addListSelectionListener(new AcceptRequestListener());
		JScrollPane requestScroll = new JScrollPane(_requests);
		GridBagConstraints scrollConstraints = new GridBagConstraints();
		scrollConstraints.gridx  = 1;
		scrollConstraints.gridy = 1;
		requestScroll.setPreferredSize(new Dimension(300, 300));
		
		JLabel currentLabel = new JLabel("Current Players");
		currentLabel.setForeground(headerColor);
		currentLabel.setBackground(backgroundColor);
		currentLabel.setFont(headerFont);
		GridBagConstraints currLabelConst = new GridBagConstraints();
		currLabelConst.gridx = 0;
		currLabelConst.gridy = 0;
		currLabelConst.insets = new Insets(0, 0, 20, 0);
		
		currentList = new JList<String>(currentListModel);
		currentList.addListSelectionListener(new CurrListListener());
		currentList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		currentList.setSelectedIndex(0);
		currentList.setVisibleRowCount(5);
		currentList.setFont(defaultFont);
		JScrollPane currListScrollPane = new JScrollPane(currentList);
		

		GridBagConstraints currScrollConstraints = new GridBagConstraints();
		currScrollConstraints.gridx  = 0;
		currScrollConstraints.gridy = 1;
		currScrollConstraints.insets = new Insets(0, 0, 0, 20);
		currListScrollPane.setPreferredSize(new Dimension(300, 300));
		
		removeButton = new JButton("remove from game");
		removeButton.addActionListener(new RemoveButtonListener());
		GridBagConstraints removeConst = new GridBagConstraints();
		removeConst.gridx = 0;
		removeConst.gridy = 2;
		removeConst.insets = new Insets(10, 0, 0, 0);

		addButton = new JButton("add to game");
		GridBagConstraints addConst = new GridBagConstraints();
		addConst.gridx = 1;
		addConst.gridy = 2;
		addButton.addActionListener(new AddButtonListener());

		JLabel requestLabel = new JLabel("Requests to Join");
		GridBagConstraints requestConst = new GridBagConstraints();
		requestConst.gridx = 1;
		requestConst.gridy = 0;
		requestLabel.setFont(headerFont);
		requestLabel.setForeground(headerColor);
		requestConst.insets = new Insets(0, 0, 20, 0);

		error = new JLabel();
		GridBagConstraints errorConst = new GridBagConstraints();
		errorConst.gridx = 0;
		errorConst.gridy = 4;
		errorConst.gridwidth = 2;
		errorConst.insets = new Insets(20, 0, 0, 0);
		error.setFont(defaultFont);
		error.setVisible(false);
		
		JButton backButton = new JButton("Back");
		GridBagConstraints backConst = new GridBagConstraints();
		backConst.gridx  = 0;
		backConst.gridy = 3;
		backConst.insets = new Insets(20, 0, 0, 0);
		backConst.anchor = GridBagConstraints.WEST;
		backButton.addActionListener(new BackListener());
		
		JButton goButton = new JButton("Start Game");
		GridBagConstraints goConst = new GridBagConstraints();
		goConst.gridx = 1;
		goConst.gridy = 3;
		goConst.insets = new Insets(20, 0, 0, 0);
		goConst.anchor = GridBagConstraints.EAST;
		goButton.addActionListener(new GoListener());
		
		add(currListScrollPane, currScrollConstraints);
		add(requestScroll, scrollConstraints);
		add(addButton, addConst);
		add(removeButton, removeConst);
		add(currentLabel, currLabelConst);
		add(requestLabel, requestConst);
		add(error, errorConst);
		add(backButton, backConst);
		add(goButton, goConst);
	}
	class GoListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			//TODO not sure what to start
		}
		
	}
	class CurrListListener implements ListSelectionListener {

		@Override
		public void valueChanged(ListSelectionEvent e) {
			if (currentList.getSelectedIndex() == -1) {
				removeButton.setEnabled(false);
			} else {
				removeButton.setEnabled(true);
			}
		}
	}
	class BackListener implements ActionListener {//TODO add a popup

		@Override
		public void actionPerformed(ActionEvent e) {
			_frame.changeCard("networked game setup");
			_requestListModel.clear();
			currentListModel.removeAllElements();
			acceptedRequests.clear();
			error.setVisible(false);
		}
		
	}
	class RemoveButtonListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			String line = (String) currentList.getSelectedValue();
			int index = currentList.getSelectedIndex();
			if (index < _localPlayers.size()) {
				error.setText("Cannot remove local players from game");
				error.setVisible(true);
			} else {
				currentListModel.remove(index);
				_numPlayers -= acceptedRequests.get(line).getNumberOfPlayers();
				_requestListModel.addElement(acceptedRequests.get(line));
				acceptedRequests.remove(line);
				if (currentListModel.size() != 0){
					currentList.setSelectedIndex(0);
				}
				error.setVisible(false);
			}
		}
	}
	class AcceptRequestListener implements ListSelectionListener {

		@Override
		public void valueChanged(ListSelectionEvent e) {
			if (_requests.getSelectedIndex() == -1) {
				addButton.setEnabled(false);
			} else {
				addButton.setEnabled(true);
			}
		}
	}
	class AddButtonListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			Request r = (Request) _requests.getSelectedValue();
			int index = _requests.getSelectedIndex();
			if (r == null) {
				return;
			}
			if (_numPlayers + r.getNumberOfPlayers() > 4) {
				error.setText("Game cannot have more than 4 players");
				error.setVisible(true);
			} else {
				currentListModel.addElement(r.toString());
				acceptedRequests.put(r.toString(), r);
				_numPlayers += r.getNumberOfPlayers();
				if (_requestListModel.size() != 0){
					_requests.setSelectedIndex(0);
				}
				_requestListModel.remove(index);
				error.setVisible(false);
			}
		}
		
	}
}
