package View;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class Asymmetric extends JPanel {
    private JComboBox<String> cbMethodsAsymmetric, cbModes, cbPadding, cbLengthKey;
    private JTextField publicKeyField, privateKeyField;
    private JTextArea textInputFileAsymmetric, textOutputAsymmetric, textInputAsymmetricText;
    private JButton btnCreateKeyPair, btnInputPubKey, btnOutputPubKey, btnInputPrivKey, btnOutputPrivKey;
    private JButton btnFileInputAsymmetric, btnEncryptAsymmetric, btnDecryptAsymmetric;
    private JButton  btnCopyPubKey, btnCopyPrivKey;
    private JTabbedPane tabFileOrText;
    private String selectedFilePath = "";
    private JScrollPane scrollOutput;

    private final Color bgDark = Color.decode("#004643");
    private final Color btnColor = Color.decode("#f9bc60");
    private final Color btnTextColor = Color.decode("#001e1d");

    public Asymmetric() {
        setLayout(new BorderLayout(10, 10));
        setBackground(bgDark);
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JPanel panelTop = new JPanel(new GridLayout(6, 2, 15, 5));
        panelTop.setOpaque(false);

        cbMethodsAsymmetric = new JComboBox<>(new String[]{"RSA"});
        cbModes = new JComboBox<>(new String[]{"ECB"});
        cbPadding = new JComboBox<>(new String[]{"PKCS1Padding", "OAEPWithSHA-1AndMGF1Padding", "OAEPWithSHA-256AndMGF1Padding", "NoPadding"});
        cbLengthKey = new JComboBox<>(new String[]{"1024", "2048", "3072", "4096"});

        btnCreateKeyPair = new JButton("Tạo cặp khoá");
        styleButton(btnCreateKeyPair);

        JPanel panelKeyActions = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        panelKeyActions.setOpaque(false);
        panelKeyActions.add(cbLengthKey);
        panelKeyActions.add(btnCreateKeyPair);

        publicKeyField = new JTextField(25);
        btnCopyPubKey = new JButton("Sao chép");
        btnInputPubKey = new JButton("Tải");
        btnOutputPubKey = new JButton("Lưu");
         styleButton(btnCopyPubKey);
        styleButton(btnInputPubKey);
        styleButton(btnOutputPubKey);

        JPanel pnGroupPubKey = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        pnGroupPubKey.setOpaque(false);
        pnGroupPubKey.add(btnCopyPubKey);
        pnGroupPubKey.add(btnInputPubKey);
        pnGroupPubKey.add(btnOutputPubKey);

        JPanel pnPubKey = new JPanel(new BorderLayout(5, 0));
        pnPubKey.setOpaque(false);
        pnPubKey.add(publicKeyField, BorderLayout.CENTER);
        pnPubKey.add(pnGroupPubKey, BorderLayout.EAST);

        privateKeyField = new JTextField(25);
        btnCopyPrivKey = new JButton("Sao chép");
        btnInputPrivKey = new JButton("Tải");
        btnOutputPrivKey = new JButton("Lưu");
        styleButton(btnCopyPrivKey);
        styleButton(btnInputPrivKey);
        styleButton(btnOutputPrivKey);

        JPanel pnGroupPrivKey = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        pnGroupPrivKey.setOpaque(false);
        pnGroupPrivKey.add(btnCopyPrivKey);
        pnGroupPrivKey.add(btnInputPrivKey);
        pnGroupPrivKey.add(btnOutputPrivKey);

        JPanel pnPrivKey = new JPanel(new BorderLayout(5, 0));
        pnPrivKey.setOpaque(false);
        pnPrivKey.add(privateKeyField, BorderLayout.CENTER);
        pnPrivKey.add(pnGroupPrivKey, BorderLayout.EAST);

        panelTop.add(createLabel("Thuật toán:"));
        panelTop.add(cbMethodsAsymmetric);
        panelTop.add(createLabel("Chế độ (Mode):"));
        panelTop.add(cbModes);
        panelTop.add(createLabel("Kiểu đệm (Padding):"));
        panelTop.add(cbPadding);
        panelTop.add(createLabel("Độ dài khóa:"));
        panelTop.add(panelKeyActions);
        panelTop.add(createLabel("Public Key (Base64):"));
        panelTop.add(pnPubKey);
        panelTop.add(createLabel("Private Key (Base64):"));
        panelTop.add(pnPrivKey);

        JPanel panelCenter = new JPanel(new GridLayout(1, 2, 20, 0));
        panelCenter.setOpaque(false);

        tabFileOrText = new JTabbedPane();
        textInputAsymmetricText = new JTextArea();
        textInputAsymmetricText.setMargin(new Insets(10, 10, 10, 10));
        textInputAsymmetricText.setLineWrap(true);
        textInputAsymmetricText.setWrapStyleWord(true);

        JPanel pnFile = new JPanel(new BorderLayout(0, 15));
        pnFile.setOpaque(false);
        pnFile.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        textInputFileAsymmetric = new JTextArea("Chưa chọn file...");
        textInputFileAsymmetric.setEditable(false);
        textInputFileAsymmetric.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        textInputFileAsymmetric.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JScrollPane spFile = new JScrollPane(textInputFileAsymmetric);
        JPanel pnBottomBtn = new JPanel(new FlowLayout(FlowLayout.CENTER));
        pnBottomBtn.setOpaque(false);

        btnFileInputAsymmetric = new JButton("Tải File");
        btnFileInputAsymmetric.setPreferredSize(new Dimension(200, 40));
        styleButton(btnFileInputAsymmetric);
        pnBottomBtn.add(btnFileInputAsymmetric);
        pnFile.add(spFile, BorderLayout.CENTER);
        pnFile.add(pnBottomBtn, BorderLayout.SOUTH);

        tabFileOrText.addTab("Văn bản", new JScrollPane(textInputAsymmetricText));
        tabFileOrText.addTab("File", pnFile);

        JPanel panelRightWrapper = new JPanel(new BorderLayout());
        panelRightWrapper.setOpaque(false);
        panelRightWrapper.setBorder(BorderFactory.createEmptyBorder(32, 0, 12, 0));

        textOutputAsymmetric = new JTextArea();
        textOutputAsymmetric.setMargin(new Insets(10, 10, 10, 10));
        textOutputAsymmetric.setLineWrap(true);
        textOutputAsymmetric.setWrapStyleWord(true);
        textOutputAsymmetric.setEditable(false);
        scrollOutput = new JScrollPane(textOutputAsymmetric);
        panelRightWrapper.add(scrollOutput, BorderLayout.CENTER);

        panelCenter.add(tabFileOrText);
        panelCenter.add(panelRightWrapper);

        JPanel panelBottom = new JPanel(new FlowLayout(FlowLayout.CENTER));
        panelBottom.setOpaque(false);
        btnEncryptAsymmetric = new JButton("Mã hoá");
        btnDecryptAsymmetric = new JButton("Giải mã");
        styleButton(btnEncryptAsymmetric);
        styleButton(btnDecryptAsymmetric);
        btnEncryptAsymmetric.setPreferredSize(new Dimension(150, 40));
        btnDecryptAsymmetric.setPreferredSize(new Dimension(150, 40));
        panelBottom.add(btnEncryptAsymmetric);
        panelBottom.add(btnDecryptAsymmetric);

        add(panelTop, BorderLayout.NORTH);
        add(panelCenter, BorderLayout.CENTER);
        add(panelBottom, BorderLayout.SOUTH);
    }

    private JLabel createLabel(String text) {
        JLabel label = new JLabel(text);
        label.setForeground(Color.WHITE);
        return label;
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
        btn.setFont(new Font("Segoe UI", Font.BOLD, 13));
    }

    public String getAlgorithm() { return cbMethodsAsymmetric.getSelectedItem().toString(); }
    public String getMode() { return cbModes.getSelectedItem().toString(); }
    public String getPadding() { return cbPadding.getSelectedItem().toString(); }
    public int getKeyLength() { return Integer.parseInt(cbLengthKey.getSelectedItem().toString()); }

    public String getPublicKey() { return publicKeyField.getText().trim(); }
    public void setPublicKey(String key) { publicKeyField.setText(key); }

    public String getPrivateKey() { return privateKeyField.getText().trim(); }
    public void setPrivateKey(String key) { privateKeyField.setText(key); }

    public String getAsymmetricInputText() { return textInputAsymmetricText.getText(); }
    public void setAsymmetricOutputText(String text) { textOutputAsymmetric.setText(text); }
    public int getAsymmetricSelectedTab() { return tabFileOrText.getSelectedIndex(); }
    public String getSelectedFilePath() { return selectedFilePath; }

    public void setTextAreaOutputFile(String text) { textOutputAsymmetric.setText(text); }
    public void setSelectedFilePath(String path) {
        this.selectedFilePath = path;
        textInputFileAsymmetric.setText(path);
    }

    public void updateKeyLengthOptions(String[] options) {
        cbLengthKey.removeAllItems();
        for (String s : options) cbLengthKey.addItem(s);
    }

    public void showMessage(String msg) { JOptionPane.showMessageDialog(this, msg); }

    public void addAlgorithmChangeListener(ActionListener l) { cbMethodsAsymmetric.addActionListener(l); }
    public void addBtnCreateKeyPair(ActionListener l) { btnCreateKeyPair.addActionListener(l); }

    public void addBtnInputPubKey(ActionListener l) { btnInputPubKey.addActionListener(l); }
    public void addBtnOutputPubKey(ActionListener l) { btnOutputPubKey.addActionListener(l); }
    public void addBtnCopyPubKey(ActionListener l) { btnCopyPubKey.addActionListener(l); }

    public void addBtnInputPrivKey(ActionListener l) { btnInputPrivKey.addActionListener(l); }
    public void addBtnOutputPrivKey(ActionListener l) { btnOutputPrivKey.addActionListener(l); }
    public void addBtnCopyPrivKey(ActionListener l) { btnCopyPrivKey.addActionListener(l); }

    public void addBtnEncrypt(ActionListener l) { btnEncryptAsymmetric.addActionListener(l); }
    public void addBtnDecrypt(ActionListener l) { btnDecryptAsymmetric.addActionListener(l); }
    public void addBtnChooseFile(ActionListener l) { btnFileInputAsymmetric.addActionListener(l); }
}