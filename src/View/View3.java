package View;

import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Label;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;

public class View3 extends JFrame{
	public View3() {
		
		setSize(1250, 550);
		setTitle("Tool mã hoá");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
		setLayout(new BorderLayout());
		
		
		JTabbedPane tabbedPane = new JTabbedPane();
//		tab1
		JPanel tabEncript = new JPanel();
		tabEncript.setLayout(new BorderLayout(10,10));
		tabEncript.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
		
		JPanel panelTop = new JPanel(new GridLayout(2, 2, 15, 5));
		JLabel lbMethod = new JLabel("Phương pháp mã hoá");
		JLabel lbKey = new JLabel("Khoá (key)");
		String[] arrPhuongPhap = {"AES", "DES", "Base64", "Caesar", "RSA"};
        JComboBox<String> cbLoaiMH = new JComboBox<>(arrPhuongPhap);
//        key
		JTextField key = new JTextField();
		SpinnerModel spinnerModel = new SpinnerNumberModel(16,1,56,1);
		JSpinner jSpinnerLenght = new JSpinner(spinnerModel);
		JButton creatKey = new JButton("Tạo khoá");
//		JButton btnInputKey = new JButton("Nhập file");
		JButton btnOutputKey = new JButton("Xuất file");
		
		JPanel panelKey = new JPanel(new FlowLayout(FlowLayout.RIGHT,5,0));
		panelKey.add(new Label("Độ dài khoá:"));
		panelKey.add(jSpinnerLenght);
		panelKey.add(creatKey);
//		panelKey.add(btnInputKey);
		panelKey.add(btnOutputKey);
		JPanel keyGroup = new JPanel(new BorderLayout(5,0));
		keyGroup.add(key,BorderLayout.CENTER);
		keyGroup.add(panelKey,BorderLayout.EAST);
		
		panelTop.add(lbMethod);
		panelTop.add(lbKey);
		panelTop.add(cbLoaiMH);
		panelTop.add(keyGroup);
		
		JPanel panelCenter = new JPanel(new GridLayout(1,2,20,0));
		JPanel input = new JPanel(new BorderLayout(0,5));
		JLabel lbInput = new JLabel("Dữ liệu cần mã hoá");
		JTextArea textInput = new JTextArea();
		textInput.setLineWrap(true);
		textInput.setWrapStyleWord(true);
		JButton btnAddFileInput = new JButton("Thêm file dữ liệu");
		JPanel panelBtnIn = new JPanel(new FlowLayout(FlowLayout.CENTER));
		panelBtnIn.add(btnAddFileInput);
		input.add(lbInput,BorderLayout.NORTH);
		input.add(panelBtnIn,BorderLayout.SOUTH);
		input.add(new JScrollPane(textInput),BorderLayout.CENTER);
		JPanel output = new JPanel(new BorderLayout(0,5));
		JLabel lbOutput = new JLabel("Dữ liệu đã mã hoá");
		JTextArea textOutput = new JTextArea();
		textOutput.setEditable(false);
		textOutput.setLineWrap(true);
		JButton btnAddFileOutput = new JButton("Xuất file đã mã hoá");
		JPanel panelBtnOut = new JPanel(new FlowLayout(FlowLayout.CENTER));
		panelBtnOut.add(btnAddFileOutput);
		output.add(panelBtnOut,BorderLayout.SOUTH);
		textOutput.setWrapStyleWord(true);
		output.add(lbOutput,BorderLayout.NORTH);
		output.add(new JScrollPane(textOutput),BorderLayout.CENTER);
		panelCenter.add(input);
		panelCenter.add(output);
		
		JPanel panelButton = new JPanel(new FlowLayout(FlowLayout.CENTER));
		JButton btnEncrypt = new JButton("Thực hiện mã hoá");
		btnEncrypt.setPreferredSize(new java.awt.Dimension(200, 40)); 
        panelButton.add(btnEncrypt);
		
		
		tabEncript.add(panelTop,BorderLayout.NORTH);
		tabEncript.add(panelCenter,BorderLayout.CENTER);
		tabEncript.add(panelButton,BorderLayout.SOUTH);
	
		
//		tab2
		JPanel tabDecript = new JPanel();
		tabDecript.setLayout(new BorderLayout(10,10));
		tabDecript.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
		
		JPanel panelTop2 = new JPanel(new GridLayout(2, 2, 15, 5));
		JLabel lbMethod2 = new JLabel("Phương pháp mã hoá");
		JLabel lbKey2 = new JLabel("Khoá (key)");
        JComboBox<String> cbLoaiMH2 = new JComboBox<>(arrPhuongPhap);
//        key
		JTextField keyDecript = new JTextField();
		JButton btnInputKey = new JButton("Nhập file");
		JPanel panelKey2 = new JPanel(new FlowLayout(FlowLayout.RIGHT,5,0));
		
		panelKey2.add(btnInputKey);
		JPanel keyGroup2 = new JPanel(new BorderLayout(5,0));
		keyGroup2.add(keyDecript,BorderLayout.CENTER);
		keyGroup2.add(panelKey2,BorderLayout.EAST);
		
		panelTop2.add(lbMethod2);
		panelTop2.add(lbKey2);
		panelTop2.add(cbLoaiMH2);
		panelTop2.add(keyGroup2);
		
		JPanel panelCenter2 = new JPanel(new GridLayout(1,2,20,0));
		JPanel input2 = new JPanel(new BorderLayout(0,5));
		JLabel lbInput2 = new JLabel("Dữ liệu cần giải mã ");
		JTextArea textInput2 = new JTextArea();
		textInput2.setLineWrap(true);
		textInput2.setWrapStyleWord(true);
		input2.add(lbInput2,BorderLayout.NORTH);
		input2.add(new JScrollPane(textInput2),BorderLayout.CENTER);
		JPanel output2 = new JPanel(new BorderLayout(0,5));
		JLabel lbOutput2 = new JLabel("Dữ liệu đã giải mã");
		JTextArea textOutput2 = new JTextArea();
		textOutput2.setEditable(false);
		textOutput2.setLineWrap(true);
		textOutput2.setWrapStyleWord(true);
		output2.add(lbOutput2,BorderLayout.NORTH);
		output2.add(new JScrollPane(textOutput2),BorderLayout.CENTER);
		panelCenter2.add(input2);
		panelCenter2.add(output2);
		
		JPanel panelButton2 = new JPanel(new FlowLayout(FlowLayout.CENTER));
		JButton btnDecrypt = new JButton("Thực hiện mã hoá");
		btnDecrypt.setPreferredSize(new java.awt.Dimension(200, 40)); 
        panelButton2.add(btnDecrypt);
		
		
		tabDecript.add(panelTop2,BorderLayout.NORTH);
		tabDecript.add(panelCenter2,BorderLayout.CENTER);
		tabDecript.add(panelButton2,BorderLayout.SOUTH);
		
		
		
		tabbedPane.add("Mã hoá", tabEncript);
		tabbedPane.add("Giải mã", tabDecript);
		
		
		this.add(tabbedPane);
		
	}
	
	public static void main(String[] args) {
		View3 view = new View3();
		view.setVisible(true);
	}
	

}
