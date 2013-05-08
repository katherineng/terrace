package terrace.gui;

import java.awt.Color;
import java.awt.Container;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;

import terrace.GameBuilder;
import terrace.GameServer;
import terrace.gui.controls.AbstractMouseListener;
import terrace.gui.controls.TerraceButton;
import terrace.gui.controls.TerraceButtonGroup;
import terrace.util.Callback;

public class JoinNetworkScreen extends TerracePanel implements MouseListener {
	private final static int MAX_NAME_LENGTH = 15;
	
	private static final long serialVersionUID = -7114643450789860986L;
	
	private int _numPlayers;
	
	private TerraceFrame _frame;
	private JLabel _portLabel;
	private JLabel _hostLabel;
	private JTextField _portField;
	private JTextField _hostField;
	private JLabel _update;
	
	private JButton goButton;
	private JButton backButton;
	
	private JLabel _p1Label;
	private JLabel _p2Label;
	private JLabel _p3Label;

	private JTextField _p1Field;
	private JTextField _p2Field;
	private JTextField _p3Field;
	
	private JPanel numPlayersPanel;
	private JLabel numPlayersLabel;
	private JPanel numPlayersOptions;
	
	private TerraceButton _onePlayer;
	private TerraceButton _twoPlayer;
	private TerraceButton _threePlayer;
	
	private JPanel namesLabelPanel;
	private JLabel nameHeader;
	private JPanel playerNames;
	
	private static final Color backgroundColor = Color.DARK_GRAY;
	private static final Color headerColor = Color.WHITE;
	private static final Color defaultColor = Color.WHITE;
	private static final Font headerFont = new Font("Verdana", Font.BOLD, 30);
	private static final Font defaultFont = new Font("Verdana", Font.BOLD, 20);
	
	public JoinNetworkScreen(TerraceFrame frame) {
		super(frame);
		_frame = frame;
		_portLabel = new JLabel("Port ");
		_hostLabel = new JLabel("Hostname ");
		_portField = new JTextField(10);
		_hostField = new JTextField(10);
		_update = new JLabel();
		_update.setVisible(false);
		goButton = new JButton("Request to Join");
		backButton = new JButton("Back");
		
		_p1Label = new JLabel("Player 1");
		_p2Label = new JLabel("Player 2");
		_p3Label = new JLabel("Player 3");

		_p1Field = new JTextField(10);
		_p2Field = new JTextField(10);
		_p3Field = new JTextField(10);
		
		numPlayersPanel = new JPanel();
		numPlayersLabel = new JLabel("Number of local players");
		numPlayersOptions = new JPanel();
		
		_onePlayer = new TerraceButton("1");
		_twoPlayer = new TerraceButton("2");
		_threePlayer = new TerraceButton("3");
		
		namesLabelPanel = new JPanel();
		nameHeader = new JLabel("Names");
		playerNames = new JPanel();
		
		setLayout(new GridBagLayout());
		setBackground(backgroundColor);
		JPanel infoPanel = new JPanel(new GridBagLayout());
		infoPanel.setBorder(BorderFactory.createLineBorder(defaultColor));
		infoPanel.setBackground(backgroundColor);
		addComponents(infoPanel);
		add(infoPanel);
	}
	
