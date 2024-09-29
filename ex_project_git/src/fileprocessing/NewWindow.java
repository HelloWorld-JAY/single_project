package fileprocessing;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JTree;

public class NewWindow extends JFrame{

	//매개변수
	JButton jbYes,jbNo;																	// 등록,취소 버튼
	JLabel 	jlCate,jlProduct,jlPrice,jlUnit;												// 상품등록창 라벨
	JTextField jtf1,jtf2,jtf3,jtf4;														// 상품등록 입력창

	HashMap<String, Integer> categoryMap ;
	int nextCategoryId = 1; 
	//상품등록창 생성자
	public NewWindow() {
		super("상품 등록창");	// 제목

		categoryMap = new HashMap<>();
		jbYes		= new JButton("등록");												// 각 변수 초기화 입력
		jbNo		= new JButton("취소");

		jlCate		= new JLabel("카테고리명");
		jlProduct		= new JLabel("      상품명");
		jlPrice		= new JLabel("          가격");
		jlUnit		= new JLabel("  단위/규격");
		jtf1		= new JTextField(16);
		jtf2		= new JTextField(16);
		jtf3		= new JTextField(16);
		jtf4		= new JTextField(16);
		newwindow();
		loadCategories();
		eventProc();																	// 버튼클릭시 이벤트 불러오기
	}

	//상품등록창 메서드
	void newwindow() {
		// 레이아웃 설정

		// 화면 구성	
		setLayout(new BorderLayout());													// 메인 화면구성은 보더 레이아웃

		// center
		JPanel c_Panel = new JPanel();													// 중앙 패널 화면구성 플로어레이아웃
		c_Panel.setLayout(new FlowLayout(FlowLayout.CENTER,40,20));	

		c_Panel.add(jlCate);															//레이블 및 텍스트필드 설정
		jlCate.setFont(new Font("고딕",Font.ITALIC,14));	
		c_Panel.add(jtf1);
		jtf1.setFont(new Font("고딕",Font.ITALIC,14));
		jtf1.setHorizontalAlignment(JTextField.RIGHT);

		c_Panel.add(jlProduct);															//레이블 및 텍스트필드 설정
		jlProduct.setFont(new Font("고딕",Font.ITALIC,14));
		c_Panel.add(jtf2);
		jtf2.setFont(new Font("고딕",Font.ITALIC,14));
		jtf2.setHorizontalAlignment(JTextField.RIGHT);

		c_Panel.add(jlPrice);															//레이블 및 텍스트필드 설정
		jlPrice.setFont(new Font("고딕",Font.ITALIC,14));
		c_Panel.add(jtf3);
		jtf3.setFont(new Font("고딕",Font.ITALIC,14));
		jtf3.setHorizontalAlignment(JTextField.RIGHT);

		c_Panel.add(jlUnit);															//레이블 및 텍스트필드 설정
		jlUnit.setFont(new Font("고딕",Font.ITALIC,14));	
		c_Panel.add(jtf4);
		jtf4.setFont(new Font("고딕",Font.ITALIC,14));
		jtf4.setHorizontalAlignment(JTextField.RIGHT);

		//south
		JPanel s_Panel = new JPanel();									
		s_Panel.setLayout(new FlowLayout(FlowLayout.CENTER,30,20));						// 남쪽 패널 화면구성 플로어 레이아웃

		s_Panel.add(jbYes);																// 버튼 설정
		jbYes.setPreferredSize(new Dimension(130,30));
		jbYes.setFont(new Font("고딕",Font.ITALIC,13));
		s_Panel.add(jbNo);																// 버튼 설정
		jbNo.setPreferredSize(new Dimension(130,30));
		jbNo.setFont(new Font("고딕",Font.ITALIC,13));

		// 화면 구성
		add(c_Panel,BorderLayout.CENTER);												// 화면에 패널 배치
		add(s_Panel,BorderLayout.SOUTH);

		// 창 생성
		setBounds(800, 300, 400, 300);													// 창 위치 및 사이즈
		setVisible(true);																// 창 켜짐
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);								// 엑스박스 누를시 창만 꺼짐
	}
	void eventProc() {			
		jbNo.addActionListener(new ActionListener() {									// 취소버튼 누를시 닫침

			public void actionPerformed(ActionEvent e) {								
				dispose();

			}
		});
		jbYes.addActionListener(new ActionListener() {


			public void actionPerformed(ActionEvent e) {

				String category = jtf1.getText();
				String product	= jtf2.getText();
				String price	= jtf3.getText();
				String unit		= jtf4.getText();

				int count = categoryMap.getOrDefault(category, 0);
				categoryMap.put(category, count + 1);
				saveToFile(count+"",category, product,price,unit);
				restField();

			}
		});
	}
	void restField() {
		jtf1.setText("");
		jtf2.setText("");
		jtf3.setText("");
		jtf4.setText("");
	}
	void saveToFile(String count,String category,String product,String price,String unit) {

		try(BufferedWriter writer = new BufferedWriter(new FileWriter("products.txt", true))){
			writer.write(String.format("%s,%s,%s,%s,%s\n",count,category,product,price,unit));
		}catch(IOException e) {
			e.printStackTrace();
		}
	}
	void loadCategories() {
		try {
			List<String> lines = Files.readAllLines(Paths.get("products.txt"));
			for(String line: lines) {
				String[] parts = line.split(",");
				if(parts.length >= 2) {
					try {
						int categoryId = Integer.parseInt(parts[0]);
						String category = parts[1];
						categoryMap.put(category, categoryId);
					}catch(NumberFormatException e) {
						System.err.println("카테고리ID 형식이 잘못되었습니다:" + parts[0])
					}
				}
			}
		}catch(IOException e) {
			e.printStackTrace();
		}
	}
}

