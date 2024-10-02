package fileprocessing;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

public class InputWindow extends JFrame {

	
	// 매개변수 지정
	JComboBox<String> jcCate;

	JLabel jlInfo;
	
	JTextField jtf;
	JButton jbSearch,jbYes,jbNo;
	
	String [] header = {"상품 No.","상품명","가격","수량","단위"};								// table 분류바 선언

	JTable table;									 									// table 선언
	DefaultTableModel model;															// table 값 디폴트 값으로 선언
	JScrollPane scrollPane;


	// 생성자 
	public InputWindow() {
		super("상품 입고창");

		jcCate = new JComboBox<>();

		jlInfo = new JLabel("카테고리를 선택하세요");

		jtf = new JTextField(16);
		
		jbSearch = new JButton("검색");
		jbYes = new JButton("확인");
		jbNo 	= new JButton("취소");
		//table , scrollPane 초기화
		model		= new DefaultTableModel(header,0);									// table 분류바만 설정후 값은 디폴트 지정								
		table 		= new JTable(model);												// 디폴트값 테이블에 삽입
		scrollPane	= new JScrollPane(table);	
		eventProc();// 스크롤페인에 테이블 삽입
	}

	//입고창 매서드
	void inputwindow() {
		// 메인 레이아웃지정
		setLayout(new BorderLayout());

		// 패널지정
		JPanel n_Panel = new JPanel();
		n_Panel.setLayout(new FlowLayout(FlowLayout.LEFT,18,10));
		n_Panel.add(jcCate);
		jcCate.setPreferredSize(new Dimension(300,20));
		n_Panel.add(jlInfo);
		

		JPanel c_Panel = new JPanel();
		GridBagConstraints gbc = new GridBagConstraints();
		c_Panel.setLayout(new GridBagLayout());
		c_Panel.setBorder(new EmptyBorder(0, 18, 0, 18));								// 위, 왼쪽, 아래, 오른쪽 여백 설정
	    gbc.gridx = 0; 																	// 첫 번째 열
	    gbc.gridy = 0;  																// 첫 번째 행
	    gbc.gridwidth = 2; 																// 하나의 열을 차지
	    gbc.fill = GridBagConstraints.BOTH;  											// 공간을 모두 차지하도록 설정
        gbc.weightx = 1.0;  										 					// x 방향으로 추가 공간을 차지하도록 설정
        gbc.weighty = 1.0; 										 						// y 방향으로 추가 공간을 차지하도록 설정
		c_Panel.add(scrollPane,gbc);
		gbc.gridx = 0; 																	// 첫 번째 열
	    gbc.gridy = 1;  																// 두 번째 행
	    gbc.gridwidth = 1; 	
	    gbc.fill = GridBagConstraints.HORIZONTAL; 										// 가로 방향으로 공간을 차지하도록 설정
        gbc.weightx = 0.94; 		 														// x 방향으로 추가 공간을 차지하도록 설정
        gbc.weighty = 0.1; 										 						// y 방향으로 추가 공간을 차지하도록 설정// 하나의 열을 차지
        gbc.insets = new Insets(5, -1, 5, 2);  // 여백 설정 (위, 왼쪽, 아래, 오른쪽)
        c_Panel.add(jtf,gbc);
		jtf.setPreferredSize(new Dimension(100, 19));  // 텍스트 필드 크기 설정
	    jtf.setFont(new Font("고딕", Font.PLAIN, 14));  // 폰트 크기 설정
	    
		gbc.gridx = 1; 																	// 두 번째 열
		gbc.weightx = 0.06; // x 방향으로 추가 공간을 차지하도록 설정
		c_Panel.add(jbSearch,gbc);
		jbSearch.setFont(new Font("고딕",Font.ITALIC,13));
		jbSearch.setPreferredSize(new Dimension(80, 19));  // 검색 버튼 크기 설정
	    jbSearch.setMargin(new Insets(0, 0, 0, 0));
	    
		scrollPane.setPreferredSize(new Dimension(450,200));							// 스크롤페인 크기지정
		table.getColumn("상품명").setPreferredWidth(230);
		table.getColumn("가격").setPreferredWidth(180); 
		
		
		JPanel s_Panel = new JPanel();
		s_Panel.setLayout(new FlowLayout(FlowLayout.CENTER,50,10));
		
		
	    
		s_Panel.add(jbYes);
		jbYes.setPreferredSize(new Dimension(120,30));
		jbYes.setFont(new Font("고딕",Font.ITALIC,14));
		s_Panel.add(jbNo);
		jbNo.setFont(new Font("고딕",Font.ITALIC,14));
		jbNo.setPreferredSize(new Dimension(120,30));
		
		//패널 배치
		add(n_Panel, BorderLayout.NORTH);
		add(c_Panel, BorderLayout.CENTER);
		add(s_Panel, BorderLayout.SOUTH);
		// 창 생성
		setBounds(800, 300, 500, 400);													// 창 위치 및 사이즈
		setVisible(true);																// 창 켜짐
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);								// 엑스박스 누를시 창만 꺼짐


	}
	void eventProc() {			
		jbNo.addActionListener(new ActionListener() {									// 취소버튼 누를시 닫침
			
			public void actionPerformed(ActionEvent e) {								
				dispose();

			}
		});
	}
}