	private void addComponents(Container pane) {
		_portLabel.setFont(headerFont);
		_portLabel.setForeground(headerColor);
		_hostLabel.setFont(headerFont);
		_hostLabel.setForeground(headerColor);
		_portField.setDocument(new PortInputVerifier());
		_portField.setFont(headerFont);
		_portField.setMargin(new Insets(0, 3, 0, 0));
		_portField.setCaretColor(fadedColor);
		_portField.setBackground(backgroundColor);
		_portField.setForeground(fadedColor);
		_hostField.setFont(headerFont);
		_hostField.setMargin(new Insets(0, 3, 0, 0));
		_hostField.setBackground(backgroundColor);
		_hostField.setForeground(fadedColor);
		_hostField.setCaretColor(fadedColor);
		_update.setFont(defaultFont);
		_update.setForeground(defaultColor);
		
		backButton.addActionListener(new BackListener());
		GridBagConstraints backConst = makeGBC(0, 7);
		backConst.insets = new Insets(0, 10, 10, 0);
		goButton.addActionListener(new GoListener());
		GridBagConstraints goConst = makeGBC(3, 7);
		goConst.insets = new Insets(0, 0, 10, 10);
		
		pane.add(_hostLabel, makeConstraints(1, 0));
		pane.add(_hostField, makeConstraints(2, 0));
		pane.add(_portLabel, makeConstraints(1, 1));
		pane.add(_portField, makeConstraints(2, 1));
		
		GridBagConstraints updateConst = makeConstraints(1, 6);
		updateConst.gridwidth = 2;
		
		namesLabelPanel.setBackground(backgroundColor);
		namesLabelPanel.add(nameHeader);
		GridBagConstraints headerConst = makeGBC(1, 3);
		headerConst.insets = new Insets(5, 0, 5, 0);
		headerConst.gridwidth = 2;
		headerSetting(nameHeader);

		//player names panel
		playerNames.setLayout(new GridBagLayout());
		playerNames.setBorder(BorderFactory.createLineBorder(Color.WHITE));
		playerNames.setBackground(backgroundColor);
		GridBagConstraints playerNamesConst = makeGBC(1, 4);
		playerNamesConst.ipady = 10;
		playerNamesConst.ipadx = 10;
		playerNamesConst.gridwidth = 2;
		playerNamesConst.anchor = GridBagConstraints.NORTH;

		//lables for player textfields
		GridBagConstraints p1LabelConst = makeGBC(0, 1);
		defaultSetting(_p1Label);

		GridBagConstraints p2LabelConst = makeGBC(0, 2);
		defaultSetting(_p2Label);
		_p2Label.setVisible(false);

		GridBagConstraints p3LabelConst = makeGBC(0, 3);
		defaultSetting(_p3Label);
		_p3Label.setVisible(false);
		
		//text fields for player names
		_p1Field.setDocument(new LengthLimit(MAX_NAME_LENGTH));
		_p1Field.addFocusListener(new InputFocusListener("Player 1"));
		GridBagConstraints p1FieldConst = makeGBC(1, 1);
		p1FieldConst.insets = new Insets(0, 4, 0, 0);
		textFieldSetting(_p1Field);
		_p1Field.setText("Player 1");

		_p2Field.addFocusListener(new InputFocusListener("Player 2"));
		_p2Field.setDocument(new LengthLimit(MAX_NAME_LENGTH));
		GridBagConstraints p2FieldConst = makeGBC(1, 2);
		p2FieldConst.insets = new Insets(0, 4, 0, 0);
		_p2Field.setText("Player 2");
		_p2Field.setEnabled(false);
		_p2Field.setVisible(false);
		textFieldSetting(_p2Field);

		_p3Field.addFocusListener(new InputFocusListener("Player 3"));
		_p3Field.setDocument(new LengthLimit(MAX_NAME_LENGTH));
		GridBagConstraints p3FieldConst = makeGBC(1, 3);
		p3FieldConst.insets = new Insets(0, 4, 0, 0);
		textFieldSetting(_p3Field);
		_p3Field.setText("Player 3");
		_p3Field.setEnabled(false);
		_p3Field.setVisible(false);
		textFieldSetting(_p3Field);
		
		playerNames.add(_p1Label, p1LabelConst);
		playerNames.add(_p2Label, p2LabelConst);
		playerNames.add(_p3Label, p3LabelConst);
		playerNames.add(_p1Field, p1FieldConst);
		playerNames.add(_p2Field, p2FieldConst);
		playerNames.add(_p3Field, p3FieldConst);

		//Number of players panel
		numPlayersPanel.setBorder(BorderFactory.createLineBorder(Color.WHITE));
		numPlayersPanel.setBackground(backgroundColor);
		numPlayersLabel.setForeground(headerColor);
		numPlayersLabel.setFont(defaultFont);
		numPlayersPanel.add(numPlayersLabel);		

		GridBagConstraints numPlayersConst = makeGBC(1, 2);
		numPlayersConst.gridwidth = 2;
		numPlayersConst.insets = new Insets(0, 0, 0, 20);

		numPlayersOptions.setLayout(new GridBagLayout());

		defaultSetting(_onePlayer);
		_onePlayer.setSelected(true);
		_onePlayer.addMouseListener(new NumPlayerListener(1));
		GridBagConstraints oneConst = makeGBC(0, 0);

		defaultSetting(_twoPlayer);
		_twoPlayer.addMouseListener(new NumPlayerListener(2));
		GridBagConstraints twoConst = makeGBC(1, 0);

		defaultSetting(_threePlayer);
		_threePlayer.addMouseListener(new NumPlayerListener(3));
		GridBagConstraints threeConst = makeGBC(0, 1);

		TerraceButtonGroup numPlayersButtons = new TerraceButtonGroup();
		numPlayersButtons.add(_onePlayer);
		numPlayersButtons.add(_twoPlayer);
		numPlayersButtons.add(_threePlayer);

		numPlayersPanel.add(_onePlayer, oneConst);
		numPlayersPanel.add(_twoPlayer, twoConst);
		numPlayersPanel.add(_threePlayer, threeConst);
		
		pane.add(_update, updateConst);
		pane.add(namesLabelPanel, headerConst);
		pane.add(playerNames, playerNamesConst);
		pane.add(numPlayersPanel, numPlayersConst);
		pane.add(goButton, goConst);
		pane.add(backButton, backConst);
	}
	public void notifyConnectionLost() {
		_update.setText("Connection has been lost");
		_update.setVisible(true);
	
	}
	public void notifyUnableToConnect() {
		_update.setText("Unable to connect to server");
		_update.setVisible(true);
	}
	public void resetScreen() {
		_hostField.setText("");
		_portField.setText("");
		_p1Field.setText("Player 1");
		_onePlayer.setSelected(true);
		_frame._builder.setNumLocalPlayers(1);
		_p3Field.setEnabled(false);
		_p3Field.setVisible(false);
		_p3Label.setVisible(false);
		_p3Field.setVisible(false);
		_threePlayer.setEnabled(true);
		_p2Field.setVisible(false);
		_p2Field.setText("Player 2");
		_p2Field.setEnabled(false);
		_p2Label.setVisible(false);
		_p3Field.setText("Player 3");
	}
	private GridBagConstraints makeConstraints(int x, int y){
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridx = x;
		gbc.gridy = y;
		gbc.insets = new Insets(10, 0, 10, 0);
		return gbc;
	}
	
