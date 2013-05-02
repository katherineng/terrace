package terrace.ai;

import javax.swing.JFrame;

import terrace.DefaultBoardGame;
import terrace.Variant;
import terrace.exception.IllegalMoveException;
import terrace.gui.GamePanel;

public class TestMain {

	public static void main(String[] args){
		try {
			JFrame test = new JFrame();
			test.setSize(800, 800);
			GamePanel panel = new GamePanel(new DefaultBoardGame(4, 0, 8, Variant.STANDARD));
			test.add(panel);
			test.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			test.setVisible(true);
		} catch (IllegalMoveException e){
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
	}
}
