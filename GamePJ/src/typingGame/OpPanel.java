package typingGame;

import java.awt.Color;
import java.awt.Font;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class OpPanel extends JPanel{
	private int hearts = 3;
	private int score = 0;
	//private OpPanel opPanel = new OpPanel();
	
	public OpPanel() {
		
	}
	public OpPanel(User user) {
		int level = user.getLevel();
		String userName = user.getName();
		
		JLabel userNick = new JLabel("");
		userNick.setText(userName);
		JLabel userLevel = new JLabel("");
		userLevel.setText("Lv." + Integer.toString(level));
		JLabel userScore = new JLabel("");
		userScore.setText(Integer.toString(score));
		
		add(userNick);
		add(userLevel);
		add(userScore);
		
	}
	
}
