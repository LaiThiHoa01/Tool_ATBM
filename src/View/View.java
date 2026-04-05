package View;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

import java.awt.*;
import java.awt.event.ActionListener;

public class View extends JFrame {
    private JComboBox<String> cbLoaiMH;
    private JTextField key;
    private JRadioButton vn, eng;
    private JTextArea textInput, textOutput;
    private JButton btnEncrypt, btnDecrypt, btnCreatKey,btnCopyKey,btnDeleteKey;

    private final Color bgDark = Color.decode("#004643");
    private final Color bgLight = Color.decode("#e8e4e6"); // xanh đậm
    private final Color textMain = Color.decode("#fffffe");
    private final Color btnColor = Color.decode("#f9bc60"); //cam
    private final Color btnTextColor = Color.decode("#001e1d"); // xanh đậm

    public View() {
        UIManager.put("Panel.background", bgDark);
        UIManager.put("Label.foreground", textMain);
        UIManager.put("RadioButton.background", bgDark);
        UIManager.put("RadioButton.foreground", textMain);
        UIManager.put("TabbedPane.background", bgLight);
        UIManager.put("TabbedPane.foreground", btnTextColor);
        UIManager.put("TabbedPane.selected", btnColor);
        UIManager.put("TabbedPane.contentAreaColor", bgDark);
        UIManager.put("ComboBox.background", bgLight);
        UIManager.put("ComboBox.foreground", btnTextColor);
        UIManager.put("ScrollPane.background", bgDark);
        UIManager.put("ScrollPane.border", BorderFactory.createEmptyBorder());
        UIManager.put("OptionPane.background", bgDark);
        UIManager.put("OptionPane.messageForeground", textMain);

        setSize(1350, 650);
        setTitle("Tool mã hoá");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        getContentPane().setBackground(bgDark);



        JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);

//		 tab ma hoa co dien
        JPanel classicalCryptography = new JPanel(new BorderLayout(10,10));
        classicalCryptography.setBackground(bgDark);
        classicalCryptography.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JPanel groupTop = new JPanel(new BorderLayout());
        groupTop.setBackground(bgDark);
        JPanel panelTop = new JPanel(new GridLayout(2, 2, 15, 5));
        JLabel lbMethod = new JLabel("Phương pháp mã hoá");
        JLabel lbKey = new JLabel("Khoá (key)");
        String[] arrPhuongPhap = {"Caesar", "Vigenere", "Affine", "Hill", "Substitution"};
        cbLoaiMH = new JComboBox<>(arrPhuongPhap);
//        key
        key = new JTextField();

        btnCreatKey = new JButton("Tạo khoá");
        btnCopyKey = new JButton("Sao chép");
        btnDeleteKey = new JButton("Xoá khoá");
        styleButton(btnCreatKey);
        styleButton(btnCopyKey);
        styleButton(btnDeleteKey);

        JPanel panelKey = new JPanel(new FlowLayout(FlowLayout.RIGHT,5,0));
        panelKey.add(btnCreatKey);
        panelKey.add(btnCopyKey);
        panelKey.add(btnDeleteKey);
//		panelKey.add(btnInputKey);
        JPanel keyGroup = new JPanel(new BorderLayout(5,0));
        keyGroup.add(key,BorderLayout.CENTER);
        keyGroup.add(panelKey,BorderLayout.EAST);

        panelTop.add(lbMethod);
        panelTop.add(lbKey);
        panelTop.add(cbLoaiMH);
        panelTop.add(keyGroup);
        JPanel radioTop = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JLabel language = new JLabel("Ngôn ngữ");
        vn = new JRadioButton("Tiếng Việt");
        eng = new JRadioButton("Tiếng Anh");
        ButtonGroup groupButton = new ButtonGroup();
        groupButton.add(vn);
        groupButton.add(eng);
        radioTop.add(language);
        radioTop.add(vn);
        radioTop.add(eng);

        groupTop.add(radioTop,BorderLayout.NORTH);
        groupTop.add(panelTop, BorderLayout.CENTER);


        JPanel panelCenter = new JPanel(new GridLayout(1,2,20,0));
        panelCenter.setBackground(bgDark);
        JPanel input = new JPanel(new BorderLayout(0,5));
        JLabel lbInput = new JLabel("Dữ liệu ");
        textInput = new JTextArea();
        textInput.setLineWrap(true);
        textInput.setWrapStyleWord(true);
        JPanel panelBtnIn = new JPanel(new FlowLayout(FlowLayout.CENTER));
        input.add(lbInput,BorderLayout.NORTH);
        input.add(panelBtnIn,BorderLayout.SOUTH);
        input.add(new JScrollPane(textInput),BorderLayout.CENTER);
        JPanel output = new JPanel(new BorderLayout(0,5));
        JLabel lbOutput = new JLabel("Kết quả");
        textOutput = new JTextArea();
        textOutput.setEditable(false);
        textOutput.setLineWrap(true);
        JPanel panelBtnOut = new JPanel(new FlowLayout(FlowLayout.CENTER));
        output.add(panelBtnOut,BorderLayout.SOUTH);
        textOutput.setWrapStyleWord(true);
        output.add(lbOutput,BorderLayout.NORTH);
        output.add(new JScrollPane(textOutput),BorderLayout.CENTER);
        panelCenter.add(input);
        panelCenter.add(output);

