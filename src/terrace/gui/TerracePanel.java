package terrace.gui;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JPanel;
import javax.swing.JTextField;

import terrace.PlayerColor;

public class TerracePanel extends JPanel {
	private static final long serialVersionUID = 4103874090610020605L;
	
	public static final Color backgroundColor = Color.DARK_GRAY;
	public static final Color headerColor = Color.WHITE;
	public static final Color defaultColor = Color.WHITE;
	public static final Color fadedColor = Color.GRAY;
	
	public static final Font headerFont = new Font("Verdana", Font.BOLD, 30);
	public static final Font defaultFont = new Font("Verdana", Font.BOLD, 24);
	
	private static final Color color1 = PlayerColor.BLUE.toColor();
	private static final Color color2 = PlayerColor.PINK.toColor();
	private static final Color color3 = PlayerColor.YELLOW.toColor();
	private static final Color color4 = PlayerColor.GREEN.toColor();
	private Dimension _size;
	private List<Color> playerColors;
	private TerraceFrame _frame;
	
	public TerracePanel(TerraceFrame frame) {
		_frame = frame;
		_size = new Dimension(_frame.getWidth(), _frame.getHeight());
		playerColors = new LinkedList<>();
		playerColors.add(color1);
		playerColors.add(color2);
		playerColors.add(color3);
		playerColors.add(color4);
	}
	
	private void updateHeight() {
		_size = new Dimension(_frame.getWidth(), _frame.getHeight());
	}
	
	@Override
	public void paintComponent(Graphics g){
		super.paintComponent(g);
		
		Graphics2D g2 = (Graphics2D)g;
		g2.setStroke(new BasicStroke((float) 3.0));
		
		int startx = 0;
		int offset;
		updateHeight();
		
		while (startx <= _size.getWidth()) {
			for (Color c : playerColors) {
				g2.setColor(c);
				offset = startx;
				
				for (int i = 0; i < _size.getHeight(); i += 70) {
					g2.drawLine(offset, i , offset, i + 70);
					g2.drawLine(offset, i + 70, offset + 50, i + 70);
					offset += 50;
				}
				startx += 50;
			}
		}
		int starty = 70;
		Collections.reverse(playerColors);
		
		while (starty <= _size.getHeight()) {
			for (Color c : playerColors) {
				g2.setColor(c);
				offset = starty;
				
				for (int i = 0; i < _size.getWidth(); i += 50) {
					g2.drawLine(i, offset, i + 50, offset);
					g2.drawLine(i + 50, offset, i + 50, offset + 70);
					offset += 70;
				}
				starty += 70;
			}
		}
	}
	public void textFieldSetting(JTextField field) {
		field.setBackground(backgroundColor);
		field.setForeground(fadedColor);
		field.setFont(defaultFont);
		field.setCaretColor(fadedColor);
	}
	public void headerSetting(Component comp) {
		comp.setBackground(backgroundColor);
		comp.setFont(headerFont);
		comp.setForeground(headerColor);
	}
	public void defaultSetting(Component comp) {
		comp.setBackground(backgroundColor);
		comp.setFont(defaultFont);
		comp.setForeground(defaultColor);
	}
	public GridBagConstraints makeGBC(int x, int y) {
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridx = x;
		gbc.gridy = y;
		return gbc;
	}
	
}
