package terrace.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import terrace.GameBuilder;
import terrace.NetworkType;
import terrace.Player;

public class GameScreen extends JPanel {
	private static final long serialVersionUID = -3415367755182469262L;
	private static final Color backgroundColor = Color.GRAY;
	
	private final TerraceFrame _frame;
	private NetworkType _networkType;
	private GamePanel _panel;
	private GameBuilder _builder;
	private JLabel _status;
	private JLabel _variant;
	private boolean _gameOver = false;
	
	public GameScreen(GameBuilder builder, TerraceFrame frame) {
		_frame = frame;
		_builder = builder;
		_networkType = NetworkType.LOCAL;
		setBackground(backgroundColor);
		
		setLayout(new BorderLayout());
		
		JPanel topBar = new JPanel();
		topBar.setLayout(new BoxLayout(topBar, BoxLayout.X_AXIS));
		topBar.setBorder(new EmptyBorder(5, 20, 5, 5));
		
		_status = new JLabel();
		_status.setFont(new Font("Dialog", Font.BOLD, 16));
		
		JButton mainMenu = new JButton("Main Menu");
		mainMenu.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (!_gameOver) {
					String message;
					
					switch (_networkType) {
					case JOIN:
						message = "Are you sure you want to forfeit this match?";
						break;
					case HOST:
						message = "Are you sure you want to shut down this game server?";
						break;
					default:
						message = "Are you sure you want to quit this game?";
						break;
					}
					
					if (JOptionPane.showConfirmDialog(
							_frame,
							message,
							"Return to Main Menu",
							JOptionPane.YES_NO_OPTION
					) != JOptionPane.YES_OPTION) {
						return;
					}
				}
				_frame.changeCard(TerraceFrame.START_SCREEN);
				
				if (_networkType == NetworkType.JOIN) {
					// TODO: remove self from game, notify other players
				} else if (_networkType == NetworkType.HOST) {
					// TODO: end game, notify other players
				}
			}
		});
		JButton pause = new JButton("Pause");
		_variant = new JLabel("Game Variant: " + _builder.getVariant() +" ");
		
		topBar.add(_status);
		topBar.add(Box.createHorizontalGlue());
		topBar.add(_variant);
		topBar.add(pause);
		topBar.add(Box.createRigidArea(new Dimension(5, 1)));
		topBar.add(mainMenu);
		
		add(topBar, BorderLayout.PAGE_START);
		
		_panel = new GamePanel(_builder.startGame(), _frame, this);
		add(_panel, BorderLayout.CENTER);
	}
	
	GamePanel getGame() {
		return _panel;
	}
	
	public void setCurrPlayer(Player player) {
		_status.setText(player.getName() + "'s turn");
		_status.setForeground(player.getColor().toColor());
	}
	
	public void setWinner(Player player) {
		_gameOver = true;
		_status.setText(player.getName() + " won.");
		_status.setForeground(player.getColor().toColor());
	}
}