	private class BackListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			_frame.changeCard(TerraceFrame.START_SCREEN);
			resetScreen();
		}
	}
	
	private class GoListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			List<String> names = new LinkedList<>();
			switch (_numPlayers) {
			case 3: names.add(_p3Field.getText());
			case 2:	names.add(_p2Field.getText());
			case 1: names.add(_p1Field.getText());
			}
			Collections.reverse(names);
			
			_frame._builder.setPlayerNames(names);			
			_update.setText("trying to connect...");
			_update.setVisible(true);
		}
	}
	private class LengthLimit extends PlainDocument {
		private static final long serialVersionUID = 1159717759453623084L;

		private int limit;

		LengthLimit(int limit) {
			super();
			this.limit = limit;
		}

		@Override
		public void insertString(int offset, String str, AttributeSet attr) throws BadLocationException {
			if (str == null) return;

			if ((getLength() + str.length()) <= limit) {
				super.insertString(offset, str, attr);
			}
		}
	}
	private class InputFocusListener implements FocusListener {
		String _defaultStr;
		InputFocusListener(String defaultStr) {
			_defaultStr = defaultStr;
		}
		@Override
		public void focusGained(FocusEvent e) {}

		@Override
		public void focusLost(FocusEvent e) {
			JTextField source = (JTextField) e.getSource();
			if (source.getText() == null || source.getText().equals("")) {
				source.setText(_defaultStr);
			}
			int end = source.getSelectionEnd();
			source.setCaretPosition(end);
		}

	}
	private class NumPlayerListener extends AbstractMouseListener {
		private int _num;

		NumPlayerListener(int num) {
			_num = num;
		}

		@Override
		public void mouseClicked(MouseEvent e) {
			if (!((TerraceButton)e.getSource()).isEnabled()) {
				return;
			}
			int oldNum = _numPlayers;
			_numPlayers = _num;
			if (oldNum < _num) {
				switch (oldNum) {
				case 1: 
					_p2Field.setText("Player 2");
				case 2:
					_p3Field.setText("Player 3");
				}
			}
			switch (_frame._builder.getNumLocalPlayers()) {
			case 1:
				_p2Field.setEnabled(false);
				_p2Field.setVisible(false);
				_p3Field.setEnabled(false);
				_p2Label.setVisible(false);
				_p3Label.setVisible(false);
				_p3Field.setVisible(false);
				break;
			case 2:
				_p2Field.setEnabled(true);
				_p2Field.setVisible(true);
				_p3Field.setEnabled(false);
				_p2Label.setVisible(true);
				_p3Label.setVisible(false);
				_p3Field.setVisible(false);
				break;
			case 3:
				_p2Field.setEnabled(true);
				_p2Field.setVisible(true);
				_p3Field.setEnabled(true);
				_p2Label.setVisible(true);
				_p3Label.setVisible(true);
				_p3Field.setVisible(true);
				break;
			}
		}
	}
	private class PortInputVerifier extends PlainDocument {
		private static final long serialVersionUID = 1159717759453623084L;

		PortInputVerifier() {
			super();
		}

		@Override
		public void insertString(int offset, String str, AttributeSet attr) throws BadLocationException {
			if (str == null) return;
			char[] chars = str.toCharArray();
			for (char c : chars) {
				if (c >= 48 && c <= 57) {
					super.insertString(offset, Character.toString(c) , attr);
				}
			}
		}
	}
	@Override
	public void mouseClicked(MouseEvent e) {
		this.requestFocus();
	}
	
	@Override
	public void mousePressed(MouseEvent e) {}
	
	@Override
	public void mouseReleased(MouseEvent e) {}
	
	@Override
	public void mouseEntered(MouseEvent e) {}
	
	@Override
	public void mouseExited(MouseEvent e) {}
}
	
