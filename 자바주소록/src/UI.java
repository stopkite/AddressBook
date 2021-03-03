import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableModel;

public class UI {
	@SuppressWarnings("unused")
	public static void main(String[] args) {

		try {

			AddressBook connection = new AddressBook();// AdressBook 객체 생성

			GUI gui = new GUI(connection);// DB 호출

		} catch (ClassNotFoundException cnfe) {

			cnfe.getStackTrace();

		} catch (SQLException se) {

			se.getStackTrace();
		}
	}
}

class GUI extends JFrame {

	@SuppressWarnings("rawtypes")
	public GUI(AddressBook connection) {

		// 배경
		JFrame frame = new JFrame();
		frame.setTitle("주소록 프로그램");
		frame.setPreferredSize(new Dimension(800, 680));
		frame.getContentPane().setLayout(null);
		Container contentPane = frame.getContentPane();

		// 타이틀
		JLabel titleLabel = new JLabel("주소록");
		titleLabel.setBounds(350, 0, 100, 70);
		contentPane.add(titleLabel);
		Font titleFont = new Font("조선일보명조", Font.BOLD, 30);
		titleLabel.setFont(titleFont);

		// 검색 - 콤보박스
		String selectCombo[] = { "이름" };
		JComboBox comboBox = new JComboBox<String>(selectCombo);
		comboBox.setBounds(40, 65, 142, 30);
		contentPane.add(comboBox);

		// 검색 - 버튼
		ImageIcon loupeIcon = new ImageIcon("C:\\Users\\Lenovo\\eclipse-workspace\\주소록\\src\\loupe.png");
		Image loupeImg = loupeIcon.getImage();
		Image sizingImg = loupeImg.getScaledInstance(30, 25, Image.SCALE_SMOOTH);
		loupeIcon.setImage(sizingImg);
		JButton searchBtn = new JButton(loupeIcon);
		searchBtn.setBounds(700, 65, 40, 30);
		contentPane.add(searchBtn);

		// 검색 - 텍스트필드
		JTextField searchTxt = new JTextField();
		searchTxt.setBounds(182, 65, 520, 32);
		contentPane.add(searchTxt);
		searchTxt.setColumns(20);

		// 테이블 패널
		JPanel tablePanel = new JPanel();
		tablePanel.setLayout(null);
		tablePanel.setBounds(20, 40, 800, 540);
		contentPane.add(tablePanel);

		// 표 생성
		String colNames[] = { "이름", "전화번호", "집주소", "이메일", "성별" };// 행 데이터
		Object data[][] = {};// 열 데이터
		DefaultTableModel model = new DefaultTableModel(data, colNames);
		JTable table = new JTable(model);

		try {
			int c = connection.getCount();// 주소록에 저장된 사람 수

			for (int i = 0; i < c; i++) {// 주소록에 저장된 열 데이터를 받아온다
				Object[] row;
				try {
					row = new Object[] { connection.getPerson(i).getName(), connection.getPerson(i).getPhoneNum(),
							connection.getPerson(i).getAddress(), connection.getPerson(i).getEmail(),
							connection.getPerson(i).getSex() };
					model.addRow(row);
				} catch (Exception e1) { // getPerson() 익셉션
					e1.printStackTrace();
				}
			}

		} catch (SQLException se) {// getCount() 익셉션
			se.printStackTrace();
		}

		// 스크롤
		JScrollPane scrollPane = new JScrollPane(table);
		LineBorder JBorder = new LineBorder(Color.gray);
		scrollPane.setBounds(20, 60, 700, 450);
		scrollPane.setBorder(JBorder);
		tablePanel.add(scrollPane);

		// 추가
		JButton addBtn = new JButton("추가");
		addBtn.setBounds(40, 580, 100, 50);
		contentPane.add(addBtn);
		addBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				addWindow showAddWin = new addWindow(connection);
			}
		});

		// 수정
		JButton editBtn = new JButton("수정");
		editBtn.setBounds(150, 580, 100, 50);
		contentPane.add(editBtn);
		editBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				editWindow showEditWin = new editWindow(connection);
			}
		});

		// 삭제
		JButton delBtn = new JButton("삭제");
		delBtn.setBounds(260, 580, 100, 50);
		contentPane.add(delBtn);
		delBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				delWindow showDelWin = new delWindow(connection);
			}
		});

		// 전체조회
		JButton showAllBtn = new JButton("새로고침");
		showAllBtn.setBounds(370, 580, 100, 50);
		contentPane.add(showAllBtn);
		showAllBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				try {
					model.setNumRows(0);
					int cnt = connection.getCount();

					if (cnt == 0) {
						System.out.println("주소록이 비었습니다.");

					} else {
						for (int i = 0; i < cnt; i++) {
							Object[] row = { connection.getPerson(i).getName(), connection.getPerson(i).getPhoneNum(),
									connection.getPerson(i).getAddress(), connection.getPerson(i).getEmail(),
									connection.getPerson(i).getSex() };
							model.addRow(row);
						}
					}

				} catch (SQLException se) {

				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});

		// 종료
		JButton exitBtn = new JButton("종료");
		exitBtn.setBounds(640, 580, 100, 50);
		contentPane.add(exitBtn);
		exitBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				try {// DB 메소드 호출
					connection.closeDB();
				} catch (Exception e1) {
					e1.printStackTrace();
				}

				System.exit(0); // 프로그램 종료
			}
		});

		// 검색 - 기능구현
		searchBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				searchBtn.setEnabled(true);

				try {
					int perNum = connection.getCount();
					String name = searchTxt.getText();

					try {

						for (int i = 0; i <= perNum; i++) {
							Person perData = connection.searchNameWith(name, i);
							String searchName[] = new String[6];

							searchName[0] = perData.getName();
							searchName[1] = perData.getPhoneNum();
							searchName[2] = perData.getAddress();
							searchName[3] = perData.getEmail();
							searchName[4] = perData.getSex();

							model.setNumRows(i); // 초기화
							model.addRow(searchName);

						}

					} catch (Exception e1) { // searchNameWith() 익셉션
						e1.printStackTrace();
					}

				} catch (SQLException e2) {// getCount() 익셉션
					e2.printStackTrace();
				}
			}// searchBtn
		});

		// 프로그램 종료
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		// 프레임 설정
		frame.pack();
		frame.setVisible(true);

	}

}