        JPanel panelButton = new JPanel(new FlowLayout(FlowLayout.CENTER));
        panelButton.setBackground(bgDark);
        btnEncrypt = new JButton("Mã hoá");
        btnDecrypt = new JButton("Giải mã");
        styleButton(btnEncrypt);
        styleButton(btnDecrypt);
        btnEncrypt.setPreferredSize(new java.awt.Dimension(200, 40));
        btnDecrypt.setPreferredSize(new java.awt.Dimension(200, 40));
        panelButton.add(btnEncrypt);
        panelButton.add(btnDecrypt);


        classicalCryptography.add(groupTop,BorderLayout.NORTH);
        classicalCryptography.add(panelCenter,BorderLayout.CENTER);
        classicalCryptography.add(panelButton,BorderLayout.SOUTH);

//       tab ma hoa doi xung

        JPanel symmetricEncryption = new JPanel(new BorderLayout(10,10));
        symmetricEncryption.setBackground(bgDark);
        symmetricEncryption.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        JPanel panelTop1 = new JPanel(new GridLayout(5, 2, 15, 5));
        JLabel lbMethods = new JLabel("Chọn thuật toán");
        String[] arrMethodsSymmetric ={"DES"};
        JComboBox<String> cbMethodsSymmetric = new JComboBox<String>(arrMethodsSymmetric );

        JLabel lbMethod1 = new JLabel("Các kiểu thao tác");
        JLabel lbKey1 = new JLabel("Khoá (key)");
        String[] arrModesSymmetric = {"ECB", "CBC", "PCBC", "CFB", "OFB", "CTR"};
        JComboBox<String> cbModes = new JComboBox<>(arrModesSymmetric);
        JLabel lbPadding1 = new JLabel("Kiểu đệm");
        String[] arrPadding1 = {"PKCS5", "PKCS7", "OAEP"};
        JComboBox<String> cbPadding1= new JComboBox<>(arrPadding1);
//        key
        JTextField key1 = new JTextField();
//        SpinnerModel spinnerModel1 = new SpinnerNumberModel(16,1,56,1);
//        JSpinner jSpinnerLenght1 = new JSpinner(spinnerModel1);
        JButton creatKey1 = new JButton("Tạo khoá");
        JButton btnInputKey1 = new JButton("Tải khoá");
        JButton btnOutputKey1 = new JButton("Lưu khoá");
        styleButton(creatKey1);
        styleButton(btnInputKey1);
        styleButton(btnOutputKey1);

        JLabel lenghtKey = new JLabel("Độ dài khoá:");
        JPanel panelKey1 = new JPanel(new FlowLayout(FlowLayout.LEFT,4,0));
        String[] arrLenghtKey = {"54","128"};
        JComboBox<String> cbLenghtKey1 = new JComboBox<>(arrLenghtKey);
        cbLenghtKey1.setPreferredSize(new Dimension(150, 35));
        panelKey1.add(cbLenghtKey1);
        panelKey1.add(creatKey1);
        panelKey1.add(btnInputKey1);
        panelKey1.add(btnOutputKey1);

        panelTop1.add(lbMethods);
        panelTop1.add(cbMethodsSymmetric);
        panelTop1.add(lbMethod1);
        panelTop1.add(cbModes);
        panelTop1.add(lbPadding1);
        panelTop1.add(cbPadding1);
        panelTop1.add(lenghtKey);
        panelTop1.add(panelKey1);
        panelTop1.add(lbKey1);
        panelTop1.add(key1);

        JPanel panelCenter1 = new JPanel(new GridLayout(1,2,20,0));
        JPanel input1 = new JPanel(new BorderLayout(0,5));

        JTabbedPane tab = new JTabbedPane();

        JLabel lbInput1 = new JLabel("Dữ liệu cần mã hoá");
        JTextArea textInput1 = new JTextArea();

        textInput1.setEditable(false);
        JPanel pnInput1 = new JPanel(new BorderLayout());


