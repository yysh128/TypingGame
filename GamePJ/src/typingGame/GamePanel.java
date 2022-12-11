package typingGame;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import typingGame.MainFrame.RankPanel;

public class GamePanel extends JPanel {
	private JTextField textField = new JTextField(50);
	private User user = new User();
	private OpPanel opPanel = null;
	private InputPanel inputPanel = new InputPanel();
	private WordPanel wordPanel = new WordPanel();
	private Vector<JLabel> word= new Vector<JLabel>();
	private CreateWordThread createWordThread = new CreateWordThread(word);
	private DownWordThread downWordThread = new DownWordThread(word);
	private NoticeWordThread noticeWordThread = new NoticeWordThread(word);
	private RankPanel rankPanel = null;

	private int heart = 3;
	
	public GamePanel() {

	}

	public GamePanel(OpPanel opPanel, User user) {
		this.opPanel = opPanel;
		this.user = user;

		this.setLayout(null);
		wordPanel.setBounds(0, 0, 630, 530);
		inputPanel.setBounds(0, 530, 630, 70);

		add(wordPanel);
		add(inputPanel);
		
		createWordThread = new CreateWordThread(word);
		downWordThread = new DownWordThread(word);
		noticeWordThread = new NoticeWordThread(word);
		
		
		textField.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JTextField w = (JTextField)(e.getSource());
				String uWord = w.getText(); // 사용자가 입력창에 입력한 단어
				for (int i=0; i < word.size(); i++) {
					String text = word.get(i).getText();
					if(text.equals(uWord)) { //입력된 단어와 동일한 단어라면
						System.out.println(uWord+" 정답!");
						wordPanel.remove(word.get(i)); // 패널에서 안보이도록 삭제
						word.remove(i); // Vector에서 삭제
						w.setText(null); // 입력칸 비우기
						opPanel.increase(user);
						break;
						}
					if(i==(word.size()-1)&& !text.equals(uWord)){
						System.out.println(uWord + " 오답!");
						// 점수 감소
						w.setText(null);
					}
					w.setText("");//못맞춰도 지우기
					}
			
				}
		});
	}

	class InputPanel extends JPanel {
		public InputPanel() {
			add(textField);
		}
	}

	class WordPanel extends JPanel {
		public WordPanel() {
			setLayout(null);
		}
	}
	
	// 단어 저장된 텍스트 파일 읽기
	public class Readfile {
		private Vector<String> w = new Vector<String>();
		String filepath = "text/words.txt";
		File file = new File(filepath);

		public Readfile() {
			readfile();
		}

		void readfile() {
			FileInputStream fstream;

			try {
				fstream = new FileInputStream("text/words.txt");
				DataInputStream in = new DataInputStream(fstream);
				BufferedReader reader = new BufferedReader(new InputStreamReader(in));
				String line;

				int i = 0;
				while ((line = reader.readLine()) != null) { //한줄씩 읽기
					w.add(line);
					i++;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		//Vector에 저장된 단어들 중 랜덤으로 불러오기
		public String get() {
			int index = (int) (Math.random() * w.size());
			return w.get(index);
		}
	}
	
	//스레드 시작하기
	public void start(User user) {
		this.user=user;
		createWordThread.start();
		downWordThread.start();
		noticeWordThread.start();
	}

	//단어 생성 스레드
	public class CreateWordThread extends Thread {
		private Vector<JLabel> word = new Vector<JLabel>();

		public CreateWordThread(Vector<JLabel> word) {
			this.word=word;
		}

		synchronized void createWord() {
			JLabel wordBox = new JLabel("");
			// 단어 불러오기
			Readfile r = new Readfile();
			String newWord = r.get();
			wordBox.setText(newWord); 

			wordBox.setOpaque(true); // setOpaque(true)로 설정해야 JLabel 색상 변경 가능
			wordBox.setHorizontalAlignment(JLabel.CENTER); // JLabel 글자 가운데맞춤 설정
			wordBox.setSize(100, 20);

			// x좌표 wordPanel 넓이 내에서 랜덤 설정
			int x = (int) (Math.random() * 530);

			wordBox.setLocation(x, 0);

			word.add(wordBox);
			wordPanel.add(wordBox);

		}

		@Override
		public void run() {
			while (true) {
				int createTime = 0; //레벨별로 생성 속도 조절
				switch (user.getLevel()) {
				case 1: createTime = 3000; break;
				case 2: createTime = 2000; break;
				case 3: createTime = 1500; break;
				case 4: createTime = 1000; break;
				case 5: createTime = 700; break;
				}

				createWord();
				wordPanel.repaint();
				try {
					sleep(createTime);
				} catch (InterruptedException e) {
					return;
				}
			}
		}
	}
	
	//단어 y축 조정하는 스레드
	public class DownWordThread extends Thread{
		private Vector<JLabel> word = null;
		
		public DownWordThread(Vector<JLabel> word) {
			this.word = word;
		}
		
		synchronized void downWord() {
			for (int i=0; i<word.size(); i++) {
				int x = word.get(i).getX();
				int y = word.get(i).getY();
				word.get(i).setLocation(x, y+3);
				wordPanel.repaint();
			}
		}
		
		@Override
		public void run() {
			 while (true){
				 int downTime = 0;
				 switch (user.getLevel()) { //레벨별로 떨어지는 속도 조절
					case 1: downTime = 400; break;
					case 2: downTime = 300; break;
					case 3: downTime = 200; break;
					case 4: downTime = 100; break;
					case 5: downTime = 60; break;
				 }
				 
				 downWord();
				 wordPanel.repaint();
				 try {
					 sleep(downTime);
					} catch (InterruptedException e) {
						return;
					}
			}
		}
	}
	
	//단어 떨어짐 확인
	public class NoticeWordThread extends Thread {
		
		private Vector<JLabel>word = null;
		
		public NoticeWordThread(Vector<JLabel>word) {
			this.word = word;
		}
		@Override
		public void run() {
			while(true) {
				try {
					sleep(1);
					for(int i=0; i<word.size(); i++) {
						// 바닥에 닿은 단어 구하기
						int position = ((JLabel)word.get(i)).getY(); //단어 y축
						if (wordPanel.getHeight()-30 < position) { //wordPanel의 하단과 닿는다면
							boolean over = opPanel.decreseHeart(user); //heart 감소
							if(over==true) { //진행중인 모든 스레드 중지
								createWordThread.interrupt();
								downWordThread.interrupt();
								noticeWordThread.interrupt();
							}
							wordPanel.remove(word.get(i)); // word패널에서 삭제
							word.remove(i);
						}
					}
				} catch (InterruptedException e) {
					return;
				}
			} 
		} 
	}

}
