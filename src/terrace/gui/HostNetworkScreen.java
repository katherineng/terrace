package terrace.gui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.swing.BorderFactory;
import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import terrace.network.ClientConnection;
import terrace.util.Callback;

public class HostNetworkScreen extends TerracePanel {
	private static final long serialVersionUID = -1281313334499999359L;
	
	private TerraceFrame _frame;
	private int _numPlayers;
	private Set<String> _localPlayers;
	private DefaultListModel<ClientConnection> _requestListModel;
	private JList<ClientConnection> _requests;
	private static final Color backgroundColor = Color.DARK_GRAY;
	private static final Color headerColor = Color.WHITE;
	private static final Color defaultColor = Color.WHITE;
	private static final Color fadedColor = Color.GRAY;
	private static final Color highlightColor = new Color(245, 245, 245);
	private static final Font headerFont = new Font("Verdana", Font.BOLD, 30);
	private static final Font defaultFont = new Font("Verdana", Font.BOLD, 16);
	
	//private JList<String> requestList;
	private JButton addButton;
	//private DefaultListModel<String> requestListModel;
	private Map<String, ClientConnection> acceptedClientConnections;
	
	private JLabel error;
	
	private DefaultListModel<String> currentListModel;
	private JList<String> currentList;
	private JButton removeButton;
	
	public HostNetworkScreen(TerraceFrame frame) {
		super(frame);
		_frame = frame;
		acceptedClientConnections = new HashMap<>();
		setBackground(backgroundColor);
		setLayout(new GridBagLayout());
		_requestListModel = new DefaultListModel<>();
		currentListModel = new DefaultListModel<>();
		removeButton = new JButton();
		addButton = new JButton();
	}
	
	public Callback<ClientConnection> getCallback() {
		return new Callback<ClientConnection>() {

			@Override
			public void call(ClientConnection val) {
				_requestListModel.addElement(val);
			}
			
		};
	}
	
	public void removeClientConnection(ClientConnection r) {
		if (!_requestListModel.removeElement(r)) {
			acceptedClientConnections.remove(r.toString());
			currentListModel.removeElement(r.toString());
		}
	}
	
	public void addClientConnection(ClientConnection r) {
		_requestListModel.addElement(r);
	}
	
	public void setPlayerNames(List<String> names) {
		removeAll();
		_numPlayers = names.size();
		_localPlayers = new HashSet<>();
		_localPlayers.addAll(names);
		
		for(String name : names) {
			currentListModel.addElement(name);
		}
		
		JPanel infoPanel = new JPanel(new GridBagLayout());
		infoPanel.setBackground(backgroundColor);
		infoPanel.setBorder(BorderFactory.createLineBorder(defaultColor));
		addComponents(infoPanel);
		add(infoPanel);
	}
	
	private void addComponents(Container pane) {
		List<String> names1 = new ArrayList<>();
		names1.add("name1");
		names1.add("name2");
		names1.add("name3");
		
		List<String> names2 = new ArrayList<>();
		names2.add("longNameldkjsfd");
		
		List<String> names3 = new ArrayList<>();
		names3.add("name");
		
		_requests  = new JList<>(_requestListModel);
		_requests.addListSelectionListener(new AcceptClientConnectionListener());
		JScrollPane requestScroll = new JScrollPane(_requests);
		_requests.setBackground(fadedColor);
		_requests.setForeground(defaultColor);
		_requests.setCellRenderer(new ListSelectionRenderer());
		GridBagConstraints scrollConstraints = new GridBagConstraints();
		scrollConstraints.gridx  = 1;
		scrollConstraints.gridy = 1;
		scrollConstraints.insets = new Insets(0, 0, 0, 10);
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
		currentList.setForeground(defaultColor);
		currentList.setCellRenderer(new ListSelectionRenderer());
		JScrollPane currListScrollPane = new JScrollPane(currentList);
		currentList.setBackground(fadedColor);

		GridBagConstraints currScrollConstraints = new GridBagConstraints();
		currScrollConstraints.gridx  = 0;
		currScrollConstraints.gridy = 1;
		currScrollConstraints.insets = new Insets(0, 10, 0, 20);
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
		errorConst.gridy = 3;
		errorConst.gridwidth = 2;
		errorConst.insets = new Insets(20, 0, 0, 0);
		error.setFont(defaultFont);
		error.setForeground(defaultColor);
		error.setVisible(false);
		
		JButton backButton = new JButton("Back");
		GridBagConstraints backConst = new GridBagConstraints();
		backConst.gridx  = 0;
		backConst.gridy = 4;
		backConst.insets = new Insets(20, 10, 10, 0);
		backConst.anchor = GridBagConstraints.WEST;
		backButton.addActionListener(new BackListener());
		
		JButton goButton = new JButton("Start Game");
		GridBagConstraints goConst = new GridBagConstraints();
		goConst.gridx = 1;
		goConst.gridy = 4;
		goConst.insets = new Insets(20, 0, 10, 10);
		goConst.anchor = GridBagConstraints.EAST;
		goButton.addActionListener(new GoListener());
		
		pane.add(currListScrollPane, currScrollConstraints);
		pane.add(requestScroll, scrollConstraints);
		pane.add(addButton, addConst);
		pane.add(removeButton, removeConst);
		pane.add(currentLabel, currLabelConst);
		pane.add(requestLabel, requestConst);
		pane.add(error, errorConst);
		pane.add(backButton, backConst);
		pane.add(goButton, goConst);
	}
	