class addWindow extends JFrame {

	public addWindow(AddressBook connection) {

		JFrame frame = new JFrame();
		frame.setTitle("주소록 프로그램");
		frame.setBounds(600, 170, 500, 560);
		frame.getContentPane().setLayout(null);
		Container contentPane = frame.getContentPane();

		JLabel titleLabel = new JLabel("추가");
		titleLabel.setBounds(209, 13, 81, 64);
		contentPane.add(titleLabel);
		frame.setPreferredSize(new Dimension(500, 200));
		Font titleFont = new Font("조선일보명조", Font.PLAIN, 40);
		titleLabel.setFont(titleFont);

		JPanel panel = new JPanel();
		panel.setBounds(47, 90, 390, 330);
		panel.setBorder(new LineBorder(Color.black, 1));
		contentPane.add(panel);
		panel.setLayout(null);

		// 폰트 설정(라벨 및 텍스트 필드)
		Font winFont = new Font("조선일보명조", Font.PLAIN, 26);

		// 이름 - 라벨
		JLabel nameLabel = new JLabel("이름");
		nameLabel.setFont(winFont);
		nameLabel.setBounds(20, 0, 81, 64);
		panel.add(nameLabel);

		// 이름 - 텍스트필드
		JTextField nameTxt = new JTextField();
		nameTxt.setBounds(109, 13, 270, 32);
		nameTxt.setFont(winFont);
		panel.add(nameTxt);
		nameTxt.setColumns(20);

		// 번호 - 라벨
		JLabel numLabel = new JLabel("번호");
		numLabel.setFont(winFont);
		numLabel.setBounds(20, 65, 81, 64);
		panel.add(numLabel);

		// 번호 - 텍스트필드
		JTextField numTxt = new JTextField();
		numTxt.setColumns(20);
		numTxt.setBounds(109, 79, 270, 32);
		numTxt.setFont(winFont);
		panel.add(numTxt);

		// 집주소 - 라벨
		JLabel adressLabel = new JLabel("집주소");
		adressLabel.setFont(winFont);
		adressLabel.setBounds(12, 136, 81, 64);
		panel.add(adressLabel);

		// 집주소 - 텍스트필드
		JTextField adressTxt = new JTextField();
		adressTxt.setColumns(20);
		adressTxt.setFont(winFont);
		adressTxt.setBounds(109, 150, 270, 32);
		panel.add(adressTxt);

		// 이메일 - 라벨
		JLabel emailLabel = new JLabel("이메일");
		emailLabel.setFont(winFont);
		emailLabel.setBounds(12, 196, 81, 64);
		panel.add(emailLabel);

		// 이메일 - 텍스트필드
		JTextField emailTxt = new JTextField();
		emailTxt.setColumns(20);
		emailTxt.setFont(winFont);
		emailTxt.setBounds(109, 219, 270, 32);
		panel.add(emailTxt);

		// 성별 - 라벨
		JLabel sexLabel = new JLabel("성별");
		sexLabel.setFont(new Font("조선일보명조", Font.PLAIN, 28));
		sexLabel.setBounds(20, 260, 81, 64);
		panel.add(sexLabel);

		// 성별 - 버튼(남)
		JRadioButton boyRtn = new JRadioButton("남");
		boyRtn.setBounds(122, 280, 53, 27);
		boyRtn.setFont(winFont);
		panel.add(boyRtn);

		// 성별 - 버튼(여)
		JRadioButton girlRtn = new JRadioButton("여");
		girlRtn.setBounds(243, 280, 53, 27);
		girlRtn.setFont(winFont);
		panel.add(girlRtn);

		// 확인
		JButton okBtn = new JButton("확인");
		okBtn.setBounds(110, 445, 105, 40);
		okBtn.setFont(winFont);
		contentPane.add(okBtn);

		// 취소
		JButton cancelBtn = new JButton("취소");
		cancelBtn.setBounds(270, 445, 105, 40);
		cancelBtn.setFont(winFont);
		contentPane.add(cancelBtn);
		cancelBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				frame.dispose();
			}
		});

		okBtn.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				String arr[] = new String[5];
				arr[0] = nameTxt.getText();
				arr[1] = numTxt.getText();
				arr[2] = adressTxt.getText();
				arr[3] = emailTxt.getText();

				if (girlRtn.isSelected())// 여자 버튼 체크
					arr[4] = "여";
				else // 남자 버튼 체크
					arr[4] = "남";

				if (arr[0].equals("")) // 이름 누락
				{
					JOptionPane.showMessageDialog(null, "필수 입력 사항 누락!(이름 입력란을 다시 확인해주세요)", "Error Message!",
							JOptionPane.PLAIN_MESSAGE);

				} else if (arr[1].equals("")) // 전화번호 누락
				{
					JOptionPane.showMessageDialog(null, "필수 입력 사항 누락!(전화번호 입력란을 다시 확인해주세요)", "Error Message!",
							JOptionPane.PLAIN_MESSAGE);

				} else // 중복 데이터 확인
					try {
						if (connection.checkName(arr[1]) == true) { // 동명이인 확인
							JOptionPane.showMessageDialog(null, "이미 등록된 이름입니다.\n 이름 뒤에 숫자를 붙여 지정하세요.(ex.정지연1,정지연2)",
									"Error Message!", JOptionPane.PLAIN_MESSAGE);

						} else if (connection.checkPhoneNum(arr[2]) == true) { // 만약 등록된 전화번호일 경우 메뉴로 돌아가게 함
							JOptionPane.showMessageDialog(null, "이미 등록된 전화번호입니다.", "Error Message!",
									JOptionPane.PLAIN_MESSAGE);

						} else if (girlRtn.isSelected() && boyRtn.isSelected()) // 남자 여자 모두 체크 되어있을 때
						{
							JOptionPane.showMessageDialog(null, "성별 중복 체크됨", "Error Message!",

									JOptionPane.PLAIN_MESSAGE);

						} else // 이상 없음 (추가 가능)
						{
							// model.addRow(arr); // 열을 추가한다

							try {// db에 정보추가
								connection.add(new Person(arr[0], arr[1], arr[2], arr[3], arr[4])); // 주소록 등록 메소드
								JOptionPane.showMessageDialog(null, "등록되었습니다! 새로고침을 해주세요.", "안내",
										JOptionPane.PLAIN_MESSAGE);
								frame.dispose();
							} catch (Exception e2) { // add 메소드 익셉션
								// JOptionPane.showMessageDialog(null, "등록오류.", "안내",
								// JOptionPane.PLAIN_MESSAGE);
							}

						}
					} catch (Exception e1) {// checkName & checkPhoneNum 익셉션
						e1.printStackTrace();
					}
			}

		});
		// 프레임 설정
		frame.setVisible(true);

	}
}