        JTextArea textInputFile = new JTextArea();
        textInputFile.setLineWrap(true);
        textInputFile.setWrapStyleWord(true);
        textInputFile.setLayout(new BorderLayout());
        JScrollPane scrollFile = new JScrollPane(textInputFile);
        pnInput1.add(scrollFile, BorderLayout.CENTER);
        JButton btnFileInput = new JButton("Tải File");
        styleButton(btnFileInput);

        btnFileInput.setPreferredSize(new java.awt.Dimension(200, 40));
        JPanel pnButton = new JPanel();
        pnButton.add(btnFileInput);

        pnInput1.add(pnButton, BorderLayout.SOUTH);
//		pnInput1.add(btnFileInput,BorderLayout.SOUTH);
        tab.add("Văn bản",textInput1);
        tab.add("File",pnInput1);
        input1.add(lbInput1,BorderLayout.NORTH);
        input1.add(tab,BorderLayout.CENTER);


        JPanel output1 = new JPanel(new BorderLayout(0,5));
        JLabel lbOutput1 = new JLabel("Dữ liệu đã mã hoá");
        JTextArea textOutput1 = new JTextArea();
        textOutput1.setEditable(false);
        textOutput1.setLineWrap(true);
        JPanel panelBtnOut1 = new JPanel(new FlowLayout(FlowLayout.CENTER));
        output1.add(panelBtnOut1,BorderLayout.SOUTH);
        textOutput1.setWrapStyleWord(true);
        output1.add(lbOutput1,BorderLayout.NORTH);
        output1.add(new JScrollPane(textOutput1),BorderLayout.CENTER);
        panelCenter1.add(input1);
        panelCenter1.add(output1);

        JPanel panelButton1 = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton btnEncrypt1 = new JButton("Mã hoá");
        JButton btnDecrypt1 = new JButton("Giải mã");
        styleButton(btnEncrypt1);
        styleButton(btnDecrypt1);
        btnEncrypt1.setPreferredSize(new java.awt.Dimension(200, 40));
        btnDecrypt1.setPreferredSize(new java.awt.Dimension(200, 40));
        panelButton1.add(btnEncrypt1);
        panelButton1.add(btnDecrypt1);


        symmetricEncryption.add(panelTop1,BorderLayout.NORTH);
        symmetricEncryption.add(panelCenter1,BorderLayout.CENTER);
        symmetricEncryption.add(panelButton1,BorderLayout.SOUTH);





//       tab ma hoa bat doi xung
        JPanel asymetricEncryption = new JPanel(new BorderLayout(10,10));
        asymetricEncryption.setBackground(bgDark);
        asymetricEncryption.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
//        hash
        JPanel hash = new JPanel(new BorderLayout(10,10));
        hash.setBackground(bgDark);
        hash.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));




        tabbedPane.add("Mã hoá cổ điển", classicalCryptography);
        tabbedPane.add("Mã hoá đối xứng", symmetricEncryption);
        tabbedPane.add("Mã hoá bất đối xứng", asymetricEncryption);
        tabbedPane.add("Hash", hash);
        tabbedPane.setBorder(BorderFactory.createEmptyBorder());
        this.add(tabbedPane,BorderLayout.CENTER);

    }
    private void styleButton(JButton btn) {
        btn.setBackground(btnColor);
        btn.setForeground(btnTextColor);
        btn.setFocusPainted(false);
        btn.setOpaque(true);
        btn.setContentAreaFilled(true);
        btn.setBorder(BorderFactory.createCompoundBorder(new LineBorder(btnColor, 1, true),
                new EmptyBorder(5, 15, 5, 15)
        ));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setFont(new Font("Segoe UI", Font.BOLD, 15));

    }
    public String getInputText() {
        return textInput.getText();
    }
    public String getKey() {
        return key.getText();
    }
    public String getMethods() {
        return (String) cbLoaiMH.getSelectedItem().toString();
    }
    public boolean isVN() {
        return vn.isSelected();
    }
    public void setOutputText(String text) {
        textOutput.setText(text);
    }
    public void setKey(String text) {
        key.setText(text);
    }
    public void showMessage(String message) {
        JOptionPane.showConfirmDialog(this, message,"Thông báo",JOptionPane.ERROR_MESSAGE);
    }

    public void addGenKey(ActionListener actionListener) {
        btnCreatKey.addActionListener(actionListener);
    }
    public void addbtnEncrypt(ActionListener actionListener) {
        btnEncrypt.addActionListener(actionListener);
    }
    public void addbtnDecrypt(ActionListener actionListener) {
        btnDecrypt.addActionListener(actionListener);
    }
    public void addCopy(ActionListener actionListener) {
        btnCopyKey.addActionListener(actionListener);
    }
    public void addDeleteKey(ActionListener actionListener) {
        btnDeleteKey.addActionListener(actionListener);
    }
    public static void main(String[] args) {
        View view = new View();
        view.setVisible(true);
    }


}