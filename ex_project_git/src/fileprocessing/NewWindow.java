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
import java.nio.file.Paths;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

public class NewWindow extends JDialog{

	//매개변수
	JButton jbYes,jbNo;																		// 등록,취소 버튼
	JLabel 	jlCate,jlProduct,jlPrice,jlUnit;												// 상품등록창 라벨
	JTextField jtf1,jtf2,jtf3,jtf4;															// 상품등록 입력창

	HashMap<String, Integer> categoryMap ;
	int nextCategoryId = 1; 
	private NewWindowListener listener;
	DecimalFormat formatter;
	
	//상품등록창 생성자
	public NewWindow(JFrame owner) {
		super(owner,"상품 등록창", true);	// 제목


		categoryMap = new HashMap<>();
		jbYes		= new JButton("등록");													// 각 변수 초기화 입력
		jbNo		= new JButton("취소");

		jlCate		= new JLabel("카테고리명");
		jlProduct		= new JLabel("       상품명");
		jlPrice		= new JLabel("          가격");
		jlUnit		= new JLabel("  단위/규격");
		jtf1		= new JTextField(16);
		jtf2		= new JTextField(16);
		jtf3		= new JTextField(16);
		jtf4		= new JTextField(16);
		
		formatter = new DecimalFormat("###,###");
		
		loadCategories();
		eventProc();
		newWindow();

																						// 버튼클릭시 이벤트 불러오기
		 
	}
	public interface NewWindowListener {
        void onProductAdded(String category, String product, String price, String unit);
    }
	//	상품등록창 메서드
	void newWindow() {
		// 레이아웃 설정
		// 화면 구성	
		setLayout(new BorderLayout());													// 메인 화면구성은 보더 레이아웃

		// center
		JPanel c_Panel = new JPanel();													// 중앙 패널 화면구성 플로어레이아웃
		c_Panel.setLayout(new FlowLayout(FlowLayout.CENTER,40,20));	

		addLabelAndField(c_Panel, jlCate, jtf1);
		addLabelAndField(c_Panel, jlProduct, jtf2);
		addLabelAndField(c_Panel, jlPrice, jtf3);
		addLabelAndField(c_Panel, jlUnit, jtf4);

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
		setModal(true);
		setVisible(true);																// 창 켜짐
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);								// 엑스박스 누를시 창만 꺼짐
	}

	private void addLabelAndField(JPanel panel, JLabel label, JTextField textField) {
		label.setFont(new Font("고딕", Font.ITALIC, 14)); 
		panel.add(label); 
		textField.setFont(new Font("고딕", Font.ITALIC, 14)); 
		textField.setHorizontalAlignment(JTextField.RIGHT); 
		panel.add(textField);
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
				String price	= formatter.format(Integer.parseInt(jtf3.getText()));
				String unit		= jtf4.getText();


				int categoryId;
				if(!categoryMap.containsKey(category)) {
					categoryId = nextCategoryId++;
					categoryMap.put(category,categoryId);
				}else {
					categoryId = categoryMap.get(category);
				}
				saveToFile(categoryId,category, product,price,unit);
				MainWindow win = new MainWindow();
				restField();
				
				if (listener != null) {
	                SwingUtilities.invokeLater(() -> {
	                    listener.onProductAdded(category, product, price, unit); // EDT에서 호출
	                });
	            }
				dispose();

			}
		});
		
		
	}
	void restField() {
		jtf1.setText("");
		jtf2.setText("");
		jtf3.setText("");
		jtf4.setText("");
	}
	void saveToFile(int count,String category,String product,String price,String unit) {

		try(BufferedWriter writer = new BufferedWriter(new FileWriter("products.txt", true))){
			writer.write(String.format("%d/%s/%s/%s/%s\n",count,category,product,price,unit));
			writer.flush();
		}catch(IOException e) {
			e.printStackTrace();
			System.out.println("파일 쓰기 오류: "+ e.getMessage());
		}
	}
	void loadCategories() {
		try {
			if(Files.exists(Paths.get("products.txt")) && Files.size(Paths.get("products.txt")) > 0) {
				List<String> lines = Files.readAllLines(Paths.get("products.txt"));
				for(String line: lines) {
					String[] parts = line.split("/");
					if(parts.length >= 2) {
						try {
							int categoryId = Integer.parseInt(parts[0]);
							String category = parts[1];
							if(!categoryMap.containsKey(category)) {
								categoryMap.put(category, categoryId);
								nextCategoryId = Math.max(nextCategoryId, categoryId + 1);
							}
						}catch(NumberFormatException e) {
							System.err.println("카테고리ID 형식이 잘못되었습니다:" + parts[0]);
						}
					}
				}
			}else {
				System.err.println("products.txt 파일이 비어 있거나 존재하지 않습니다.");
			}
		}catch(IOException e) {
			e.printStackTrace();
		}
		
	}
	 public void setListener(NewWindowListener listener) {
	        this.listener = listener; // 리스너 설정
	    }
}