	private class GoListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			//TODO not sure what to start
		}
	}
	
	private class CurrListListener implements ListSelectionListener {
		@Override
		public void valueChanged(ListSelectionEvent e) {
			if (currentList.getSelectedIndex() == -1) {
				removeButton.setEnabled(false);
			} else {
				removeButton.setEnabled(true);
			}
		}
	}
	
	private class BackListener implements ActionListener {//TODO add a popup
		@Override
		public void actionPerformed(ActionEvent e) {
			_frame.changeCard("networked game setup");
			_requestListModel.clear();
			currentListModel.removeAllElements();
			acceptedClientConnections.clear();
			error.setVisible(false);
		}
	}
	
	private class ListSelectionRenderer extends DefaultListCellRenderer {
		private static final long serialVersionUID = -4558741714060548611L;
		
		@Override
	     public Component getListCellRendererComponent(
	    		 JList<?> list,
	    		 Object value,
	    		 int index,
	    		 boolean isSelected,
	    		 boolean cellHasFocus
	    ) {
	         Component c = super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
	         
	         if (isSelected) {
	             c.setBackground(highlightColor);
	         }
	         return c;
	     }
	}
	
	private class RemoveButtonListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			String line = (String) currentList.getSelectedValue();
			int index = currentList.getSelectedIndex();
			
			if (index < _localPlayers.size()) {
				error.setText("Cannot remove local players from game");
				error.setVisible(true);
			} else {
				currentListModel.remove(index);
				_numPlayers -= acceptedClientConnections.get(line).getPlayerNames().size();
				_requestListModel.addElement(acceptedClientConnections.get(line));
				acceptedClientConnections.remove(line);
				
				if (currentListModel.size() != 0){
					currentList.setSelectedIndex(0);
				}
				error.setVisible(false);
			}
		}
	}
	
	private class AcceptClientConnectionListener implements ListSelectionListener {
		@Override
		public void valueChanged(ListSelectionEvent e) {
			if (_requests.getSelectedIndex() == -1) {
				addButton.setEnabled(false);
			} else {
				addButton.setEnabled(true);
			}
		}
	}
	
	private class AddButtonListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			ClientConnection r = (ClientConnection) _requests.getSelectedValue();
			int index = _requests.getSelectedIndex();
			
			if (r == null) {
				return;
			}
			if (_numPlayers + r.getPlayerNames().size() > 4) {
				error.setText("Game cannot have more than 4 players");
				error.setVisible(true);
			} else {
				currentListModel.addElement(r.toString());
				acceptedClientConnections.put(r.toString(), r);
				_numPlayers += r.getPlayerNames().size();
				
				if (_requestListModel.size() != 0){
					_requests.setSelectedIndex(0);
				}
				_requestListModel.remove(index);
				error.setVisible(false);
			}
		}
	}
}
