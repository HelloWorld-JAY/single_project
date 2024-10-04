package fileprocessing;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.regex.PatternSyntaxException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.RowFilter;
import javax.swing.SwingConstants;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableRowSorter;

public class MainWindow extends JFrame {
	//매개변수
	JButton jbNew,jbMod,jbIn,jbOut,jbDel;										// 버튼 매개변수 선언
	JTextField jtf;
	String [] header = {"카테고리 No.(이름)","상품명","가격","수량","단위"};						// table 헤더 스트링배열 선언
	JLabel jlSearch;
	JTable table;																		// jtable 클래스 변수 table 선언
	DefaultTableModel model;															// defaulttablemodel 클래스 변수 model 선언
	JScrollPane scrollPane;																// 스크롤페인 선언
	DefaultTableCellRenderer dtcr;
	
	TableRowSorter<DefaultTableModel> sorter;
	
	
	NewWindow productInsert;															//클래스 뉴윈도우 
	InputWindow input;
	//생성자 초기화
	MainWindow(){
		super("재고 관리 프로그램");														// 창 제목
		// 버튼 초기화
		jlSearch	= new JLabel("검색");
		// 버튼 글씨 지정
		jbNew 		= new JButton("상품 등록");											// 버튼 글씨 지정
		jbMod 		= new JButton("상품 수정");											// 버튼 글씨 지정
		jbDel		= new JButton("상품 삭제");
		jbIn		= new JButton("입고");												// 버튼 글씨 지정
		jbOut		= new JButton("출고");												// 버튼 글씨 지정

		jtf 		= new JTextField(20);

		//table , scrollPane 초기화
		
		model		= new DefaultTableModel(header,0) {
			public boolean isCellEditable(int rowIndex,int mColIndex) {
				return false;
			}
		};
		dtcr = new DefaultTableCellRenderer();
		
		table 		= new JTable(model);												// 디폴트값 테이블에 삽입
		scrollPane	= new JScrollPane(table);											// 스크롤페인에 테이블 삽입
		sorter =	 new TableRowSorter<>(model);
		
		
		loadExistingData();
		eventProc();
		mainWindow();

	}

	// 메인 화면 메서드
	void mainWindow() {

		//레이아웃
		setLayout(new BorderLayout());													// 메인 레이아웃은 보더레이아웃

		// 패널만들기

		//south
		JPanel s_Panel = new JPanel();													// 서쪽 보더 판넬 생성

		s_Panel.setLayout(new FlowLayout(FlowLayout.LEFT,17,30));						// 서쪽 보더 판넬 레이아웃 지정(왼쪽정렬,가로 16 간격, 세로 30 간격)

		s_Panel.add(jbNew);																// 등록 버튼 추가 및 설정
		jbNew.setPreferredSize(new Dimension(136,30));									// 버튼크기지정
		jbNew.setFont(new Font("고딕",Font.ITALIC,13));									// 버튼글씨지정		

		s_Panel.add(jbMod);																// 수정 버튼 추가 및 설정
		jbMod.setPreferredSize(new Dimension(136,30));
		jbMod.setFont(new Font("고딕",Font.ITALIC,13));

		s_Panel.add(jbDel);
		jbDel.setPreferredSize(new Dimension(136,30));
		jbDel.setFont(new Font("고딕",Font.ITALIC,13));

		s_Panel.add(jbIn);																// 입고 버튼 추가 및 설정
		jbIn.setPreferredSize(new Dimension(136,30));
		jbIn.setFont(new Font("고딕",Font.ITALIC,14));

		s_Panel.add(jbOut);																// 출고 버튼 추가 및 설정
		jbOut.setPreferredSize(new Dimension(136,30));
		jbOut.setFont(new Font("고딕",Font.ITALIC,14));

		//center
		JPanel c_panel = new JPanel();													// 중앙 보더 판넬 생성

		c_panel.add(scrollPane);														// 스크롤페인 패널에 추가
		scrollPane.setPreferredSize(new Dimension(750,400));							// 스크롤페인 크기지정
		
		dtcr.setHorizontalAlignment(SwingConstants.RIGHT);
		TableColumnModel tcm = table.getColumnModel();
		for(int i = 0; i < tcm.getColumnCount(); i++) {
			tcm.getColumn(i).setCellRenderer(dtcr);
		}
		
		table.getTableHeader().setReorderingAllowed(false);
		table.getTableHeader().setResizingAllowed(false);
		table.getColumn("상품명").setPreferredWidth(250);									// table column 사이즈 조절
		table.getColumn("카테고리 No.(이름)").setPreferredWidth(140);						// table column 사이즈 조절
		table.getColumn("가격").setPreferredWidth(140);									// table column 사이즈 조절
		//table.setAutoCreateRowSorter(true);
		
		table.setRowSorter(sorter);
		jtf.getDocument().addDocumentListener(new DocumentListener() {
			
			public void removeUpdate(DocumentEvent e) {
				search();
			}
			
			public void insertUpdate(DocumentEvent e) {
				search();
			}
			
			public void changedUpdate(DocumentEvent e) {
				search();
			}
		});

		//north
		JPanel n_panel = new JPanel();													// 북쪽 보더 판낼 생성

		n_panel.setLayout(new FlowLayout(FlowLayout.LEFT,30,15));						// 북쪽 판넬 레이아웃지정


		n_panel.add(jlSearch);															// 서치 버튼 추가 및 설정
		jlSearch.setFont(new Font("고딕",Font.BOLD,15));									// 버튼 글씨 지정

		n_panel.add(jtf);																// 텍스트 필드 생성 및 추가

		//배치하기
		add(n_panel,BorderLayout.NORTH);												// 북쪽 판넬 배치
		add(c_panel,BorderLayout.CENTER);												// 중앙 판넬 배치
		add(s_Panel,BorderLayout.SOUTH);												// 서쪽 판넬 배치

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
				loadExistingData();
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

		jbDel.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				
				int selectIndex = table.getSelectedRow();
				if(selectIndex != -1) {
					model.removeRow(selectIndex);
					
				}else JOptionPane.showMessageDialog(null, "삭제할 행을 선택해 주세요.");
			}
		});
	}
	void search() {
		String searchText = jtf.getText().trim();
		
		if(searchText.isEmpty()) {
			sorter.setRowFilter(null);
		}else {
			try {
				sorter.setRowFilter(RowFilter.regexFilter("(?i)" + searchText));
			}catch(PatternSyntaxException ex) {
				sorter.setRowFilter(null);
			}
		}
	}
	void loadExistingData() {
		try {
			List<String> lines = Files.readAllLines(Paths.get("products.txt"));
			for (String line : lines) {
				String[] parts = line.split("/");
				if (parts.length == 5) {
					int categoryId = Integer.parseInt(parts[0]);
					String category = parts[1];
					String product = parts[2];
					String price = parts[3];
					String unit = parts[4];

					// 테이블에 동일한 데이터가 있는지 확인
					boolean exists = false;
					for (int i = 0; i < model.getRowCount(); i++) {
						String existingCategory = model.getValueAt(i, 0).toString();
						String existingProduct = model.getValueAt(i, 1).toString();

						if (existingCategory.equals(categoryId + "." + category) && existingProduct.equals(product)) {
							exists = true; // 이미 존재하는 데이터
							break;
						}
					}

					// 중복되지 않으면 추가
					if (!exists) {
						model.addRow(new Object[]{categoryId + "." + category, product, price, 0, unit}); // 테이블에 행 추가
						sorter.modelStructureChanged();
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}

