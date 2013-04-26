package terrace.gui;

import javax.swing.JFrame;

public class TerraceFrame extends JFrame {
	public TerraceFrame() {
		add(new GamePanel());
		setSize(1200, 1200);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
}
