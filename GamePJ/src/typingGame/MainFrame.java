package typingGame;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.*;
import javax.swing.*;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.JTextField;


public class MainFrame extends JFrame {
	private LoginPanel loginPanel = new LoginPanel();
	private GamePanel gamePanel = new GamePanel();
	private User user = new User();
	private OpPanel opPanel = new OpPanel();
	private int score = 0;
	
	public MainFrame() {
		setTitle("산성비 게임");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(850,600);
		setContentPane(loginPanel);
		setLocationRelativeTo(null);
		setVisible(true);
	}
	
	public static void main(String [] args) {
		MainFrame mf = new MainFrame();
	}
	
	public class LoginPanel extends JPanel{
		private JLabel name = new JLabel("이름");
		private JTextField nameField = new JTextField();
		private JLabel level = new JLabel("레벨");
		private String levels[] = {"lv.1", "lv.2", "lv.3", "lv.4", "lv.5"};
		private JComboBox<String> levelCombo = new JComboBox<String>(levels);
		private JButton startBtn = new JButton("게임시작!");
		private JButton rankBtn = new JButton("랭킹보기");
		
		public LoginPanel() {
			this.setLayout(null); //없으면 setBounds 동작 안함
			name.setBounds(280,150,100,30);
			nameField.setBounds(320,150,200,30);
			level.setBounds(280,200,100,30);
			levelCombo.setBounds(320,200,200,30);
			startBtn.setBounds(350,250,130,30);
			rankBtn.setBounds(350,300,130,30);
			
			add(name);
			add(nameField);
			add(level);
			add(levelCombo);
			add(startBtn);
			add(rankBtn);
			
			startBtn.addActionListener(new ActionListener() { //게임시작 버튼 클릭

				@Override
				public void actionPerformed(ActionEvent e) {
					// TODO Auto-generated method stub
					user = new User(nameField.getText(),levelCombo.getSelectedIndex()+1, score);
					user.setName(nameField.getText());
					user.setLevel(levelCombo.getSelectedIndex()+1);
					System.out.println(user.getName()+"님 "+user.getLevel()+"단계 게임을 시작합니다!");
					
					levelCombo.setVisible(false);
					level.setVisible(false);
					name.setVisible(false);
					nameField.setVisible(false);
					startBtn.setVisible(false);
					rankBtn.setVisible(false);
					
					gamePanel = new GamePanel(opPanel, user);
					opPanel = new OpPanel(user);
					
					setLayout(null);
					opPanel.setBounds(650,0,200,600);
					gamePanel.setBounds(0, 0, 650, 600);
					add(gamePanel);
					add(opPanel);
					setResizable(false);
					repaint();
					
					gamePanel.start();

				}
				
			});
			
		}
	}
}
