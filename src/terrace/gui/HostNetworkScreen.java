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
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import terrace.NetworkType;
import terrace.Variant;
import terrace.network.ClientConnection;
import terrace.util.Callback;

public class HostNetworkScreen extends TerracePanel {
	private static final long serialVersionUID = -1281313334499999359L;
	
	private static final Color backgroundColor = Color.DARK_GRAY;
	private static final Color headerColor = Color.WHITE;
	private static final Color defaultColor = Color.WHITE;
	private static final Color fadedColor = Color.GRAY;
	private static final Color highlightColor = new Color(245, 245, 245);
	private static final Font headerFont = new Font("Verdana", Font.BOLD, 30);
	private static final Font defaultFont = new Font("Verdana", Font.BOLD, 16);
	
	private final TerraceFrame _frame;
	private int _numPlayers;
	private Set<String> _localPlayers;
	
	private final DefaultListModel<ClientConnection> _requestListModel = new DefaultListModel<>();
	private final DefaultListModel<String> _currentListModel = new DefaultListModel<>();
	
	private JList<ClientConnection> _requests;
	private JList<String> _currentList;
	
	private final Map<String, ClientConnection> _acceptedClientConnections = new HashMap<>();
	
	// We have to make these dummy controls cause we refer to them in listeners...
	private JButton _addButton = new JButton();
	private JButton _removeButton = new JButton();
	private JLabel _error = new JLabel();
	
	public HostNetworkScreen(TerraceFrame frame) {
		super(frame);
		_frame = frame;
		setBackground(backgroundColor);
		setLayout(new GridBagLayout());
	}
	
	public Callback<ClientConnection> getNewRequestCallback() {
		return new Callback<ClientConnection>() {
			@Override
			public void call(ClientConnection conn) {
				_requestListModel.addElement(conn);
			}
		};
	}
	
	public Callback<ClientConnection> getConnectionDroppedCallback() {
		return new Callback<ClientConnection>() {
			@Override
			public void call(ClientConnection conn) {
				removeClientConnection(conn);
			}
		};
	}
	
	public void removeClientConnection(ClientConnection r) {
		if (!_requestListModel.removeElement(r)) {
			_acceptedClientConnections.remove(r.toString());
			_currentListModel.removeElement(r.toString());
			_numPlayers -= r.getPlayerNames().size();
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
			_currentListModel.addElement(name);
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
		
		_currentList = new JList<String>(_currentListModel);
		_currentList.addListSelectionListener(new CurrListListener());
		_currentList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		_currentList.setSelectedIndex(0);
		_currentList.setVisibleRowCount(5);
		_currentList.setFont(defaultFont);
		_currentList.setForeground(defaultColor);
		_currentList.setCellRenderer(new ListSelectionRenderer());
		JScrollPane currListScrollPane = new JScrollPane(_currentList);
		_currentList.setBackground(fadedColor);

		GridBagConstraints currScrollConstraints = new GridBagConstraints();
		currScrollConstraints.gridx  = 0;
		currScrollConstraints.gridy = 1;
		currScrollConstraints.insets = new Insets(0, 10, 0, 20);
		currListScrollPane.setPreferredSize(new Dimension(300, 300));
		
		_removeButton = new JButton("remove from game");
		_removeButton.addActionListener(new RemoveButtonListener());
		GridBagConstraints removeConst = new GridBagConstraints();
		removeConst.gridx = 0;
		removeConst.gridy = 2;
		removeConst.insets = new Insets(10, 0, 0, 0);

		_addButton = new JButton("add to game");
		GridBagConstraints addConst = new GridBagConstraints();
		addConst.gridx = 1;
		addConst.gridy = 2;
		_addButton.addActionListener(new AddButtonListener());

		JLabel requestLabel = new JLabel("Requests to Join");
		GridBagConstraints requestConst = new GridBagConstraints();
		requestConst.gridx = 1;
		requestConst.gridy = 0;
		requestLabel.setFont(headerFont);
		requestLabel.setForeground(headerColor);
		requestConst.insets = new Insets(0, 0, 20, 0);

		_error = new JLabel();
		GridBagConstraints errorConst = new GridBagConstraints();
		errorConst.gridx = 0;
		errorConst.gridy = 3;
		errorConst.gridwidth = 2;
		errorConst.insets = new Insets(20, 0, 0, 0);
		_error.setFont(defaultFont);
		_error.setForeground(defaultColor);
		_error.setVisible(false);
		
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
		pane.add(_addButton, addConst);
		pane.add(_removeButton, removeConst);
		pane.add(currentLabel, currLabelConst);
		pane.add(requestLabel, requestConst);
		pane.add(_error, errorConst);
		pane.add(backButton, backConst);
		pane.add(goButton, goConst);
	}
	
	private class GoListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			_frame.startGame(new ArrayList<>(_acceptedClientConnections.values()));
		}
	}
	
	private class CurrListListener implements ListSelectionListener {
		@Override
		public void valueChanged(ListSelectionEvent e) {
			if (_currentList.getSelectedIndex() == -1) {
				_removeButton.setEnabled(false);
			} else {
				_removeButton.setEnabled(true);
			}
		}
	}
	
	private class BackListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			if (JOptionPane.showConfirmDialog(
					_frame,
					"Are you sure you would like to quit this server?",
					"Return to Main Menu",
					JOptionPane.YES_NO_OPTION
			) != JOptionPane.YES_OPTION) {
				return;
			} else {
				//TODO shut down server
				_frame.changeCard(TerraceFrame.NETWORK_SETUP);
				_requestListModel.clear();
				_currentListModel.removeAllElements();
				_acceptedClientConnections.clear();
				_error.setVisible(false);
			}
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
			String line = (String) _currentList.getSelectedValue();
			int index = _currentList.getSelectedIndex();
			
			if (index < _localPlayers.size()) {
				_error.setText("Cannot remove local players from game");
				_error.setVisible(true);
			} else {
				_currentListModel.remove(index);
				_numPlayers -= _acceptedClientConnections.get(line).getPlayerNames().size();
				_requestListModel.addElement(_acceptedClientConnections.get(line));
				_acceptedClientConnections.remove(line);
				
				if (_currentListModel.size() != 0) {
					_currentList.setSelectedIndex(0);
				}
				_error.setVisible(false);
			}
		}
	}
	
	private class AcceptClientConnectionListener implements ListSelectionListener {
		@Override
		public void valueChanged(ListSelectionEvent e) {
			if (_requests.getSelectedIndex() == -1) {
				_addButton.setEnabled(false);
			} else {
				_addButton.setEnabled(true);
			}
		}
	}
	
	private class AddButtonListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			ClientConnection r = _requests.getSelectedValue();
			int index = _requests.getSelectedIndex();
			
			if (r == null) {
				return;
			}
			if (_frame._builder.getVariant() == Variant.TRIANGLE) {
				 if (_numPlayers + r.getPlayerNames().size() > 2) {
						_error.setText("Game using the triangular board cannot have more than 2 players");
						_error.setVisible(true);
					}
			} else if (_numPlayers + r.getPlayerNames().size() > 4) {
				_error.setText("Games using the standard board cannot have more than 4 players");
				_error.setVisible(true);
			} else {
				_currentListModel.addElement(r.toString());
				_acceptedClientConnections.put(r.toString(), r);
				_numPlayers += r.getPlayerNames().size();
				
				if (_requestListModel.size() != 0){
					_requests.setSelectedIndex(0);
				}
				_requestListModel.remove(index);
				_error.setVisible(false);
			}
		}
	}
}
