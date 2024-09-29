package fileprocessing;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

public class MainWindow extends JFrame {
	//매개변수
	JButton jbSearch,jbNew,jbMod,jbIn,jbOut;											// 버튼 매개변수 선언
	String [] header = {"카테고리 No.(이름)","상품명","가격","수량","단위"};			// table 분류바 선언
	JTable table;																		// table 선언
	DefaultTableModel model;															// table 값 디폴트 값으로 선언
	JScrollPane scrollPane;

	NewWindow productInsert;
	InputWindow input;
	//생성자 초기화
	MainWindow(){
		super("재고 관리 프로그램");														// 창 제목
		// 버튼 초기화
		jbSearch	= new JButton("검색");												// 버튼 글씨 지정
		jbNew 		= new JButton("상품 등록");											// 버튼 글씨 지정
		jbMod 		= new JButton("상품 수정");											// 버튼 글씨 지정
		jbIn		= new JButton("입고");												// 버튼 글씨 지정
		jbOut		= new JButton("출고");												// 버튼 글씨 지정

		//table , scrollPane 초기화
		model		= new DefaultTableModel(header,0);									// table 분류바만 설정후 값은 디폴트 지정								
		table 		= new JTable(model);												// 디폴트값 테이블에 삽입
		scrollPane	= new JScrollPane(table);											// 스크롤페인에 테이블 삽입
		
		eventProc();
		loadExistingData();
		mainWindow();
		
	}

	// 메인 화면 메서드
	void mainWindow() {

		//레이아웃
		setLayout(new BorderLayout());													// 메인 레이아웃은 보더레이아웃

		// 패널만들기

		//south
		JPanel s_panle = new JPanel();													// 서쪽 보더 판넬 생성

		s_panle.setLayout(new FlowLayout(FlowLayout.LEFT,16,30));						// 서쪽 보더 판넬 레이아웃 지정(왼쪽정렬,가로 16 간격, 세로 30 간격)

		s_panle.add(jbNew);																// 등록 버튼 추가 및 설정
		jbNew.setPreferredSize(new Dimension(175,30));									// 버튼크기지정
		jbNew.setFont(new Font("고딕",Font.ITALIC,13));									// 버튼글씨지정		

		s_panle.add(jbMod);																// 수정 버튼 추가 및 설정
		jbMod.setPreferredSize(new Dimension(175,30));
		jbMod.setFont(new Font("고딕",Font.ITALIC,13));

		s_panle.add(jbIn);																// 입고 버튼 추가 및 설정
		jbIn.setPreferredSize(new Dimension(175,30));
		jbIn.setFont(new Font("고딕",Font.ITALIC,14));

		s_panle.add(jbOut);																// 출고 버튼 추가 및 설정
		jbOut.setPreferredSize(new Dimension(175,30));
		jbOut.setFont(new Font("고딕",Font.ITALIC,14));

		//center
		JPanel c_panel = new JPanel();													// 중앙 보더 판넬 생성

		c_panel.add(scrollPane);														// 스크롤페인 패널에 추가
		scrollPane.setPreferredSize(new Dimension(750,400));							// 스크롤페인 크기지정

		table.getColumn("상품명").setPreferredWidth(200);									// table column 사이즈 조절
		table.getColumn("카테고리 No.(이름)").setPreferredWidth(120);						// table column 사이즈 조절
		table.getColumn("가격").setPreferredWidth(120);									// table column 사이즈 조절


		//north
		JPanel n_panel = new JPanel();													// 북쪽 보더 판낼 생성

		n_panel.setLayout(new FlowLayout(FlowLayout.LEFT,17,20));						// 북쪽 판넬 레이아웃지정
		n_panel.add(new JTextField(20));												// 텍스트 필드 생성 및 추가

		n_panel.add(jbSearch);															// 서치 버튼 추가 및 설정
		jbSearch.setPreferredSize(new Dimension(40,19));									// 버튼 크기 지정
		jbSearch.setFont(new Font("고딕",Font.ITALIC,13));								// 버튼 글씨 지정
		jbSearch.setMargin(new Insets(0, 0, 0, 0));										// 마진 설정

		//배치하기
		add(n_panel,BorderLayout.NORTH);												// 북쪽 판넬 배치
		add(c_panel,BorderLayout.CENTER);												// 중앙 판넬 배치
		add(s_panle,BorderLayout.SOUTH);												// 서쪽 판넬 배치

		//창 생성
		setBounds(600, 150, 800, 600);													// 창 위치 및 사이즈
		setVisible(true);																// 창 켜짐
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);									// 엑스박스 눌를시 작동

	}

	// 이벤트 메서드
	void eventProc() {
		//메인 버튼 이벤트
		jbNew.addActionListener(new ActionListener() {
		    public void actionPerformed(ActionEvent e) {
		        if (productInsert == null || !productInsert.isShowing()) {
		            productInsert = new NewWindow(MainWindow.this);
		            productInsert.setListener(new NewWindow.NewWindowListener() {
		                public void onProductAdded(String category, String product, String price, String unit) {
		                    model.addRow(new Object[]{category, product, price, 0, unit}); // 새 제품 추가
		                }
		            });
		        } else {
		            productInsert.toFront();
		        }
		    }
		});
        jbIn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (input == null) {
                    input = new InputWindow();
                    input.inputwindow();
                }
                input.setVisible(true);
            }
        });
    }
	void loadExistingData() {
        try {
            List<String> lines = Files.readAllLines(Paths.get("products.txt"));
            for (String line : lines) {
                String[] parts = line.split(",");
                if (parts.length == 5) {
                    int categoryId = Integer.parseInt(parts[0]);
                    String category = parts[1];
                    String product = parts[2];
                    String price = parts[3];
                    String unit = parts[4];

                    model.addRow(new Object[]{categoryId+"."+category, product, price, 0, unit}); // 테이블에 행 추가
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}

