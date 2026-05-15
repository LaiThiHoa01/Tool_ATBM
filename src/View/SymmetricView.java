package View;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class SymmetricView extends JPanel {
    private JComboBox<String> cbMethodsSymmetric, cbModes, cbPadding1, cbLenghtKey1;
    private JTextField keySymmetric;
    private JTextArea textInputFileSymmetric, textOutputSymmetric, textInputSymmetricText;
    private JButton creatKeySymmetric, btnInputKeySymmetric, btnOutputKeySymmetric, btnFileInputSymmetric, btnEncryptSymmetric, btnDecryptSymmetric, btnDeleteKey, btnCopyKey, btnSaveFileOut;
    private JTabbedPane tabFileOrText;
    private String selectedFilePath = "";
    private JScrollPane scrollOutput;

    private final Color bgDark = Color.decode("#004643");
    private final Color btnColor = Color.decode("#f9bc60");
    private final Color btnTextColor = Color.decode("#001e1d");


    public SymmetricView() {
        setLayout(new BorderLayout(10, 10));
        setBackground(Color.decode("#004643"));
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JPanel panelTop = new JPanel(new GridLayout(5, 2, 15, 5));
        panelTop.setOpaque(false);

        cbMethodsSymmetric = new JComboBox<>(new String[]{"AES","ARIA", "Blowfish", "Camellia", "CAST5","CAST6","DES", "DESede", "Serpent", "Twofish"});
        cbModes = new JComboBox<>(new String[]{"CBC", "ECB", "CFB", "OFB", "CTR"});
        cbPadding1 = new JComboBox<>(new String[]{"PKCS5Padding","PKCS7Padding", "NoPadding"});
        cbLenghtKey1 = new JComboBox<>();
        keySymmetric = new JTextField(30);
        cbMethodsSymmetric.addActionListener(e -> keySymmetric.setText(""));
        JPanel pnGroupKey = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        btnDeleteKey = new JButton("Xoá khoá");
        btnCopyKey = new JButton("Sao chép");
        pnGroupKey.add(btnDeleteKey);
        pnGroupKey.add(btnCopyKey);
        styleButton(btnDeleteKey);
        styleButton(btnCopyKey);
        JPanel pnKey = new JPanel(new BorderLayout(5, 0));
        pnKey.add(keySymmetric, BorderLayout.CENTER);
        pnKey.add(pnGroupKey, BorderLayout.EAST);



        creatKeySymmetric = new JButton("Tạo khoá");
        btnInputKeySymmetric = new JButton("Tải khoá");
        btnOutputKeySymmetric = new JButton("Lưu khoá");
        styleButton(creatKeySymmetric);
        styleButton(btnInputKeySymmetric);
        styleButton(btnOutputKeySymmetric);

        JPanel panelKeyActions = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        panelKeyActions.setOpaque(false);
        panelKeyActions.add(cbLenghtKey1);
        panelKeyActions.add(creatKeySymmetric);
        panelKeyActions.add(btnInputKeySymmetric);
        panelKeyActions.add(btnOutputKeySymmetric);

        panelTop.add(new JLabel("Thuật toán:")); panelTop.add(cbMethodsSymmetric);
        panelTop.add(new JLabel("Chế độ (Mode):")); panelTop.add(cbModes);
        panelTop.add(new JLabel("Kiểu đệm (Padding):")); panelTop.add(cbPadding1);
        panelTop.add(new JLabel("Độ dài khóa:")); panelTop.add(panelKeyActions);
        panelTop.add(new JLabel("Khóa (Base64):")); panelTop.add(pnKey);

        JPanel panelCenter = new JPanel(new GridLayout(1, 2, 20, 0));
        panelCenter.setOpaque(false);

        JPanel panelRightWrapper = new JPanel(new BorderLayout());
        panelRightWrapper.setOpaque(false);
        panelRightWrapper.setBorder(BorderFactory.createEmptyBorder(32, 0, 12, 0));

        tabFileOrText = new JTabbedPane();
        textInputSymmetricText = new JTextArea();
        textInputSymmetricText.setMargin(new Insets(10, 10, 10, 10));
        JPanel pnFile = new JPanel(new BorderLayout(0, 15));
        pnFile.setOpaque(false);
        pnFile.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        textInputFileSymmetric = new JTextArea("Chưa chọn file...");
        textInputFileSymmetric.setEditable(false);
        textInputFileSymmetric.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        textInputFileSymmetric.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JScrollPane spFile = new JScrollPane(textInputFileSymmetric);
        JPanel pnBottomBtn = new JPanel(new FlowLayout(FlowLayout.CENTER));
        pnBottomBtn.setOpaque(false);

        btnFileInputSymmetric = new JButton("Tải File");
        styleButton(btnFileInputSymmetric);


        btnFileInputSymmetric.setPreferredSize(new Dimension(200, 40));
        styleButton(btnFileInputSymmetric);
        pnBottomBtn.add(btnFileInputSymmetric);
        pnFile.add(spFile, BorderLayout.CENTER);
        pnFile.add(pnBottomBtn, BorderLayout.SOUTH);

        tabFileOrText.addTab("Văn bản", new JScrollPane(textInputSymmetricText));
        tabFileOrText.addTab("File", pnFile);
        textOutputSymmetric = new JTextArea();
        textOutputSymmetric.setMargin(new Insets(10, 10, 10, 10));
        textOutputSymmetric.setEditable(false);
        scrollOutput = new JScrollPane(textOutputSymmetric);
        panelRightWrapper.add(scrollOutput, BorderLayout.CENTER);

        btnSaveFileOut = new JButton("Lưu file");
        styleButton(btnSaveFileOut);
        JPanel pnOutputBtn = new JPanel(new FlowLayout(FlowLayout.CENTER));
        pnOutputBtn.setOpaque(false);
        pnOutputBtn.add(btnSaveFileOut);
        panelRightWrapper.add(pnOutputBtn, BorderLayout.SOUTH);


        panelCenter.add(tabFileOrText);
        panelCenter.add(panelRightWrapper);

        JPanel panelBottom = new JPanel(new FlowLayout(FlowLayout.CENTER));
        panelBottom.setOpaque(false);
        btnEncryptSymmetric = new JButton("Mã hoá");
        btnDecryptSymmetric = new JButton("Giải mã");
        styleButton(btnEncryptSymmetric); styleButton(btnDecryptSymmetric);
        btnEncryptSymmetric.setPreferredSize(new Dimension(150, 40));
        btnDecryptSymmetric.setPreferredSize(new Dimension(150, 40));
        panelBottom.add(btnEncryptSymmetric);
        panelBottom.add(btnDecryptSymmetric);

        add(panelTop, BorderLayout.NORTH);
        add(panelCenter, BorderLayout.CENTER);
        add(panelBottom, BorderLayout.SOUTH);
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

    public String getAlgorithm() { return cbMethodsSymmetric.getSelectedItem().toString(); }
    public String getMode() { return cbModes.getSelectedItem().toString(); }
    public String getPadding() { return cbPadding1.getSelectedItem().toString(); }
    public int getKeyLength() { return Integer.parseInt(cbLenghtKey1.getSelectedItem().toString()); }
    public String getSymmetricKey() { return keySymmetric.getText().trim(); }
    public void setSymmetricKey(String key) { keySymmetric.setText(key); }
    public String getSymmetricInputText() { return textInputSymmetricText.getText(); }
    public void setSymmetricOutputText(String text) { textOutputSymmetric.setText(text); }
    public int getSymmetricSelectedTab() { return tabFileOrText.getSelectedIndex(); }
    public String getSelectedFilePath() { return selectedFilePath; }
    public void setTextAreaOutputFile(String text) { textOutputSymmetric.setText(text); }
    public void setSelectedFilePath(String path) {
        this.selectedFilePath = path;
        textInputFileSymmetric.setText(path);
    }

    public void updateKeyLengthOptions(String[] options) {
        cbLenghtKey1.removeAllItems();
        for (String s : options) cbLenghtKey1.addItem(s);
    }

    public void showMessage(String msg) { JOptionPane.showMessageDialog(this, msg); }

    public void addAlgorithmChangeListener(ActionListener l) { cbMethodsSymmetric.addActionListener(l); }
    public void addBtnCreateKey(ActionListener l) { creatKeySymmetric.addActionListener(l); }
    public void addBtnInputKey(ActionListener l) { btnInputKeySymmetric.addActionListener(l); }
    public void addBtnSaveKey(ActionListener l) { btnOutputKeySymmetric.addActionListener(l); }
    public void addBtnEncrypt(ActionListener l) { btnEncryptSymmetric.addActionListener(l); }
    public void addBtnDecrypt(ActionListener l) { btnDecryptSymmetric.addActionListener(l); }
    public void addBtnChooseFile(ActionListener l) { btnFileInputSymmetric.addActionListener(l); }
    public void addBtnDeleteKey(ActionListener l){
        btnDeleteKey.addActionListener(l);
    }
    public void addBtnCopyKey(ActionListener l){
        btnCopyKey.addActionListener(l);
    }
    public void addBtnSaveFileOut(ActionListener l) { btnSaveFileOut.addActionListener(l); }
}