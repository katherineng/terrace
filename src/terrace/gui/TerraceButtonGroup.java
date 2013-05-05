package terrace.gui;

import java.util.LinkedList;
import java.util.List;

import javax.swing.AbstractButton;
import javax.swing.ButtonModel;

public class TerraceButtonGroup {

	private List<TerraceButton> buttons;
	
	private TerraceButton currentButton;
	
	public TerraceButtonGroup() {
		buttons = new LinkedList<>();
	}
	
	public void add(TerraceButton b) {
		buttons.add(b);
		if (b == null) {
			return;
		}
		if (b.isSelected()) {
            if (currentButton == null) {
            	currentButton = b;
            } else {
                b.setSelected(false);
            }
        }
		b.setGroup(this);
	}
	
	public void remove(TerraceButton b) {
        if(b == null) {
            return;
        }
        buttons.remove(b);
        if(b == currentButton) {
            currentButton = null;
        }
        b.setGroup(null);
    }
	
	public void setSelected(TerraceButton button, boolean b) {
        if (b && button != null && button != currentButton) {
            TerraceButton oldSelection = currentButton;
            if (oldSelection != null) {
                oldSelection.setSelected(false, false);
            }
            currentButton = button;
            button.setSelected(true, false);
        }
    }
}
