package terrace.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.AbstractButton;
import javax.swing.BorderFactory;
import javax.swing.ButtonModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JToggleButton;

public class TerraceButton extends JPanel implements MouseListener {

	private Color background = Color.DARK_GRAY;
	private Color unselectedColor = Color.GRAY;
	private Color selectedColor = Color.BLACK;
	private Color hoverColor = new Color(100, 100, 100);
	private Color currentColor;
	private Dimension size;
	public Dimension arc;
	private String _text;
	private Font defaultFont = new Font("Verdana", Font.BOLD, 20);
	private boolean isSelected;
	private JLabel label;
	private TerraceButtonGroup buttonGroup;
	
	
	public TerraceButton(String text) {
		_text = text;
		label = new JLabel(_text);
		label.setFont(defaultFont);
		label.setForeground(unselectedColor);
		setBackground(background);
		//setBorder(BorderFactory.createLineBorder(background));
		//super.setMinimumSize(super.getPreferredSize());
		//super.setMaximumSize(super.getPreferredSize());
		addMouseListener(this);
		size = new Dimension(getWidth(), getHeight());
		arc = new Dimension((int)Math.sqrt(size.width), (int)Math.sqrt(size.height));
		isSelected = false;
		currentColor = unselectedColor;
		add(label);
	}
	public TerraceButton(){
		super();
		addMouseListener(this);
		size = new Dimension(getWidth(), getHeight());
		arc = new Dimension((int)Math.sqrt(size.width), (int)Math.sqrt(size.height));
		isSelected = false;
		currentColor = unselectedColor;
	}
	public void setText(String s) {
		_text = s;
		size = new Dimension(getWidth(), getHeight());
		arc = new Dimension((int)Math.sqrt(size.width), (int)Math.sqrt(size.height));
		repaint();
	}
	public boolean isSelected() {
		return isSelected;
	}
	public void setGroup(TerraceButtonGroup g) {
		buttonGroup = g;
	}
	public void setSelected(boolean b) {
		setSelected(b, true);
	}
	public void setSelected(boolean b, boolean resetGroup) {
		isSelected = b;
		if (b) {
			currentColor = selectedColor;
			if (resetGroup) {
				if (buttonGroup != null) {
					buttonGroup.setSelected(this, b);
				}
			}
		} else {
			currentColor = unselectedColor;
		}
		label.setForeground(currentColor);
	}
	
	@Override
	public void setBackground(Color c) {
		super.setBackground(c);
		background = c;
	}
	
	@Override
	public void setFont(Font f) {
		super.setFont(f);
		defaultFont = f;
	}
	
	@Override
	public void mouseClicked(MouseEvent e) {
		if (!isSelected) {
			setSelected(true);
		}
	}

	@Override
	public void mousePressed(MouseEvent e) {}

	@Override
	public void mouseReleased(MouseEvent e) {}

	@Override
	public void mouseEntered(MouseEvent e) {
		if (!isSelected) {
			label.setForeground(hoverColor);
		}
	}

	@Override
	public void mouseExited(MouseEvent e) {
		if (!isSelected) {
			label.setForeground(currentColor);
		}	
	}
	@Override
	public Dimension getPreferredSize() {
		return super.getPreferredSize();
	}
	@Override
	public Dimension getMinimumSize() {
		return getPreferredSize();
	}
	@Override
	public Dimension getMaximumSize() {
		return getPreferredSize();
	}
	/*public static void main(String[] args) {
		JFrame frame = new JFrame();
		JPanel panel = new JPanel();
		panel.add(new TerraceButton("asdfkldjsf"));
		frame.setSize(new Dimension(200, 200));
		frame.add(panel);
		frame.pack();
		frame.setVisible(true);
	}*/
}
