package terrace.gui;

import java.awt.Frame;

import javax.swing.JDialog;
import javax.swing.JLabel;

public class WinnerDialog extends JDialog {
	private static final long serialVersionUID = 5717806098837355989L;
	
	public WinnerDialog(Frame owner, String name) {
		super(owner);
		
		add(new JLabel(name + " has won!"));
	}
}
