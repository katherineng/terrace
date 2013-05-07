package terrace.gui.controls;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JLabel;
import javax.swing.JPanel;

public class TerraceButton extends JPanel implements MouseListener {
	private static final long serialVersionUID = 7859559519269540135L;
	
	private Color unselectedColor = Color.GRAY;
	private Color selectedColor = Color.WHITE;
	private Color hoverColor = new Color(180, 180, 180);
	private Color currentColor;
	private String _text;
	private Font defaultFont = new Font("Verdana", Font.BOLD, 20);
	private boolean isSelected;
	private boolean isEnabled;
	private JLabel label;
	private TerraceButtonGroup buttonGroup;
	
	public TerraceButton(String text) {
		_text = text;
		label = new JLabel(_text);
		label.setFont(defaultFont);
		label.setForeground(unselectedColor);
		setBackground(Color.DARK_GRAY);
		
		addMouseListener(this);
		isSelected = false;
		isEnabled = true;
		currentColor = unselectedColor;
		add(label);
	}
	
	public TerraceButton(){
		super();
		addMouseListener(this);
		isSelected = false;
		currentColor = unselectedColor;
	}
	
	public void setText(String s) {
		_text = s;
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
	public void setEnabled(boolean enabled) {
		boolean oldEnabled = isEnabled;
		super.setEnabled(enabled);
		isEnabled = enabled;
		if (enabled == oldEnabled) {
			return;
		} else {
			if (!enabled) {
				setSelected(false);
				label.setForeground(getBackground());
			} else {
				label.setForeground(unselectedColor);
			}
		}
	}
	
	@Override
	public void setFont(Font f) {
		super.setFont(f);
		defaultFont = f;
	}
	
	@Override
	public void mouseClicked(MouseEvent e) {
		if (!isEnabled) {
			return;
		} else if (!isSelected) {
			setSelected(true);
		}
		requestFocus();
	}
	
	@Override
	public void mousePressed(MouseEvent e) {
		requestFocus();
	}
	
	@Override
	public void mouseReleased(MouseEvent e) {}
	
	@Override
	public void mouseEntered(MouseEvent e) {
		if (!isEnabled) {
			return;
		} else if (!isSelected) {
			label.setForeground(hoverColor);
		}
	}
	
	@Override
	public void mouseExited(MouseEvent e) {
		if (!isEnabled) {
			return;
		} else if (!isSelected) {
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
}
