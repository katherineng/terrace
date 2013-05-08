package terrace.gui;

import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

//TODO redo for the new background
public class HelpScreen extends TerracePanel {
	private static final long serialVersionUID = -6419195153812519273L;
	
	private TerraceFrame _frame;
	private JTextArea textArea;
	private JLabel terraceLabel;
	private JScrollPane scroll;
	private JButton backButton;
	
	private static final Font headerFont = new Font("Verdana", Font.BOLD, 36);
	private static final Font defaultFont = new Font("Verdana", Font.BOLD, 16);
	
	private static final Color backgroundColor = Color.DARK_GRAY;
	private static final Color textColor = Color.WHITE;
	private static final Color headerColor = Color.WHITE;
	private static final Color fadedColor = Color.GRAY;
	
	public HelpScreen(TerraceFrame frame) {
		super(frame);
		_frame = frame;
		setBackground(backgroundColor);
		setLayout(new GridBagLayout());
		JPanel info = new JPanel(new GridBagLayout());
		info.setBackground(backgroundColor);
		info.setBorder(BorderFactory.createLineBorder(Color.white));
		addComponents(info);
		add(info);
	}
	
	private void addComponents(Container pane) {
		setLayout(new GridBagLayout());
		terraceLabel = new JLabel("TERRACE");
		terraceLabel.setFont(headerFont);
		terraceLabel.setForeground(headerColor);
		terraceLabel.setBackground(backgroundColor);
		//terraceLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		GridBagConstraints labelConst = new GridBagConstraints();
		labelConst.gridx = 0;
		labelConst.gridy = 0;
		labelConst.insets = new Insets(10, 0, 10, 0);
		
		textArea = new JTextArea();
		
		//textArea.setBackground(backgroundColor);
		//textArea.setForeground(textColor);
		textArea.setColumns(5);
		textArea.setLineWrap(true);
		textArea.setRows(2);
		textArea.setWrapStyleWord(true);
		textArea.setEditable(false);
		scroll = new JScrollPane(textArea);
		scroll.setPreferredSize(new Dimension(500,300));
		
		GridBagConstraints textConst = new GridBagConstraints();
		textConst.gridx = 0;
		textConst.gridy = 1;
		textConst.insets = new Insets(0, 10, 0, 10);
		textArea.setSize(new Dimension(500, 300));
		textArea.setEditable(false);
		textArea.setFont(defaultFont);
		textArea.setBackground(fadedColor);
		
		textArea.append("Terrace is a stratey game played on a three dimensional board.\n");
		textArea.append("There are two types of boards for this game: Triangle and Square.\n");
		textArea.append("The Square board also has 3 rule variants: Standard, Downhill, and Agressive.\n");
		textArea.append(".....");
		backButton = new JButton("Back");
		backButton.addActionListener(new BackListener());
		GridBagConstraints backConst = new GridBagConstraints();
		backConst.gridx = 0;
		backConst.gridy = 2;
		backConst.insets = new Insets(20, 0, 10 ,0);
		
		pane.add(terraceLabel, labelConst);
		//add(textArea, textConst);
		pane.add(scroll, textConst);
		pane.add(backButton, backConst);
	}
	
	class BackListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			_frame.changeCard(TerraceFrame.START_SCREEN);
		}
	}
}