class editWindow {

	public editWindow(AddressBook connection) {
		JFrame frame = new JFrame();
		frame.setTitle("주소록 프로그램");
		frame.setBounds(600, 150, 501, 314);
		frame.getContentPane().setLayout(null);
		Container contentPane = frame.getContentPane();

		JLabel titleLabel = new JLabel("수정");
		titleLabel.setBounds(205, 13, 81, 64);
		contentPane.add(titleLabel);
		frame.setPreferredSize(new Dimension(500, 200));
		Font titleFont = new Font("조선일보명조", Font.PLAIN, 40);
		titleLabel.setFont(titleFont);

		// 폰트 설정(라벨 및 텍스트 필드)
		Font winFont = new Font("조선일보명조", Font.PLAIN, 26);

		// 검색 - 콤보박스
		String selectCombo[] = { "이름" };
		JComboBox comboBox = new JComboBox<String>(selectCombo);
		comboBox.setBounds(50, 99, 113, 32);
		comboBox.setFont(winFont);
		contentPane.add(comboBox);

		// 검색 - 텍스트필드
		JTextField searchTxt = new JTextField();
		searchTxt.setBounds(162, 99, 253, 32);
		contentPane.add(searchTxt);
		searchTxt.setFont(winFont);
		searchTxt.setColumns(20);

		// 안내 라벨
		JLabel infoLabel = new JLabel("수정하실 주소록의 정보를 입력하세요");
		infoLabel.setBounds(75, 150, 340, 32);
		infoLabel.setFont(new Font("조선일보명조", Font.PLAIN, 20));
		contentPane.add(infoLabel);

		// 확인
		JButton okBtn_1 = new JButton("확인");
		okBtn_1.setBounds(90, 200, 105, 35);
		okBtn_1.setFont(winFont);
		contentPane.add(okBtn_1);
		okBtn_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String name = searchTxt.getText();
				try {
					if (connection.checkName(name) == false || name != null && ("".equals(name))) {// 입력을 했으나, 일치하는 이름이
																									// 없을 때
						JOptionPane.showMessageDialog(null, "등록된 이름이 없습니다.", "Error Message!",
								JOptionPane.PLAIN_MESSAGE);
						frame.dispose();
					} else {// 일치하는 이름 찾음 => 수정가능! (이상 없음)

						JFrame frame = new JFrame();
						frame.setVisible(true);
						frame.setTitle("주소록 프로그램");
						frame.setBounds(600, 170, 500, 560);
						frame.getContentPane().setLayout(null);
						Container contentPane = frame.getContentPane();

						JLabel titleLabel = new JLabel("수정");
						titleLabel.setBounds(209, 13, 81, 64);
						contentPane.add(titleLabel);
						frame.setPreferredSize(new Dimension(500, 200));
						Font titleFont = new Font("조선일보명조", Font.PLAIN, 40);
						titleLabel.setFont(titleFont);

						JPanel panel = new JPanel();
						panel.setBounds(47, 90, 390, 330);
						panel.setBorder(new LineBorder(Color.black, 1));
						contentPane.add(panel);
						panel.setLayout(null);

						// 폰트 설정(라벨 및 텍스트 필드)
						Font winFont = new Font("조선일보명조", Font.PLAIN, 26);

						// 이름 - 라벨
						JLabel nameLabel = new JLabel("이름");
						nameLabel.setFont(winFont);
						nameLabel.setBounds(20, 0, 81, 64);
						panel.add(nameLabel);

						// 이름 - 텍스트필드
						JTextField nameTxt = new JTextField();
						nameTxt.setBounds(109, 13, 270, 32);
						nameTxt.setFont(winFont);
						panel.add(nameTxt);
						nameTxt.setColumns(20);

						// 번호 - 라벨
						JLabel numLabel = new JLabel("번호");
						numLabel.setFont(winFont);
						numLabel.setBounds(20, 65, 81, 64);
						panel.add(numLabel);

						// 번호 - 텍스트필드
						JTextField numTxt = new JTextField();
						numTxt.setColumns(20);
						numTxt.setBounds(109, 79, 270, 32);
						numTxt.setFont(winFont);
						panel.add(numTxt);

						// 집주소 - 라벨
						JLabel adressLabel = new JLabel("집주소");
						adressLabel.setFont(winFont);
						adressLabel.setBounds(12, 136, 81, 64);
						panel.add(adressLabel);

						// 집주소 - 텍스트필드
						JTextField adressTxt = new JTextField();
						adressTxt.setColumns(20);
						adressTxt.setFont(winFont);
						adressTxt.setBounds(109, 150, 270, 32);
						panel.add(adressTxt);

						// 이메일 - 라벨
						JLabel emailLabel = new JLabel("이메일");
						emailLabel.setFont(winFont);
						emailLabel.setBounds(12, 196, 81, 64);
						panel.add(emailLabel);

						// 이메일 - 텍스트필드
						JTextField emailTxt = new JTextField();
						emailTxt.setColumns(20);
						emailTxt.setFont(winFont);
						emailTxt.setBounds(109, 219, 270, 32);
						panel.add(emailTxt);

						// 성별 - 라벨
						JLabel sexLabel = new JLabel("성별");
						sexLabel.setFont(new Font("조선일보명조", Font.PLAIN, 28));
						sexLabel.setBounds(20, 260, 81, 64);
						panel.add(sexLabel);

						// 성별 - 버튼(남)
						JRadioButton boyRtn = new JRadioButton("남");
						boyRtn.setBounds(122, 280, 53, 27);
						boyRtn.setFont(winFont);
						panel.add(boyRtn);

						// 성별 - 버튼(여)
						JRadioButton girlRtn = new JRadioButton("여");
						girlRtn.setBounds(243, 280, 53, 27);
						girlRtn.setFont(winFont);
						panel.add(girlRtn);

						// 확인
						JButton okBtn_2 = new JButton("확인");
						okBtn_2.setBounds(110, 445, 105, 40);
						okBtn_2.setFont(winFont);
						contentPane.add(okBtn_2);
						okBtn_2.addActionListener(new ActionListener() {
							public void actionPerformed(ActionEvent e) {

								String modifyName = nameTxt.getText();
								String modifyAddress = adressTxt.getText();
								String modifyEmail = emailTxt.getText();
								String modifySex;
								String modifyPhoneNum = numTxt.getText();

								if (girlRtn.isSelected())// 여자 버튼 체크
									modifySex = "여";
								else // 남자 버튼 체크
									modifySex = "남";

								if (girlRtn.isSelected() & boyRtn.isSelected()) // 남자 여자 모두 체크 되어있을 때
									JOptionPane.showMessageDialog(null, "성별 중복 체크됨", "Error Message!",

											JOptionPane.PLAIN_MESSAGE);

								Person p = new Person(modifyName, modifyPhoneNum, modifyAddress, modifyEmail,
										modifySex);

								try {
									connection.modify(name, p);

									JOptionPane.showMessageDialog(null, "수정이 완료되었습니다!.새로고침바람", "Error Message!",

											JOptionPane.PLAIN_MESSAGE);
									frame.dispose();

								} catch (Exception e1) {
									// TODO Auto-generated catch block
									e1.printStackTrace();
								} // 주소록 수정
							}
						});

						// 취소
						JButton cancelBtn_2 = new JButton("취소");
						cancelBtn_2.setBounds(270, 445, 105, 40);
						cancelBtn_2.setFont(winFont);
						contentPane.add(cancelBtn_2);
						cancelBtn_2.addActionListener(new ActionListener() {
							public void actionPerformed(ActionEvent e) {
								frame.dispose();
							}
						});

					}

				} catch (Exception e2) {
				}
			}
		});

		// 취소
		JButton cancelBtn_1 = new JButton("취소");
		cancelBtn_1.setBounds(302, 200, 105, 35);
		cancelBtn_1.setFont(winFont);
		contentPane.add(cancelBtn_1);
		cancelBtn_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				frame.dispose();
			}
		});

		// 프레임 설정
		frame.setVisible(true);
	}
}

