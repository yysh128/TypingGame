package typingGame;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.awt.*;
import javax.swing.*;

public class MainFrame extends JFrame {
	private LoginPanel loginPanel = new LoginPanel();
	private GamePanel gamePanel = new GamePanel();
	private User user = new User();
	private OpPanel opPanel = new OpPanel();
	private JButton homeBtn = new JButton("메인화면");
	private int score = 0;

	public MainFrame() {
		setTitle("산성비 게임");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(850, 600);
		setContentPane(loginPanel);
		setLocationRelativeTo(null);
		setVisible(true);
		setResizable(false);
	}

	public static void main(String[] args) {
		MainFrame mf = new MainFrame();
	}

	public class LoginPanel extends JPanel {
		private JLabel name = new JLabel("이름");
		private JTextField nameField = new JTextField();
		private JLabel level = new JLabel("레벨");
		private String levels[] = { "lv.1", "lv.2", "lv.3", "lv.4", "lv.5" };
		private JComboBox<String> levelCombo = new JComboBox<String>(levels);
		private JButton startBtn = new JButton("게임시작!");
		private JButton rankBtn = new JButton("랭킹보기");
		private JLabel title = new JLabel("Typing Game");

		public void paintComponent(Graphics g) {
			super.paintComponent(g);
			ImageIcon icon = new ImageIcon("images/introImage.jpg");
			Image img = icon.getImage();
			g.drawImage(img, 0, 0, getWidth(), getHeight(), this);
		}

		public LoginPanel() {
			this.setLayout(null); // 없으면 setBounds 동작 안함
			title.setBounds(120, 70, 300, 70);
			name.setBounds(130, 230, 100, 30);
			nameField.setBounds(170, 230, 200, 30);
			level.setBounds(130, 280, 100, 30);
			levelCombo.setBounds(170, 280, 200, 30);
			startBtn.setBounds(200, 330, 130, 30);
			rankBtn.setBounds(200, 380, 130, 30);

			title.setFont(new Font("Serif", Font.BOLD, 50));

			add(title);
			add(name);
			add(nameField);
			add(level);
			add(levelCombo);
			add(startBtn);
			add(rankBtn);

			startBtn.addActionListener(new ActionListener() { // 게임시작 버튼 클릭

				@Override
				public void actionPerformed(ActionEvent e) {
					// TODO Auto-generated method stub
					user = new User(nameField.getText(), levelCombo.getSelectedIndex() + 1, score);
					user.setName(nameField.getText());
					user.setLevel(levelCombo.getSelectedIndex() + 1);
					System.out.println(user.getName() + "님 " + user.getLevel() + "단계 게임을 시작합니다!");

					UnVisibleLogin();

					gamePanel = new GamePanel(opPanel, user);
					InfoPanel infoPanel = new InfoPanel(user);

					setLayout(null);
					opPanel.setBounds(630, 0, 220, 200);
					infoPanel.setBounds(630, 200, 220, 400);
					gamePanel.setBounds(0, 0, 630, 600);
					add(gamePanel);
					add(opPanel);
					add(infoPanel);
					setResizable(false);
					repaint();
					gamePanel.start(user);
				}
			});

			// 랭킹버튼클릭
			rankBtn.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					user = new User(nameField.getText(), levelCombo.getSelectedIndex() + 1, score);
					user.setLevel(levelCombo.getSelectedIndex() + 1);
					UnVisibleLogin();

					RankPanel rankPanel = new RankPanel(user);
					setLayout(null);
					rankPanel.setBounds(0, 0, 850, 600);
					add(rankPanel);

					// 메인화면 버튼 클릭
					homeBtn.addActionListener(new ActionListener() {
						@Override
						public void actionPerformed(ActionEvent e) {
							rankPanel.setVisible(false);
							VisibleLogin();
						}
					});
				}
			});
		}

		public void UnVisibleLogin() {
			levelCombo.setVisible(false);
			level.setVisible(false);
			name.setVisible(false);
			nameField.setVisible(false);
			startBtn.setVisible(false);
			rankBtn.setVisible(false);
			title.setVisible(false);
		}

		public void VisibleLogin() {
			levelCombo.setVisible(true);
			level.setVisible(true);
			name.setVisible(true);
			nameField.setVisible(true);
			startBtn.setVisible(true);
			rankBtn.setVisible(true);
			title.setVisible(true);
		}

	}

	public class InfoPanel extends JPanel {
		private JLabel userNick = new JLabel("");
		private JLabel userLevel = new JLabel("");
		private Color backColor = new Color(153, 204, 255);

		public InfoPanel(User user) {
			setBackground(backColor);
			userNick.setText(user.getName());
			userLevel.setText("Lv." + Integer.toString(user.getLevel()));

			add(userNick);
			add(userLevel);
		}
	}

	public class RankPanel extends JPanel {
		private JLabel rankLabel = new JLabel("Top 10");
		private JLabel[] rankNum = new JLabel[10];
		private JLabel[] rankName = new JLabel[10];
		private String[] rankText = new String[10];
		private JLabel[] scoreText = new JLabel[10];
		private String line;
		private String[] rankInfo = new String[2];

		public RankPanel(User user) {

			setLayout(null);
			rankLabel.setBounds(350, 30, 150, 60);
			rankLabel.setFont(new Font("Serif", Font.BOLD, 40));
			homeBtn.setBounds(370, 500, 100, 30);
			add(homeBtn);
			add(rankLabel);

			String fileName = "sort.lv." + user.getLevel() + ".txt";

			try {

				BufferedReader in = new BufferedReader(
						new InputStreamReader(new FileInputStream("text/" + fileName), "UTF-8"));

				int rank = 0;

				while (rank < 10) {
					line = in.readLine();
					if (line == null)
						break;
					// 순위
					rankInfo = line.trim().split(",");
					rankText[rank] = Integer.toString(rank + 1);
					rankNum[rank] = new JLabel(rankText[rank]);
					rankNum[rank].setHorizontalAlignment(JLabel.CENTER);
					rankNum[rank].setBounds(100, 120 + rank * 30, 30, 30);
					rankNum[rank].setFont(new Font("굴림", Font.BOLD, 16));
					// 이름
					rankName[rank] = new JLabel(rankInfo[0]);
					rankName[rank].setHorizontalAlignment(JLabel.CENTER);
					rankName[rank].setBounds(320, 120 + rank * 30, 150, 30);
					rankName[rank].setFont(new Font("굴림", Font.BOLD, 16));
					// 점수
					scoreText[rank] = new JLabel(rankInfo[1]);
					scoreText[rank].setHorizontalAlignment(JLabel.CENTER);
					scoreText[rank].setBounds(680, 120 + rank * 30, 60, 30);
					scoreText[rank].setFont(new Font("굴림", Font.BOLD, 16));

					add(rankNum[rank]);
					add(rankName[rank]);
					add(scoreText[rank]);
					rank++;
				}
			} catch (IOException e1) {
				System.out.println(e1);
			}

		}
	}

}
