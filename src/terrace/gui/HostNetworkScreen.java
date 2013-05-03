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
import java.util.HashSet;
import java.util.List;
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

import message.Request;

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
	
	private JList<String> requestList;
	private JButton addButton;
	private DefaultListModel<String> requestListModel;
	
	private DefaultListModel<String> currentListModel;
	private JList<String> currentList;
	private JButton removeButton;
	
	public HostNetworkScreen(TerraceFrame frame) {
		_frame = frame;
		setBackground(backgroundColor);
		setLayout(new GridBagLayout());
		
	}
	public void removeRequest(Request r) {
		//TODO Not sure how to remove from the defaultListmodel
	}
	public void addRequest(Request r) {
		_requestListModel.addElement(r);
	}
	public void setPlayerNames(List<String> names) {
		_numPlayers = names.size();
		_playerNames = names;
		_localPlayers = new HashSet<>();
		_localPlayers.addAll(names);
		addComponents();
	}
	private void addComponents() {
		
		requestListModel = new DefaultListModel();
		_requests = new JList(requestListModel);
		
		JPanel currentPlayers = new JPanel(new GridBagLayout());
		//currentPlayers.setBackground(backgroundColor);
		GridBagConstraints currConstraints = new GridBagConstraints();
		currConstraints.gridx = 0;
		currConstraints.gridy = 1;
		//currConstraints.insets = new Insets(0, 0, 0, 0);
		
		JLabel currentLabel = new JLabel("Current Players");
		currentLabel.setForeground(headerColor);
		currentLabel.setBackground(backgroundColor);
		currentLabel.setFont(headerFont);
		GridBagConstraints currLabelConst = new GridBagConstraints();
		currLabelConst.gridx = 0;
		currLabelConst.gridy = 0;
		currLabelConst.insets = new Insets(0, 0, 20, 0);
		
		currentListModel = new DefaultListModel<>();
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
		
		for(String s: _playerNames) {
			currentListModel.addElement(s);
		}
		
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
		requestList.setFont(defaultFont);
		JScrollPane listScrollPane = new JScrollPane(requestList);
		GridBagConstraints scrollConstraints = new GridBagConstraints();
		scrollConstraints.gridx  = 1;
		scrollConstraints.gridy = 1;
		listScrollPane.setPreferredSize(new Dimension(300, 300));
		
		add(currListScrollPane, currScrollConstraints);
		add(listScrollPane, scrollConstraints);
		add(addButton, addConst);
		add(removeButton, removeConst);
		add(currentLabel, currLabelConst);
		add(requestLabel, requestConst);
	}
	class CurrListListener implements ListSelectionListener {

		@Override
		public void valueChanged(ListSelectionEvent e) {
			if (currentList.getSelectedIndex() == -1) {
				removeButton.setEnabled(false);
			} else if(_localPlayers.size() > currentList.getSelectedIndex()) {
				removeButton.setEnabled(false);
			} else {
				if (_numPlayers >= _localPlayers.size()) {
					removeButton.setEnabled(true);
				}	
			}
		}
	}
	class RemoveButtonListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			String line = (String) currentList.getSelectedValue();
			currentListModel.remove(currentList.getSelectedIndex());
			requestListModel.addElement(line);
			if (_numPlayers <= _localPlayers.size()) {
				removeButton.setEnabled(false);
			}
		}
		
	}
	class AcceptRequestListener implements ListSelectionListener {

		@Override
		public void valueChanged(ListSelectionEvent e) {
			String line = (String) requestList.getSelectedValue();
			if (line == null) {
				return;
			}
			String[] names = line.split(", ");
			if (requestList.getSelectedIndex() == -1) {
				addButton.setEnabled(false);
			} else if (_numPlayers +  names.length > 4) {
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
			currentListModel.addElement(line);
			for(String name : names) {
				_playerNames.add(name);
			}
			_numPlayers += names.length;
			if (_numPlayers == 4) {
				addButton.setEnabled(false);
			}
			requestListModel.remove(index);
		}
		
	}
}
