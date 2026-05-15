package View;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionListener;

public class ClassicalView extends JPanel {
    private JComboBox<String> cbLoaiMH;
    private JTextField key;
    private JRadioButton vn, eng;
    private ButtonGroup groupButton;
    private JTextArea textInput, textOutput;
    private JButton btnEncrypt, btnDecrypt, btnCreatKey, btnCopyKey, btnDeleteKey, btnSaveFileOut;

    private final Color bgDark = Color.decode("#004643");
    private final Color btnColor = Color.decode("#f9bc60");
    private final Color btnTextColor = Color.decode("#001e1d");

    public ClassicalView() {
        setLayout(new BorderLayout(10, 10));
        setBackground(bgDark);
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JPanel groupTop = new JPanel(new BorderLayout());
        groupTop.setBackground(bgDark);
        JPanel panelTop = new JPanel(new GridLayout(2, 2, 15, 5));
        JLabel lbMethod = new JLabel("Phương pháp mã hoá");
        JLabel lbKey = new JLabel("Khoá (key)");
        String[] arrPhuongPhap = {"Caesar", "Vigenere", "Affine", "Hill", "Substitution"};
        cbLoaiMH = new JComboBox<>(arrPhuongPhap);
        key = new JTextField();

        btnCreatKey = new JButton("Tạo khoá");
        btnCopyKey = new JButton("Sao chép");
        btnDeleteKey = new JButton("Xoá khoá");
        styleButton(btnCreatKey);
        styleButton(btnCopyKey);
        styleButton(btnDeleteKey);

        JPanel panelKey = new JPanel(new FlowLayout(FlowLayout.RIGHT, 5, 0));
        panelKey.add(btnCreatKey);
        panelKey.add(btnCopyKey);
        panelKey.add(btnDeleteKey);

        JPanel keyGroup = new JPanel(new BorderLayout(5, 0));
        keyGroup.add(key, BorderLayout.CENTER);
        keyGroup.add(panelKey, BorderLayout.EAST);

        panelTop.add(lbMethod);
        panelTop.add(lbKey);
        panelTop.add(cbLoaiMH);
        panelTop.add(keyGroup);

        JPanel radioTop = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JLabel language = new JLabel("Ngôn ngữ");
        vn = new JRadioButton("Tiếng Việt");
        eng = new JRadioButton("Tiếng Anh");
        groupButton = new ButtonGroup();
        groupButton.add(vn);
        groupButton.add(eng);
        radioTop.add(language);
        radioTop.add(vn);
        radioTop.add(eng);

        groupTop.add(radioTop, BorderLayout.NORTH);
        groupTop.add(panelTop, BorderLayout.CENTER);

        JPanel panelCenter = new JPanel(new GridLayout(1, 2, 20, 0));
        panelCenter.setBackground(bgDark);
        JPanel input = new JPanel(new BorderLayout(0, 5));
        JLabel lbInput = new JLabel("Dữ liệu ");
        textInput = new JTextArea();
        textInput.setMargin(new Insets(10, 10, 10, 10));
        textInput.setLineWrap(true);
        textInput.setWrapStyleWord(true);
        JPanel panelBtnIn = new JPanel(new FlowLayout(FlowLayout.CENTER));
        input.add(lbInput, BorderLayout.NORTH);
        input.add(panelBtnIn, BorderLayout.SOUTH);
        input.add(new JScrollPane(textInput), BorderLayout.CENTER);

        JPanel output = new JPanel(new BorderLayout(0, 5));
        JLabel lbOutput = new JLabel("Kết quả");
        textOutput = new JTextArea();
        textOutput.setMargin(new Insets(10, 10, 10, 10));
        textOutput.setEditable(false);
        textOutput.setLineWrap(true);
        textOutput.setWrapStyleWord(true);
        JPanel panelBtnOut = new JPanel(new FlowLayout(FlowLayout.CENTER));
        btnSaveFileOut = new JButton("Lưu file");
        styleButton(btnSaveFileOut);
        panelBtnOut.add(btnSaveFileOut);
        output.add(panelBtnOut, BorderLayout.SOUTH);
        output.add(lbOutput, BorderLayout.NORTH);
        output.add(new JScrollPane(textOutput), BorderLayout.CENTER);
        panelCenter.add(input);
        panelCenter.add(output);

        JPanel panelButton = new JPanel(new FlowLayout(FlowLayout.CENTER));
        panelButton.setBackground(bgDark);
        btnEncrypt = new JButton("Mã hoá");
        btnDecrypt = new JButton("Giải mã");
        styleButton(btnEncrypt);
        styleButton(btnDecrypt);
        btnEncrypt.setPreferredSize(new Dimension(200, 40));
        btnDecrypt.setPreferredSize(new Dimension(200, 40));
        panelButton.add(btnEncrypt);
        panelButton.add(btnDecrypt);

        add(groupTop, BorderLayout.NORTH);
        add(panelCenter, BorderLayout.CENTER);
        add(panelButton, BorderLayout.SOUTH);
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
    public boolean isSelectLanguage() {
        return groupButton.getSelection() != null;

    }

    public String getInputText() { return textInput.getText(); }
    public String getKey() { return key.getText(); }
    public String getMethods() { return cbLoaiMH.getSelectedItem().toString(); }
    public boolean isVN() { return vn.isSelected(); }
    public void setOutputText(String text) { textOutput.setText(text); }
    public void setKey(String text) { key.setText(text); }

    public void showMessage(String msg) { JOptionPane.showMessageDialog(this, msg); }

    public void addGenKey(ActionListener actionListener) { btnCreatKey.addActionListener(actionListener); }
    public void addbtnEncrypt(ActionListener actionListener) { btnEncrypt.addActionListener(actionListener); }
    public void addbtnDecrypt(ActionListener actionListener) { btnDecrypt.addActionListener(actionListener); }
    public void addCopy(ActionListener actionListener) { btnCopyKey.addActionListener(actionListener); }
    public void addDeleteKey(ActionListener actionListener) { btnDeleteKey.addActionListener(actionListener); }
    public void addBtnSaveFileOut(ActionListener actionListener) { btnSaveFileOut.addActionListener(actionListener); }
}