class delWindow {
	public delWindow(AddressBook connection) {
		JFrame frame = new JFrame();
		frame.setTitle("주소록 프로그램");
		frame.setBounds(600, 150, 501, 314);
		frame.getContentPane().setLayout(null);
		Container contentPane = frame.getContentPane();

		JLabel titleLabel = new JLabel("삭제");
		titleLabel.setBounds(205, 13, 81, 64);
		contentPane.add(titleLabel);
		frame.setPreferredSize(new Dimension(500, 200));
		Font titleFont = new Font("조선일보명조", Font.PLAIN, 40);
		titleLabel.setFont(titleFont);

		// 폰트 설정(라벨 및 텍스트 필드)
		Font winFont = new Font("조선일보명조", Font.PLAIN, 26);

		// 검색 - 콤보박스
		String selectCombo[] = { "이름" };
		JComboBox comboBox = new JComboBox<String>(selectCombo);
		comboBox.setBounds(50, 99, 113, 32);
		comboBox.setFont(winFont);
		contentPane.add(comboBox);

		// 검색 - 텍스트필드
		JTextField searchTxt = new JTextField();
		searchTxt.setBounds(162, 99, 253, 32);
		contentPane.add(searchTxt);
		searchTxt.setFont(winFont);
		searchTxt.setColumns(20);

		// 안내 라벨
		JLabel infoLabel = new JLabel("삭제할 주소록의 정보를 입력하세요");
		infoLabel.setBounds(75, 150, 340, 32);
		infoLabel.setFont(new Font("조선일보명조", Font.PLAIN, 20));
		contentPane.add(infoLabel);

		// 확인
		JButton okBtn = new JButton("확인");
		okBtn.setBounds(90, 200, 105, 35);
		okBtn.setFont(winFont);
		contentPane.add(okBtn);
		okBtn.addActionListener(new ActionListener() {// 등록의 입력 버튼 이벤트
			public void actionPerformed(ActionEvent e) {
				String name = searchTxt.getText();
				try {
					if (connection.getCount() == 0) {
						JOptionPane.showMessageDialog(null, "등록된 주소록이 없습니다.", "Error Message!",
								JOptionPane.PLAIN_MESSAGE);
					}
				} catch (SQLException e1) {
					e1.printStackTrace();
				}

				try {
					for (int i = 0; i < connection.getCount(); i++) {
						if (connection.getPerson(i).getName().equals(name) == true) {
							JOptionPane.showMessageDialog(null, "해당 주소록이 삭제되었습니다! 새로고침을 해주세요.", "안내",
									JOptionPane.PLAIN_MESSAGE);
							frame.dispose();
							

						} else if (name.equals("") && (i == connection.getCount())) {
							JOptionPane.showMessageDialog(null, "입력된 이름이 없습니다.", "Error Message!",
									JOptionPane.PLAIN_MESSAGE);
							frame.dispose();

						} else if (connection.getPerson(i).getName().equals(name) == false && (i == connection.getCount())) {
							JOptionPane.showMessageDialog(null, "등록된 이름이 없습니다.", "Error Message!",
									JOptionPane.PLAIN_MESSAGE);
							frame.dispose();
						}

					}
				} catch (Exception e2) {

				}
			}
		});

		// 취소
		JButton cancelBtn = new JButton("취소");
		cancelBtn.setBounds(302, 200, 105, 35);
		cancelBtn.setFont(winFont);
		contentPane.add(cancelBtn);
		cancelBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				frame.dispose();
			}
		});

		// 프레임 설정
		frame.setVisible(true);
	}
}
