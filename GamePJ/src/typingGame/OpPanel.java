package typingGame;

import java.awt.Color;
import java.awt.Font;
import java.awt.Image;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import typingGame.MainFrame.RankPanel;

public class OpPanel extends JPanel{
	private GamePanel gamePanel = new GamePanel();
	private User user = new User();
	private int hearts = 3;
	private int score = 0;
	private Color opColor = new Color(153, 204, 255);
	private JLabel userScore = new JLabel("");
	JLabel [] heartLabel = new JLabel[hearts];
	
	public OpPanel() {

		setBackground(opColor);
		setLayout(null);
		
		//heart 이미지 크기 조절
		ImageIcon heart = new ImageIcon("images/heart.png");
		Image img = heart.getImage();
		Image changeImg = img.getScaledInstance(40, 40, Image.SCALE_SMOOTH);
		ImageIcon changeIcon = new ImageIcon(changeImg);

		for (int i=0; i<hearts; i++) {
			heartLabel[i] = new JLabel(changeIcon);
			heartLabel[i].setSize(40,40);
			heartLabel[i].setLocation(40*i+50,50);
			add(heartLabel[i]);
		}
				
		userScore.setText("점수 : "+Integer.toString(score));
		userScore.setBounds(85,100,100,30);
		add(userScore);
		
	}
	
	public synchronized void increase(User user) {
		score += 10;
		user.setScore(score);
		System.out.println(user.getName()+"점수 : "+user.getScore());
		userScore.setText("점수 : "+Integer.toString(score));
	}
	
	public synchronized boolean decreseHeart(User user) {
		hearts--;
		boolean over = false;
		switch(hearts) {
		case 2:
			heartLabel[2].setVisible(false);
			break;
		case 1:
			heartLabel[1].setVisible(false);
			break;
		case 0:
			heartLabel[0].setVisible(false);
			User player = new User(user.getName(), user.getLevel(),user.getScore());
			saveScore(player);
			
			int answer = JOptionPane.showConfirmDialog(this, "게임을 종료 하시겠습니까?", "confirm",JOptionPane.YES_NO_OPTION );
			if(answer==JOptionPane.YES_OPTION){  //사용자가 yes를 눌렀을 경우
				System.out.println("게임을 종료합니다.");
				System.exit(JFrame.EXIT_ON_CLOSE);
			} else{  //사용자가 Yes 이외의 값을 눌렀을 경우
				System.out.println("다시 시작합니다.");
				MainFrame mf = new MainFrame();
				over = true;
			}
			break;
			
		} return over;
	}
	public void saveScore(User user) {
		int level = user.getLevel();
		String name = user.getName();
		int score = user.getScore();
		
		try {
			String fileName = "lv." + level + ".txt";
			FileWriter fw = new FileWriter("text/"+fileName,true); //경로명. text폴더에 저장
			String context = name + "," + score + "\n";
			fw.write(context);
			fw.close();
			sortFile(fileName);
			
		}
		catch(IOException e) {
			System.out.println(e);
		}
	}
	
	public void sortFile(String fileName) {
		try {
			BufferedReader in = new BufferedReader(new InputStreamReader(
					new FileInputStream("text/"+fileName), "UTF-8"));
			FileWriter out = new FileWriter("text/sort."+fileName,false);

			String line;
			String []rank = new String[2];
			
			//해쉬맵에 저장 <name,score>
			Map<String,Integer> temp = new HashMap<>();
			while ((line = in.readLine()) != null) {
				rank = line.trim().split(",");
				temp.put(rank[0], Integer.parseInt(rank[1]));
			}
			
			// 목록 생성
			List<Entry<String, Integer>> ranking = new ArrayList<Entry<String, Integer>>(temp.entrySet());
			
			// Comparator 정렬
			Collections.sort(ranking, new Comparator<Entry<String, Integer>>() {
				public int compare(Entry<String, Integer> obj1, 
						Entry<String, Integer> obj2)
				{
					//점수를 기준으로 내림차순으로 정렬
					return obj2.getValue().compareTo(obj1.getValue());
				}
			});
			
			//정렬된 목록 새로 저장
			for(Entry<String, Integer> entry : ranking) {
				String userRank;
				userRank = entry.getKey() + "," + Integer.toString(entry.getValue()) + "\n";
				out.write(userRank);
				out.flush();
			}
			out.close();
		} catch (IOException e) {
			System.out.println(e);
		} 
	}
}
