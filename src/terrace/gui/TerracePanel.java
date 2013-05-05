package terrace.gui;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JPanel;

import terrace.PlayerColor;

public class TerracePanel extends JPanel {
	private static final long serialVersionUID = 4103874090610020605L;
	
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
}
